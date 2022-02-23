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
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal neck;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal frontLength;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal chestGrith;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal frontChestWidth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal upperWaistGrith;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal hipGrith;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal armhole;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal shoulderWidth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal sleeveLength;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal backwidth;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal bicepGrith;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal forearmGrith;
    @Column(nullable = false, precision=8, scale=1)
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer=7, fraction=1)
    private BigDecimal wristGrith;

    public JacketMeasurementEntity() {
    }

    public JacketMeasurementEntity(BigDecimal neck, BigDecimal frontLength, BigDecimal chestGrith, BigDecimal frontChestWidth, BigDecimal upperWaistGrith, BigDecimal hipGrith, BigDecimal armhole, BigDecimal shoulderWidth, BigDecimal sleeveLength, BigDecimal backwidth, BigDecimal bicepGrith, BigDecimal forearmGrith, BigDecimal wristGrith) {
        this.neck = neck;
        this.frontLength = frontLength;
        this.chestGrith = chestGrith;
        this.frontChestWidth = frontChestWidth;
        this.upperWaistGrith = upperWaistGrith;
        this.hipGrith = hipGrith;
        this.armhole = armhole;
        this.shoulderWidth = shoulderWidth;
        this.sleeveLength = sleeveLength;
        this.backwidth = backwidth;
        this.bicepGrith = bicepGrith;
        this.forearmGrith = forearmGrith;
        this.wristGrith = wristGrith;
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

    public BigDecimal getChestGrith() {
        return chestGrith;
    }

    public void setChestGrith(BigDecimal chestGrith) {
        this.chestGrith = chestGrith;
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

    public BigDecimal getHipGrith() {
        return hipGrith;
    }

    public void setHipGrith(BigDecimal hipGrith) {
        this.hipGrith = hipGrith;
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

    public BigDecimal getBicepGrith() {
        return bicepGrith;
    }

    public void setBicepGrith(BigDecimal bicepGrith) {
        this.bicepGrith = bicepGrith;
    }

    public BigDecimal getForearmGrith() {
        return forearmGrith;
    }

    public void setForearmGrith(BigDecimal forearmGrith) {
        this.forearmGrith = forearmGrith;
    }

    public BigDecimal getWristGrith() {
        return wristGrith;
    }

    public void setWristGrith(BigDecimal wristGrith) {
        this.wristGrith = wristGrith;
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
