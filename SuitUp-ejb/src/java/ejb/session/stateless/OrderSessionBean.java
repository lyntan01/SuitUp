/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.CustomerEntity;
import entity.CustomizedJacketEntity;
import entity.CustomizedPantsEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.PromotionEntity;
import entity.StandardProductEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CollectionMethodEnum;
import util.enumeration.OrderStatusEnum;
import util.exception.AddressNotFoundException;
import util.exception.CancelOrderException;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomizationNotFoundException;
import util.exception.CustomizedProductIdExistsException;
import util.exception.InputDataValidationException;
import util.exception.JacketMeasurementNotFoundException;
import util.exception.OrderNotFoundException;
import util.exception.PantsMeasurementNotFoundException;
import util.exception.PromotionCodeExpiredException;
import util.exception.PromotionFullyRedeemedException;
import util.exception.PromotionMinimumAmountNotHitException;
import util.exception.PromotionNotFoundException;
import util.exception.StandardProductInsufficientQuantityOnHandException;
import util.exception.StandardProductNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.VoidTransactionException;

/**
 *
 * @author keithcharleschan
 */
@Stateless
public class OrderSessionBean implements OrderSessionBeanLocal {

    @EJB
    private CustomizedPantsSessionBeanLocal customizedPantsSessionBeanLocal;

    @EJB
    private CustomizedJacketSessionBeanLocal customizedJacketSessionBeanLocal;

    @Resource
    private EJBContext context;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private PromotionSessionBeanLocal promotionSessionBeanLocal;

    @EJB
    private StandardProductSessionBeanLocal standardProductSessionBeanLocal;

    @EJB
    private AddressSessionBeanLocal addressSessionBeanLocal;

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public static BigDecimal EXPRESS_DELIVERY_FEE = BigDecimal.valueOf(10.0);

