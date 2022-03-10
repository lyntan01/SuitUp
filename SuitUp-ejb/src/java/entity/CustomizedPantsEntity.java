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
public class CustomizedPantsEntity extends CustomizedProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotNull
    @ManyToOne(optional = false)
    private FabricEntity fabric;
    
    @NotNull
    @ManyToOne(optional = false)
    private PantsCuttingEntity pantsCutting;
    
    @NotNull
    @ManyToOne(optional = false)
    private PantsMeasurementEntity pantsMeasurement;
    
    public CustomizedPantsEntity() {
    }
    
    public CustomizedPantsEntity(String name, String description, BigDecimal basePrice, BigDecimal totalPrice, String gender) {
        super(name, description, basePrice, totalPrice, gender); 
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getProductId() != null ? this.getProductId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customizedPantsId fields are not set
        if (!(object instanceof CustomizedPantsEntity)) {
            return false;
        }
        CustomizedPantsEntity other = (CustomizedPantsEntity) object;
        if ((this.getProductId() == null && other.getProductId() != null) || (this.getProductId() != null && !this.getProductId().equals(other.getProductId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomizedPantsEntity[ id=" + this.getProductId() + " ]";
    }

    public FabricEntity getFabric() {
        return fabric;
    }

    public void setFabric(FabricEntity fabric) {
        this.fabric = fabric;
    }

    public PantsCuttingEntity getPantsCutting() {
        return pantsCutting;
    }

    public void setPantsCutting(PantsCuttingEntity pantsCutting) {
        this.pantsCutting = pantsCutting;
    }

    public PantsMeasurementEntity getPantsMeasurement() {
        return pantsMeasurement;
    }

    public void setPantsMeasurement(PantsMeasurementEntity pantsMeasurement) {
        this.pantsMeasurement = pantsMeasurement;
    }
    
}
