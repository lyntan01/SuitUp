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
public class JacketMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jacketMeasurementId;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal neck;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal frontLength;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal chestGirth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal frontChestWidth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal upperWaistGrith;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal hipGirth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal armhole;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal shoulderWidth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal sleeveLength;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal backwidth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal bicepGirth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal forearmGirth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.1")
    @Digits(integer=7, fraction=1)
    private BigDecimal wristGirth;

    public JacketMeasurementEntity() {
    }

    public JacketMeasurementEntity(BigDecimal neck, BigDecimal frontLength, BigDecimal chestGrith, BigDecimal frontChestWidth, BigDecimal upperWaistGrith, BigDecimal hipGrith, BigDecimal armhole, BigDecimal shoulderWidth, BigDecimal sleeveLength, BigDecimal backwidth, BigDecimal bicepGrith, BigDecimal forearmGrith, BigDecimal wristGrith) {
        this.neck = neck;
        this.frontLength = frontLength;
        this.chestGirth = chestGrith;
        this.frontChestWidth = frontChestWidth;
        this.upperWaistGrith = upperWaistGrith;
        this.hipGirth = hipGrith;
        this.armhole = armhole;
        this.shoulderWidth = shoulderWidth;
        this.sleeveLength = sleeveLength;
        this.backwidth = backwidth;
        this.bicepGirth = bicepGrith;
        this.forearmGirth = forearmGrith;
        this.wristGirth = wristGrith;
    }

    public Long getJacketMeasurementId() {
        return jacketMeasurementId;
    }

    public void setJacketMeasurementId(Long jacketMeasurementId) {
        this.jacketMeasurementId = jacketMeasurementId;
    }

    public BigDecimal getNeck() {
        return neck;
    }

    public void setNeck(BigDecimal neck) {
        this.neck = neck;
    }

    public BigDecimal getFrontLength() {
        return frontLength;
    }

    public void setFrontLength(BigDecimal frontLength) {
        this.frontLength = frontLength;
    }

    public BigDecimal getChestGirth() {
        return chestGirth;
    }

    public void setChestGirth(BigDecimal chestGirth) {
        this.chestGirth = chestGirth;
    }

    public BigDecimal getFrontChestWidth() {
        return frontChestWidth;
    }

    public void setFrontChestWidth(BigDecimal frontChestWidth) {
        this.frontChestWidth = frontChestWidth;
    }

    public BigDecimal getUpperWaistGrith() {
        return upperWaistGrith;
    }

    public void setUpperWaistGrith(BigDecimal upperWaistGrith) {
        this.upperWaistGrith = upperWaistGrith;
    }

    public BigDecimal getHipGirth() {
        return hipGirth;
    }

    public void setHipGirth(BigDecimal hipGirth) {
        this.hipGirth = hipGirth;
    }

    public BigDecimal getArmhole() {
        return armhole;
    }

    public void setArmhole(BigDecimal armhole) {
        this.armhole = armhole;
    }

    public BigDecimal getShoulderWidth() {
        return shoulderWidth;
    }

    public void setShoulderWidth(BigDecimal shoulderWidth) {
        this.shoulderWidth = shoulderWidth;
    }

    public BigDecimal getSleeveLength() {
        return sleeveLength;
    }

    public void setSleeveLength(BigDecimal sleeveLength) {
        this.sleeveLength = sleeveLength;
    }

    public BigDecimal getBackwidth() {
        return backwidth;
    }

    public void setBackwidth(BigDecimal backwidth) {
        this.backwidth = backwidth;
    }

    public BigDecimal getBicepGirth() {
        return bicepGirth;
    }

    public void setBicepGirth(BigDecimal bicepGirth) {
        this.bicepGirth = bicepGirth;
    }

    public BigDecimal getForearmGirth() {
        return forearmGirth;
    }

    public void setForearmGirth(BigDecimal forearmGirth) {
        this.forearmGirth = forearmGirth;
    }

    public BigDecimal getWristGirth() {
        return wristGirth;
    }

    public void setWristGirth(BigDecimal wristGirth) {
        this.wristGirth = wristGirth;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jacketMeasurementId != null ? jacketMeasurementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the jacketMeasurementId fields are not set
        if (!(object instanceof JacketMeasurementEntity)) {
            return false;
        }
        JacketMeasurementEntity other = (JacketMeasurementEntity) object;
        if ((this.jacketMeasurementId == null && other.jacketMeasurementId != null) || (this.jacketMeasurementId != null && !this.jacketMeasurementId.equals(other.jacketMeasurementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.JacketMeasurementEntity[ id=" + jacketMeasurementId + " ]";
    }
    
}