    public OrderSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // Updated in v4.1
    @Override
    public OrderEntity createNewOrder(Long customerId, Long addressId, OrderEntity newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException, InputDataValidationException, AddressNotFoundException, StackOverflowError {
        Set<ConstraintViolation<OrderEntity>> constraintViolations = validator.validate(newOrderEntity);

        System.out.println("TESTTT---");
        System.out.println(newOrderEntity);

        if (constraintViolations.isEmpty()) {

            if (newOrderEntity != null) {
                try {
                    if (addressId != null) {
                        AddressEntity addressEntity = addressSessionBeanLocal.retrieveAddressByAddressId(addressId);
                        newOrderEntity.setDeliveryAddress(addressEntity);
                    }

                    CustomerEntity customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                    newOrderEntity.setCustomer(customerEntity);
                    customerEntity.getOrders().add(newOrderEntity);

                    if (newOrderEntity.getCollectionMethodEnum() == CollectionMethodEnum.DELIVERY && newOrderEntity.getExpressOrder()) {
                        newOrderEntity.setTotalAmount(newOrderEntity.getTotalAmount().add(EXPRESS_DELIVERY_FEE));
                    }

                    entityManager.persist(newOrderEntity);

                    for (OrderLineItemEntity orderLineItemEntity : newOrderEntity.getOrderLineItems()) {
                        if (orderLineItemEntity.getProduct() instanceof StandardProductEntity) {
                            standardProductSessionBeanLocal.debitQuantityOnHand(orderLineItemEntity.getProduct().getProductId(), orderLineItemEntity.getQuantity());
                        }

                        entityManager.persist(orderLineItemEntity);
                    }

                    entityManager.flush();

                    return newOrderEntity;
                } catch (StandardProductNotFoundException | StandardProductInsufficientQuantityOnHandException ex) {
                    System.out.println("ERRORRR");
                    System.out.println(ex.getMessage());
                    // The line below rolls back all changes made to the database.
                    context.setRollbackOnly();

                    throw new CreateNewOrderException("Unable to create new order!");
                }
            } else {
                throw new CreateNewOrderException("Sale transaction information not provided");
            }

        } else {
            System.out.println("ERRORRR");
            System.out.println(prepareInputDataValidationErrorsMessage(constraintViolations));
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    // Updated in v4.1
    @Override
    public OrderEntity createNewOfflineOrder(Long customerId, Long addressId, OrderEntity newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException, InputDataValidationException, AddressNotFoundException, StackOverflowError {
        Set<ConstraintViolation<OrderEntity>> constraintViolations = validator.validate(newOrderEntity);

        System.out.println("TESTTT---");
        System.out.println(newOrderEntity);

        if (constraintViolations.isEmpty()) {

            if (newOrderEntity != null) {
                try {
                    if (addressId > 0) {
                        AddressEntity addressEntity = addressSessionBeanLocal.retrieveAddressByAddressId(addressId);
                        newOrderEntity.setDeliveryAddress(addressEntity);
                    }

                    CustomerEntity customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                    newOrderEntity.setCustomer(customerEntity);
                    customerEntity.getOrders().add(newOrderEntity);

                    if (newOrderEntity.getCollectionMethodEnum() == CollectionMethodEnum.DELIVERY && newOrderEntity.getExpressOrder()) {
                        newOrderEntity.setTotalAmount(newOrderEntity.getTotalAmount().add(EXPRESS_DELIVERY_FEE));
                    }
                    
                         entityManager.persist(newOrderEntity);

//                    if(newOrderEntity.getOrderStatusEnum() = null)
                    for (OrderLineItemEntity orderLineItemEntity : newOrderEntity.getOrderLineItems()) {
                        
                        System.out.println("orderLineItemEntity" + orderLineItemEntity);
                        System.out.println("orderLineItemEntity.getProduct()" + orderLineItemEntity.getProduct());
                        
                        
                        entityManager.persist(orderLineItemEntity);
                        
                        if (orderLineItemEntity.getProduct() instanceof StandardProductEntity) {
                            standardProductSessionBeanLocal.debitQuantityOnHand(orderLineItemEntity.getProduct().getProductId(), orderLineItemEntity.getQuantity());
                        } 
                        
                        else if (orderLineItemEntity.getProduct() instanceof CustomizedJacketEntity) {
                            CustomizedJacketEntity newJacket = (CustomizedJacketEntity) orderLineItemEntity.getProduct();
                            System.out.println("how do you dooooooooo" + newJacket.getPocketStyle().getCustomizationId());
//                            customizedJacketSessionBeanLocal.createNewCustomizedJacket(newJacket, newJacket.getPocketStyle().getCustomizationId(), newJacket.getJacketStyle().getCustomizationId(), newJacket.getInnerFabric().getCustomizationId(), newJacket.getOuterFabric().getCustomizationId(), customerEntity.getJacketMeasurement().getJacketMeasurementId());
                            customizedJacketSessionBeanLocal.createNewCustomizedJacket(newJacket);
                        } else if (orderLineItemEntity.getProduct() instanceof CustomizedPantsEntity) {
                            System.out.println("suppp");
                            CustomizedPantsEntity newPants = (CustomizedPantsEntity) orderLineItemEntity.getProduct();
//                            customizedPantsSessionBeanLocal.createNewCustomizedPants(newPants, newPants.getFabric().getCustomizationId(), newPants.getPantsCutting().getCustomizationId(), customerEntity.getPantsMeasurement().getPantsMeasurementId());
                            customizedPantsSessionBeanLocal.createNewCustomizedPants(newPants);
                        }

                        

                    }

               

                    entityManager.flush();

                    return newOrderEntity;
                } catch (StandardProductNotFoundException | StandardProductInsufficientQuantityOnHandException | AddressNotFoundException | CustomerNotFoundException | CustomizationNotFoundException | CustomizedProductIdExistsException | InputDataValidationException | JacketMeasurementNotFoundException | PantsMeasurementNotFoundException | UnknownPersistenceException ex) {
                    System.out.println("ERRORRR");
                    System.out.println(ex.getMessage());
                    // The line below rolls back all changes made to the database.
                    context.setRollbackOnly();

                    throw new CreateNewOrderException("Unable to create new order!");
                }
            } else {
                throw new CreateNewOrderException("Sale transaction information not provided");
            }

        } else {
            System.out.println("ERRORRR");
            System.out.println(prepareInputDataValidationErrorsMessage(constraintViolations));
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    
    

    @Override
    public List<OrderEntity> retrieveAllOrders() {
        Query query = entityManager.createQuery("SELECT st FROM OrderEntity st");

        return query.getResultList();
    }

    // Added in v4.1
    @Override
    public List<OrderLineItemEntity> retrieveOrderLineItemsByProductId(Long productId) {
        Query query = entityManager.createNamedQuery("selectAllOrderLineItemsByProductId");
        query.setParameter("inProductId", productId);

        return query.getResultList();
    }

    @Override
    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException {
        OrderEntity orderEntity = entityManager.find(OrderEntity.class, orderId);

        if (orderEntity != null) {
            orderEntity.getOrderLineItems().size();

            return orderEntity;
        } else {
            throw new OrderNotFoundException("Order ID " + orderId + " does not exist!");
        }
    }

    @Override
    public List<OrderEntity> retrieveOrderbyCustomerId(Long customerId) {
        Query query = entityManager.createQuery("SELECT o FROM OrderEntity o WHERE o.customer.customerId = :customerId");
        query.setParameter("customerId", customerId);

        return query.getResultList();
    }

    @Override
    public void updateOrder(OrderEntity orderEntity) {
        entityManager.merge(orderEntity); //have to check if this does all the fetching traversing down all the relationships
    }

    // ADMIN SIDE
    @Override
    public void voidRefundOrder(Long orderId) throws OrderNotFoundException, VoidTransactionException {
        OrderEntity orderEntity = retrieveOrderByOrderId(orderId);

        if (!orderEntity.getTransaction().getVoidRefund()) {
            try {
                transactionSessionBeanLocal.voidTransaction(orderEntity.getTransaction().getTransactionId());
            } catch (VoidTransactionException ex) {
                throw new VoidTransactionException("Error when voiding transaction: " + ex.getMessage());
            }
        } else {
            throw new VoidTransactionException("The sale transaction has aready been voided/refunded");
        }
    }

    // CUSTOMER SIDE
    @Override
    public void updateOrderToBeCancelled(Long orderId) throws OrderNotFoundException, CancelOrderException {
        OrderEntity orderEntity = retrieveOrderByOrderId(orderId);

        // Check for time (12h)
        Date orderDateTime = orderEntity.getOrderDateTime();
        Date currentTime = new Date();
        long differenceInTime = currentTime.getTime() - orderDateTime.getTime();
        long differenceInHours = (differenceInTime / (1000 * 60 * 60)) % 24;

        if ((int) differenceInHours <= 12) {
            try {
                voidRefundOrder(orderId);
                orderEntity.setOrderStatusEnum(OrderStatusEnum.CANCELLED);
                entityManager.flush();
            } catch (VoidTransactionException ex) {
                throw new CancelOrderException(ex.getMessage());
            }
        } else {
            throw new CancelOrderException("Order cannot be cancelled, as more than 12 hours have passed.");
        }

    }

    @Override
    public BigDecimal applyPromotionCode(Long orderId, String promotionCode) throws OrderNotFoundException, PromotionNotFoundException, PromotionCodeExpiredException, PromotionMinimumAmountNotHitException, PromotionFullyRedeemedException {

        OrderEntity currentOrder = retrieveOrderByOrderId(orderId);
        BigDecimal originalTotalAmount = currentOrder.getTotalAmount();
        BigDecimal amountAfterPromotionApplied = promotionSessionBeanLocal.getDiscountedAmount(promotionCode, originalTotalAmount);
        PromotionEntity promotion = promotionSessionBeanLocal.retrievePromotionByPromotionCode(promotionCode);
        currentOrder.setTotalAmount(amountAfterPromotionApplied);
        currentOrder.setPromotion(promotion);
        promotion.getOrders().add(currentOrder);

        return amountAfterPromotionApplied;

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<OrderEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
