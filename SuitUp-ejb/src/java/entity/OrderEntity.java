/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import util.enumeration.CollectionMethodEnum;
import util.enumeration.OrderStatusEnum;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityInstanceMissingInCollectionException;

/**
 *
 * @author lyntan, keithcharleschan
 */
@Entity
public class OrderEntity implements Serializable {

    //-------[Attributes]-------
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @NotNull
    @Min(1)
    private Integer totalLineItem;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer totalQuantity;
    @Column(nullable = false, precision = 11, scale = 2)

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal totalAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date orderDateTime;

    @Column(nullable = false)
    @NotNull
    private Boolean expressOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private OrderStatusEnum orderStatusEnum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private CollectionMethodEnum collectionMethodEnum;

    //-------[Relationship Attributes]-------
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customer;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AddressEntity deliveryAddress;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private PromotionEntity promotion;

    @OneToOne
    private TransactionEntity transaction;

    @OneToMany
    private List<OrderLineItemEntity> orderLineItems;

    //---------[Methods]---------
    public OrderEntity() {
        this.orderLineItems = new ArrayList<>();
    }

    public OrderEntity(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Date orderDateTime, Boolean expressOrder, OrderStatusEnum orderStatusEnum, CollectionMethodEnum collectionMethodEnum) {
        this();
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.orderDateTime = orderDateTime;
        this.expressOrder = expressOrder;
        this.orderStatusEnum = orderStatusEnum;
        this.collectionMethodEnum = collectionMethodEnum;
    }

    public OrderEntity(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Date orderDateTime, Boolean expressOrder, OrderStatusEnum orderStatusEnum, CollectionMethodEnum collectionMethodEnum, List<OrderLineItemEntity> orderLineItems) {
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.orderDateTime = orderDateTime;
        this.expressOrder = expressOrder;
        this.orderStatusEnum = orderStatusEnum;
        this.collectionMethodEnum = collectionMethodEnum;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(Date orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public Boolean getExpressOrder() {
        return expressOrder;
    }

    public void setExpressOrder(Boolean expressOrder) {
        this.expressOrder = expressOrder;
    }

    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }

    public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
        this.orderStatusEnum = orderStatusEnum;
    }

    public CollectionMethodEnum getCollectionMethodEnum() {
        return collectionMethodEnum;
    }

    public void setCollectionMethodEnum(CollectionMethodEnum collectionMethodEnum) {
        this.collectionMethodEnum = collectionMethodEnum;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AddressEntity deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PromotionEntity getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionEntity promotion) {
        this.promotion = promotion;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    public void addOrderLineItemEntity(OrderLineItemEntity orderLineItemEntity) throws EntityInstanceExistsInCollectionException {
        if (!this.orderLineItems.contains(orderLineItemEntity)) {
            this.orderLineItems.add(orderLineItemEntity);
        } else {
            throw new EntityInstanceExistsInCollectionException("Order Line Item already exist");
        }
    }

    public void removeOrderLineItemEntity(OrderLineItemEntity orderLineItemEntity) throws EntityInstanceMissingInCollectionException {
        if (this.orderLineItems.contains(orderLineItemEntity)) {
            this.orderLineItems.remove(orderLineItemEntity);
        } else {
            throw new EntityInstanceMissingInCollectionException("Order Line Item does not exist");
        }
    }

    public List<OrderLineItemEntity> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItemEntity> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getOrderId() != null ? getOrderId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the orderId fields are not set
        if (!(object instanceof OrderEntity)) {
            return false;
        }
        OrderEntity other = (OrderEntity) object;
        if ((this.getOrderId() == null && other.getOrderId() != null) || (this.getOrderId() != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrderEntity{" + "orderId=" + orderId + ", totalLineItem=" + totalLineItem + ", totalQuantity=" + totalQuantity + ", totalAmount=" + totalAmount + ", orderDateTime=" + orderDateTime + ", expressOrder=" + expressOrder + ", orderStatusEnum=" + orderStatusEnum + ", collectionMethodEnum=" + collectionMethodEnum + ", customer=" + customer + ", deliveryAddress=" + deliveryAddress + ", promotion=" + promotion + ", transaction=" + transaction + ", orderLineItems=" + orderLineItems + '}';
    }

}
