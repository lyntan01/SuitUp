/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author lyntan
 */
@Entity
public class PercentagePromotionEntity extends PromotionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    @Max(100)
    private Integer percentageDiscount;

    public PercentagePromotionEntity() {
        super();
    }

    public PercentagePromotionEntity(Integer percentageDiscount, String promotionCode, Integer maxNumOfUsages, BigDecimal minimumSpending, Date expiryDate) {
        super(promotionCode, maxNumOfUsages, minimumSpending, expiryDate);
        this.percentageDiscount = percentageDiscount;
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
        if (!(object instanceof PercentagePromotionEntity)) {
            return false;
        }
        PercentagePromotionEntity other = (PercentagePromotionEntity) object;
        if ((this.promotionId == null && other.promotionId != null) || (this.promotionId != null && !this.promotionId.equals(other.promotionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PercentagePromotionEntity[ promotionId=" + promotionId + " ]";
    }

    public Integer getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(Integer percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }
    
}
