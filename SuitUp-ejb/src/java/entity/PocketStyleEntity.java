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
public class PocketStyleEntity extends CustomizationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public PocketStyleEntity() {
        super();
    }
    
    public PocketStyleEntity(String name, BigDecimal additionalPrice, String description, String image) {
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
        // TODO: Warning - this method won't work in the case the pocketStyleId fields are not set
        if (!(object instanceof PocketStyleEntity)) {
            return false;
        }
        PocketStyleEntity other = (PocketStyleEntity) object;
        if ((this.getCustomizationId() == null && other.getCustomizationId() != null) || (this.getCustomizationId() != null && !this.getCustomizationId().equals(other.getCustomizationId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PocketStyleEntity[ id=" + this.getCustomizationId() + " ]";
    }
}
