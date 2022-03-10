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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author meganyee
 */
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public class CustomizedProductEntity extends ProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Digits(integer = 9, fraction = 2)
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal basePrice;
    @NotNull
    @Digits(integer = 9, fraction = 2)
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal totalPrice;
    @NotNull
    @Size(min = 3, max = 20)
    private String gender;
    
    public CustomizedProductEntity() {
    }
    
    public CustomizedProductEntity(String name, String description, BigDecimal basePrice, BigDecimal totalPrice, String gender) {
        super(name, description);
        this.basePrice = basePrice;
        this.totalPrice = totalPrice;
        this.gender = gender;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getProductId() != null ? this.getProductId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the productId fields are not set
        if (!(object instanceof ProductEntity)) {
            return false;
        }
        ProductEntity other = (ProductEntity) object;
        if ((this.getProductId() == null && other.getProductId() != null) || (this.getProductId() != null && !this.getProductId().equals(other.getProductId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomizedProductEntity[ id=" + getProductId() + " ]";
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
}
