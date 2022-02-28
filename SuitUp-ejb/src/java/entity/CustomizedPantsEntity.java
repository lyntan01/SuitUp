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
public class CustomizedPantsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customizedPantsId;
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
    private FabricEntity fabric;
    
    @NotNull
    @ManyToOne(optional = false)
    private PantsCuttingEntity pantsCutting;

    public Long getCustomizedPantsId() {
        return customizedPantsId;
    }

    public void setCustomizedPantsId(Long customizedPantsId) {
        this.customizedPantsId = customizedPantsId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customizedPantsId != null ? customizedPantsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customizedPantsId fields are not set
        if (!(object instanceof CustomizedPantsEntity)) {
            return false;
        }
        CustomizedPantsEntity other = (CustomizedPantsEntity) object;
        if ((this.customizedPantsId == null && other.customizedPantsId != null) || (this.customizedPantsId != null && !this.customizedPantsId.equals(other.customizedPantsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomizedPantsEntity[ id=" + customizedPantsId + " ]";
    }
    
}
