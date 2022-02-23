/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author lyntan
 */
@Entity
public class StoreEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String storeName;
    @Column(length = 128)
    @Size(max = 128)
    private String storeDescription;
    @Column(nullable = false, length = 10)
    @NotNull
    @Size(max = 10)
    private String openingHour;
    @Column(nullable = false, length = 10)
    @NotNull
    @Size(max = 10)
    private String closingHour;
    @Column(nullable = false, length = 10)
    @NotNull
    @Size(max = 10)
    private String contactNumber;
    
    @OneToMany
    @JoinColumn(nullable = false)
    private List<StaffEntity> staff;
    
    @OneToOne(optional = false)
    private AddressEntity address;
    
    @OneToMany(mappedBy = "store")
    @JoinColumn(nullable = true)
    private List<AppointmentEntity> appointments;

    public StoreEntity() {
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (storeId != null ? storeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the storeId fields are not set
        if (!(object instanceof StoreEntity)) {
            return false;
        }
        StoreEntity other = (StoreEntity) object;
        if ((this.storeId == null && other.storeId != null) || (this.storeId != null && !this.storeId.equals(other.storeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StoreEntity[ id=" + storeId + " ]";
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<StaffEntity> getStaff() {
        return staff;
    }

    public void setStaff(List<StaffEntity> staff) {
        this.staff = staff;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }
    
}
