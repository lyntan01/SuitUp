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
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 *
 * @author meganyee
 */
@Entity
public class CustomizedJacketEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customizedJacketId;
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
    @ManyToOne(optional = false)
    private PocketStyleEntity pocketStyle;
    
    @NotNull
    @ManyToOne(optional = false)
    private JacketStyleEntity jacketStyle;
    
    @NotNull
    @ManyToOne(optional = false)
    private FabricEntity innerFabric;
    
    @NotNull
    @ManyToOne(optional = false)
    private FabricEntity outerFabric;
    

    public Long getCustomizedJacketId() {
        return customizedJacketId;
    }

    public void setCustomizedJacketId(Long customizedJacketId) {
        this.customizedJacketId = customizedJacketId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customizedJacketId != null ? customizedJacketId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customizedJacketId fields are not set
        if (!(object instanceof CustomizedJacketEntity)) {
            return false;
        }
        CustomizedJacketEntity other = (CustomizedJacketEntity) object;
        if ((this.customizedJacketId == null && other.customizedJacketId != null) || (this.customizedJacketId != null && !this.customizedJacketId.equals(other.customizedJacketId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomizedJacketEntity[ id=" + customizedJacketId + " ]";
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

    public PocketStyleEntity getPocketStyle() {
        return pocketStyle;
    }

    public void setPocketStyle(PocketStyleEntity pocketStyle) {
        this.pocketStyle = pocketStyle;
    }

    public JacketStyleEntity getJacketStyle() {
        return jacketStyle;
    }

    public void setJacketStyle(JacketStyleEntity jacketStyle) {
        this.jacketStyle = jacketStyle;
    }

    public FabricEntity getInnerFabric() {
        return innerFabric;
    }

    public void setInnerFabric(FabricEntity innerFabric) {
        this.innerFabric = innerFabric;
    }

    public FabricEntity getOuterFabric() {
        return outerFabric;
    }

    public void setOuterFabric(FabricEntity outerFabric) {
        this.outerFabric = outerFabric;
    }
    
}
