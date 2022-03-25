/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.enumeration.AppointmentTypeEnum;

/**
 *
 * @author lyntan, keithcharleschan
 */
@Entity
public class AppointmentEntity implements Serializable {

    //-------[Attributes]-------
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private AppointmentTypeEnum appointmentTypeEnum;

    @Column(nullable = false)
    @NotNull
    private Boolean isFree;

    //-------[Relationship Attributes]-------
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private StoreEntity store;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customer;

    @OneToOne
    private TransactionEntity transaction;

    //---------[Methods]---------
    public AppointmentEntity() {

    }

    public AppointmentEntity(Date appointmentDateTime, AppointmentTypeEnum appointmentTypeEnum) {
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentTypeEnum = appointmentTypeEnum;
    }

    public AppointmentEntity(Date appointmentDateTime, AppointmentTypeEnum appointmentTypeEnum, boolean isFree) {
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentTypeEnum = appointmentTypeEnum;
        this.isFree = isFree;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(Date appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentTypeEnum getAppointmentTypeEnum() {
        return appointmentTypeEnum;
    }

    public void setAppointmentTypeEnum(AppointmentTypeEnum appointmentTypeEnum) {
        this.appointmentTypeEnum = appointmentTypeEnum;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getAppointmentId() != null ? getAppointmentId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the appointmentId fields are not set
        if (!(object instanceof AppointmentEntity)) {
            return false;
        }
        AppointmentEntity other = (AppointmentEntity) object;
        if ((this.getAppointmentId() == null && other.getAppointmentId() != null) || (this.getAppointmentId() != null && !this.appointmentId.equals(other.appointmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AppointmentEntity{" + "appointmentId=" + appointmentId + ", appointmentDateTime=" + appointmentDateTime + ", appointmentTypeEnum=" + appointmentTypeEnum + ", isFree=" + isFree + ", store=" + store + ", customer=" + customer + ", transaction=" + transaction + '}';
    }

}
