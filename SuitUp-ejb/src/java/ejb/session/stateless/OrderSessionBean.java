/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.OrderNotFoundException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author keithcharleschan
 */
@Stateless
public class OrderSessionBean implements OrderSessionBeanLocal {

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;
    
    

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
    public OrderEntity createNewOrder(Long staffId, OrderEntity newOrderEntity) throws StaffNotFoundException, CreateNewOrderException {
        if (newOrderEntity != null) {
            try {
                StaffEntity staffEntity = staffSessionBeanLocal.retrieveStaffByStaffId(staffId);
                newOrderEntity.setStaff(staffEntity);
                staffEntity.getOrders().add(newOrderEntity);

                entityManager.persist(newOrderEntity);

                for (OrderLineItemEntity orderLineItemEntity : newOrderEntity.getOrderLineItems()) {
                    productSessionBeanLocal.debitQuantityOnHand(orderLineItemEntity.getProduct().getProductId(), orderLineItemEntity.getQuantity());
                    entityManager.persist(orderLineItemEntity);
                }

                entityManager.flush();

                return newOrderEntity;
            } catch (ProductNotFoundException | ProductInsufficientQuantityOnHandException ex) {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();

                throw new CreateNewOrderException(ex.getMessage());
            }
        } else {
            throw new CreateNewOrderException("Sale transaction information not provided");
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
            throw new OrderNotFoundException("Sale Transaction ID " + orderId + " does not exist!");
        }
    }

    @Override
    public void updateOrder(OrderEntity orderEntity) {
        entityManager.merge(orderEntity);
    }

    // Updated in v4.1
    @Override
    public void voidRefundOrder(Long orderId) throws OrderNotFoundException, OrderAlreadyVoidedRefundedException {
        OrderEntity orderEntity = retrieveOrderByOrderId(orderId);

        if (!orderEntity.getVoidRefund()) {
            for (OrderLineItemEntity orderLineItemEntity : orderEntity.getOrderLineItems()) {
                try {
                    productSessionBeanLocal.creditQuantityOnHand(orderLineItemEntity.getProduct().getProductId(), orderLineItemEntity.getQuantity());
                } catch (ProductNotFoundException ex) {
                    ex.printStackTrace(); // Ignore exception since this should not happen
                }
            }

            orderEntity.setVoidRefund(true);
        } else {
            throw new OrderAlreadyVoidedRefundedException("The sale transaction has aready been voided/refunded");
        }
    }

    @Override
    public void deleteOrder(OrderEntity orderEntity) {
        throw new UnsupportedOperationException();
    }
}
