/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import entity.OrderLineItemEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewOrderException;
import util.exception.OrderAlreadyVoidedRefundedException;
import util.exception.OrderNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.TransactionNotFoundException;

/**
 *
 * @author keithcharleschan
 */
@Local
public interface OrderSessionBeanLocal {

    public OrderEntity createNewOrder(Long staffId, OrderEntity newOrderEntity) throws StaffNotFoundException, CreateNewOrderException;

    public List<OrderEntity> retrieveAllOrders();

    public List<OrderLineItemEntity> retrieveOrderLineItemsByProductId(Long productId);

    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException;

    public void updateOrder(OrderEntity orderEntity);

    public void voidRefundOrder(Long orderId) throws OrderNotFoundException, OrderAlreadyVoidedRefundedException, TransactionNotFoundException;

    public void deleteOrder(OrderEntity orderEntity);

}
