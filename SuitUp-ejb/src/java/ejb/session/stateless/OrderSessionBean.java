/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.CustomerEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.PromotionEntity;
import entity.StandardProductEntity;
import java.math.BigDecimal;
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
import util.enumeration.OrderStatusEnum;
import util.exception.AddressNotFoundException;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OrderNotFoundException;
import util.exception.PromotionCodeExpiredException;
import util.exception.PromotionFullyRedeemedException;
import util.exception.PromotionMinimumAmountNotHitException;
import util.exception.PromotionNotFoundException;
import util.exception.StandardProductInsufficientQuantityOnHandException;
import util.exception.StandardProductNotFoundException;
import util.exception.VoidTransactionException;

/**
 *
 * @author keithcharleschan
 */
@Stateless
public class OrderSessionBean implements OrderSessionBeanLocal {

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

    public OrderSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // Updated in v4.1
    @Override
    public OrderEntity createNewOrder(Long customerId, Long addressId, OrderEntity newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException, InputDataValidationException, AddressNotFoundException {
        Set<ConstraintViolation<OrderEntity>> constraintViolations = validator.validate(newOrderEntity);

        if (constraintViolations.isEmpty()) {

            if (newOrderEntity != null) {
                try {
                    AddressEntity addressEntity = addressSessionBeanLocal.retrieveAddressByAddressId(addressId);
                    newOrderEntity.setDeliveryAddress(addressEntity);

                    CustomerEntity customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                    newOrderEntity.setCustomer(customerEntity);
                    customerEntity.getOrders().add(newOrderEntity);
                    

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
                    // The line below rolls back all changes made to the database.
                    context.setRollbackOnly();

                    throw new CreateNewOrderException("Unable to create new order!");
                }
            } else {
                throw new CreateNewOrderException("Sale transaction information not provided");
            }

        } else {
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

    // Updated in v4.1
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

    @Override
    public void updateOrderToBeCancelled(Long orderId) throws OrderNotFoundException {
        OrderEntity orderEntity = retrieveOrderByOrderId(orderId);

        orderEntity.setOrderStatusEnum(OrderStatusEnum.CANCELLED);
        entityManager.flush();
    }

    @Override
    public BigDecimal calculateTotalAmount(Long orderId) throws OrderNotFoundException {

        try {
            OrderEntity orderEntity = retrieveOrderByOrderId(orderId);
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (OrderLineItemEntity orderLineItemEntity : orderEntity.getOrderLineItems()) {
                totalAmount = totalAmount.add(orderLineItemEntity.getSubTotal());
            }

            return totalAmount;

        } catch (OrderNotFoundException ex) {
            throw new OrderNotFoundException("Order ID " + orderId + " does not exist!");
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

