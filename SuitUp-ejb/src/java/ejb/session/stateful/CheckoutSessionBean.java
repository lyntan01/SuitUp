///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ejb.session.stateful;
//
//import ejb.session.stateless.OrderSessionBean;
//import entity.OrderEntity;
//import entity.OrderLineItemEntity;
//import entity.ProductEntity;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.ejb.EJB;
//import javax.ejb.Remove;
//import javax.ejb.Stateful;
//import util.enumeration.OrderStatusEnum;
//import util.exception.CreateNewOrderException;
//import util.exception.StaffNotFoundException;
//
//@Stateful
//public class CheckoutSessionBean implements CheckoutSessionBeanLocal, CheckoutSessionBeanRemote {
//
//    @EJB
//    private OrderSessionBean orderEntitySessionBeanLocal;
//
//    private List<OrderLineItemEntity> orderLineItemEntities;
//    private Integer totalLineItem;
//    private Integer totalQuantity;
//    private BigDecimal totalAmount;
//
//    public CheckoutSessionBean() {
//        initialiseState();
//    }
//
//    @Remove
//    public void remove() {
//    }
//
//    private void initialiseState() {
//        orderLineItemEntities = new ArrayList<>();
//        totalLineItem = 0;
//        totalQuantity = 0;
//        totalAmount = new BigDecimal("0.00");
//    }
//
//    @Override
//    public BigDecimal addItem(ProductEntity productEntity, Integer quantity) {
//        BigDecimal subTotal = productEntity.getUnitPrice().multiply(new BigDecimal(quantity));
//
//        ++totalLineItem;
//        orderLineItemEntities.add(new OrderLineItemEntity(totalLineItem, productEntity, quantity, productEntity.getUnitPrice(), subTotal));
//        totalQuantity += quantity;
//        totalAmount = totalAmount.add(subTotal);
//
//        return subTotal;
//    }
//
//    // Updated in v4.1
//    @Override
//    public OrderEntity doCheckout(Long customerId) throws StaffNotFoundException, CreateNewOrderException {
//        Long newOrderId = orderEntitySessionBeanLocal.createNewOrder(customerId, new OrderEntity(totalLineItem, new Date(), false, OrderStatusEnum.PROCESSING, orderLineItemEntities));
//        OrderEntity newOrderEntity = orderEntitySessionBeanLocal.retrieveOrderByOrderId(newOrderId);
//        
//        initialiseState();
//
//        return newOrderEntity;
//    }
//
//    @Override
//    public void clearShoppingCart() {
//        initialiseState();
//    }
//
//    @Override
//    public List<OrderLineItemEntity> getOrderLineItemEntities() {
//        return orderLineItemEntities;
//    }
//
//    @Override
//    public void setOrderLineItemEntities(List<OrderLineItemEntity> orderLineItemEntities) {
//        this.orderLineItemEntities = orderLineItemEntities;
//    }
//
//    @Override
//    public Integer getTotalLineItem() {
//        return totalLineItem;
//    }
//
//    @Override
//    public void setTotalLineItem(Integer totalLineItem) {
//        this.totalLineItem = totalLineItem;
//    }
//
//    @Override
//    public Integer getTotalQuantity() {
//        return totalQuantity;
//    }
//
//    @Override
//    public void setTotalQuantity(Integer totalQuantity) {
//        this.totalQuantity = totalQuantity;
//    }
//
//    @Override
//    public BigDecimal getTotalAmount() {
//        return totalAmount;
//    }
//
//    @Override
//    public void setTotalAmount(BigDecimal totalAmount) {
//        this.totalAmount = totalAmount;
//    }
//}
