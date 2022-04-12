/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author meganyee
 */
@Entity
public class CustomizedJacketEntity extends CustomizedProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
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
    
    @NotNull
    @ManyToOne(optional = false)
    private JacketMeasurementEntity jacketMeasurement;
    
    public CustomizedJacketEntity() {
        super(new BigDecimal("200.00"));
    }
    
    public CustomizedJacketEntity(String name, String description, String image, BigDecimal totalPrice, String gender) {
        super(name, description, image, new BigDecimal("200.00"), totalPrice, gender); 
    }
    
    public CustomizedJacketEntity(String name, String description, BigDecimal totalPrice, String gender) {
        super(name, description, new BigDecimal("200.00"), totalPrice, gender); 
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getProductId() != null ? this.getProductId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customizedJacketId fields are not set
        if (!(object instanceof CustomizedJacketEntity)) {
            return false;
        }
        CustomizedJacketEntity other = (CustomizedJacketEntity) object;
        if ((this.getProductId() == null && other.getProductId() != null) || (this.getProductId() != null && !this.getProductId().equals(other.getProductId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[Pocket Style: " + pocketStyle.getName() + ", Jacket Style: " + jacketStyle.getName() + ", Outer Fabric: " + outerFabric.getName() + ", Inner Fabric: " + innerFabric.getName() + "]";
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

    public JacketMeasurementEntity getJacketMeasurement() {
        return jacketMeasurement;
    }

    public void setJacketMeasurement(JacketMeasurementEntity jacketMeasurement) {
        this.jacketMeasurement = jacketMeasurement;
    }
    
}
