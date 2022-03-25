/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;

/**
 *
 * @author meganyee
 */
@Entity
public class PantsCuttingEntity extends CustomizationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public PantsCuttingEntity() {
        super();
    }
    
    public PantsCuttingEntity(String name, BigDecimal additionalPrice, String description, String image) {
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
        // TODO: Warning - this method won't work in the case the pantsCuttingId fields are not set
        if (!(object instanceof PantsCuttingEntity)) {
            return false;
        }
        PantsCuttingEntity other = (PantsCuttingEntity) object;
        if ((this.getCustomizationId() == null && other.getCustomizationId() != null) || (this.getCustomizationId() != null && !this.getCustomizationId().equals(other.getCustomizationId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PantsCuttingEntity[ id=" + this.getCustomizationId() + " ]";
    }

}
