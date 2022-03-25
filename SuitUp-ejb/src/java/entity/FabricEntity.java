/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
/**
 *
 * @author meganyee
 */
@Entity
public class FabricEntity extends CustomizationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ColourEntity colour;
    
    public FabricEntity() {
        super();
    }
    
    public FabricEntity(String name, BigDecimal additionalPrice, String description, String image) {
        super(name, additionalPrice, description, image);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getCustomizationId() != null ? this.getCustomizationId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the fabricId fields are not set
        if (!(object instanceof FabricEntity)) {
            return false;
        }
        FabricEntity other = (FabricEntity) object;
        if ((this.getCustomizationId() == null && other.getCustomizationId() != null) || (this.getCustomizationId() != null && !this.getCustomizationId().equals(other.getCustomizationId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FabricEntity[ id=" + this.getCustomizationId() + " ]";
    }

    public ColourEntity getColour() {
        return colour;
    }

    public void setColour(ColourEntity colour) {
        this.colour = colour;
    }
  
}
