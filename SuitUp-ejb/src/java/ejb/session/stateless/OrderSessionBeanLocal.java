/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import entity.OrderLineItemEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.exception.AddressNotFoundException;
import util.exception.CancelOrderException;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OrderNotFoundException;
import util.exception.PromotionCodeExpiredException;
import util.exception.PromotionFullyRedeemedException;
import util.exception.PromotionMinimumAmountNotHitException;
import util.exception.PromotionNotFoundException;
import util.exception.VoidTransactionException;

/**
 *
 * @author keithcharleschan
 */
@Local
public interface OrderSessionBeanLocal {

    public OrderEntity createNewOrder(Long customerId, Long addressId, OrderEntity newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException, InputDataValidationException, AddressNotFoundException, StackOverflowError;

    public List<OrderEntity> retrieveAllOrders();

    public List<OrderLineItemEntity> retrieveOrderLineItemsByProductId(Long productId);

    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException;
    
    public List<OrderEntity> retrieveOrderbyCustomerId(Long customerId);

    public void updateOrder(OrderEntity orderEntity);

    public void voidRefundOrder(Long orderId) throws OrderNotFoundException, VoidTransactionException;

    public void updateOrderToBeCancelled(Long orderId) throws OrderNotFoundException, CancelOrderException;

    public BigDecimal applyPromotionCode(Long orderId, String promotionCode) throws OrderNotFoundException, PromotionNotFoundException, PromotionCodeExpiredException, PromotionMinimumAmountNotHitException, PromotionFullyRedeemedException;

    public OrderEntity createNewOfflineOrder(Long customerId, Long addressId, OrderEntity newOrderEntity) throws CustomerNotFoundException, CreateNewOrderException, InputDataValidationException, AddressNotFoundException, StackOverflowError;

}
