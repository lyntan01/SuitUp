/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author lyntan
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PromotionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long promotionId;
    @Column(nullable = false, unique = true, length = 20)
    @NotNull
    @Size(min = 3, max = 20)
    private String promotionCode;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer maxNumOfUsages;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal minimumSpending;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date expiryDate;
    
    @Transient
    private String discountString;
    
    @OneToMany(mappedBy = "promotion")
    @JoinColumn(nullable = true)
    private List<OrderEntity> orders;

    public PromotionEntity() {
        this.orders = new ArrayList<>();
    }

    public PromotionEntity(String promotionCode, Integer maxNumOfUsages, BigDecimal minimumSpending, Date expiryDate, String discountString) {
        this();
        this.promotionCode = promotionCode;
        this.maxNumOfUsages = maxNumOfUsages;
        this.minimumSpending = minimumSpending;
        this.expiryDate = expiryDate;
        this.discountString = discountString;
    }
    
    

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
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
        if (!(object instanceof PromotionEntity)) {
            return false;
        }
        PromotionEntity other = (PromotionEntity) object;
        if ((this.promotionId == null && other.promotionId != null) || (this.promotionId != null && !this.promotionId.equals(other.promotionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PromotionEntity[ id=" + promotionId + " ]";
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Integer getMaxNumOfUsages() {
        return maxNumOfUsages;
    }

    public void setMaxNumOfUsages(Integer maxNumOfUsages) {
        this.maxNumOfUsages = maxNumOfUsages;
    }

    public BigDecimal getMinimumSpending() {
        return minimumSpending;
    }

    public void setMinimumSpending(BigDecimal minimumSpending) {
        this.minimumSpending = minimumSpending;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public String getDiscountString() {
        if (this.discountString == null) {
           if (this instanceof AbsolutePromotionEntity) {
               AbsolutePromotionEntity absolutePromotion = (AbsolutePromotionEntity) this;
               this.discountString = NumberFormat.getCurrencyInstance().format(absolutePromotion.getAbsoluteDiscount());
           } else if (this instanceof PercentagePromotionEntity) {
               PercentagePromotionEntity percentagePromotion = (PercentagePromotionEntity) this;
               this.discountString = percentagePromotion.getPercentageDiscount() + "%";
           }
        }
        return this.discountString;
    }

    public void setDiscountString(String discountString) {
        this.discountString = discountString;
    }
    
}
