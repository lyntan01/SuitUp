/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 *
 * @author lyntan
 */
@Entity
public class AbsolutePromotionEntity extends PromotionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal absoluteDiscount;

    public AbsolutePromotionEntity() {
        super();
    }
    
    public AbsolutePromotionEntity(BigDecimal absoluteDiscount, String promotionCode, Integer maxNumOfUsages, BigDecimal minimumSpending, Date expiryDate) {
        super(promotionCode, maxNumOfUsages, minimumSpending, expiryDate, NumberFormat.getCurrencyInstance().format(absoluteDiscount));
        this.absoluteDiscount = absoluteDiscount;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (promotionId != null ? promotionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the promotionId fields are not set
        if (!(object instanceof AbsolutePromotionEntity)) {
            return false;
        }
        AbsolutePromotionEntity other = (AbsolutePromotionEntity) object;
        if ((this.promotionId == null && other.promotionId != null) || (this.promotionId != null && !this.promotionId.equals(other.promotionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AbsolutePromotionEntity[ promotionId=" + promotionId + " ]";
    }

    public BigDecimal getAbsoluteDiscount() {
        return absoluteDiscount;
    }

    public void setAbsoluteDiscount(BigDecimal absoluteDiscount) {
        this.absoluteDiscount = absoluteDiscount;
    }
    
}
