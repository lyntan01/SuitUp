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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 *
 * @author xianhui
 */
@Entity
public class PantsMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pantsMeasurementId;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal legsLength;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal lowerWaistGirth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal hipGirth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal crotch;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal thighGirth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal kneeGirth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal calfGirth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal pantsOpeningWidth;

    public PantsMeasurementEntity() {
    }

    public PantsMeasurementEntity(BigDecimal legsLength, BigDecimal lowerWaistGirth, BigDecimal hipGirth, BigDecimal crotch, BigDecimal thighGrith, BigDecimal kneeGrith, BigDecimal calfGrith, BigDecimal pantsOpeningWidth) {
        this.legsLength = legsLength;
        this.lowerWaistGirth = lowerWaistGirth;
        this.hipGirth = hipGirth;
        this.crotch = crotch;
        this.thighGirth = thighGrith;
        this.kneeGirth = kneeGrith;
        this.calfGirth = calfGrith;
        this.pantsOpeningWidth = pantsOpeningWidth;
    }

    public Long getPantsMeasurementId() {
        return pantsMeasurementId;
    }

    public void setPantsMeasurementId(Long pantsMeasurementId) {
        this.pantsMeasurementId = pantsMeasurementId;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pantsMeasurementId != null ? pantsMeasurementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the pantsMeasurementId fields are not set
        if (!(object instanceof PantsMeasurementEntity)) {
            return false;
        }
        PantsMeasurementEntity other = (PantsMeasurementEntity) object;
        if ((this.pantsMeasurementId == null && other.pantsMeasurementId != null) || (this.pantsMeasurementId != null && !this.pantsMeasurementId.equals(other.pantsMeasurementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PantsMeasurementEntity[ id=" + pantsMeasurementId + " ]";
    }

    public BigDecimal getLegsLength() {
        return legsLength;
    }

    public void setLegsLength(BigDecimal legsLength) {
        this.legsLength = legsLength;
    }

    public BigDecimal getLowerWaistGirth() {
        return lowerWaistGirth;
    }

    public void setLowerWaistGirth(BigDecimal lowerWaistGirth) {
        this.lowerWaistGirth = lowerWaistGirth;
    }

    public BigDecimal getHipGirth() {
        return hipGirth;
    }

    public void setHipGirth(BigDecimal hipGirth) {
        this.hipGirth = hipGirth;
    }

    public BigDecimal getCrotch() {
        return crotch;
    }

    public void setCrotch(BigDecimal crotch) {
        this.crotch = crotch;
    }

    public BigDecimal getThighGirth() {
        return thighGirth;
    }

    public void setThighGirth(BigDecimal thighGirth) {
        this.thighGirth = thighGirth;
    }

    public BigDecimal getKneeGirth() {
        return kneeGirth;
    }

    public void setKneeGirth(BigDecimal kneeGirth) {
        this.kneeGirth = kneeGirth;
    }

    public BigDecimal getCalfGirth() {
        return calfGirth;
    }

    public void setCalfGirth(BigDecimal calfGirth) {
        this.calfGirth = calfGirth;
    }

    public BigDecimal getPantsOpeningWidth() {
        return pantsOpeningWidth;
    }

    public void setPantsOpeningWidth(BigDecimal pantsOpeningWidth) {
        this.pantsOpeningWidth = pantsOpeningWidth;
    }
    
}
