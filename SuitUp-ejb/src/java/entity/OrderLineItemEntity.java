/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author keithcharleschan
 */
@Entity
public class OrderLineItemEntity implements Serializable {

    //-------[Attributes]-------
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineItemId;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;

    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal subTotal;

    //-------[Relationship Attributes]-------
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ProductEntity product;

    //---------[Methods]---------
    public OrderLineItemEntity() {

    }

    public OrderLineItemEntity(Integer quantity, BigDecimal subTotal, ProductEntity product) {
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.product = product;
    }

    public Long getOrderLineItemId() {
        return orderLineItemId;
    }

    public void setOrderLineItemId(Long orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getOrderLineItemId() != null ? getOrderLineItemId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the orderLineItemId fields are not set
        if (!(object instanceof OrderLineItemEntity)) {
            return false;
        }
        OrderLineItemEntity other = (OrderLineItemEntity) object;
        if ((this.getOrderLineItemId() == null && other.getOrderLineItemId() != null) || (this.getOrderLineItemId() != null && !this.orderLineItemId.equals(other.orderLineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return product.getName() + " × " + quantity + " ($" + subTotal + ")";
    }

    
    

}
