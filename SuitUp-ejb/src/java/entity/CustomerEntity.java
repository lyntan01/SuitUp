/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import util.security.CryptographicHelper;

/**
 *
 * @author lyntan, keithcharleschan
 */
@Entity

@NamedQueries({
    @NamedQuery(name = "findCustomerByCreditCardId", query = "SELECT cu FROM CustomerEntity cu, IN (cu.creditCards) cc WHERE cc.creditCardId = :inCardId"),
    @NamedQuery(name = "findCustomerByAddressId", query = "SELECT cu FROM CustomerEntity cu, IN (cu.addresses) a WHERE a.addressId = :inAddressId")
})

public class CustomerEntity implements Serializable {

    //-------[Attributes]-------
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstName;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;

    @Column(nullable = false, length = 256, unique = true)
    @NotNull
    @Size(max = 256)
    private String email;

    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    private String password;

    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;

    @Column(nullable = false, length = 10)
    @NotNull
    @Size(max = 10)
    private String contactNumber;

    //-------[Relationship Attributes]-------
    @OneToMany(mappedBy = "customer")
    @JoinColumn(nullable = true)
    private List<AppointmentEntity> appointments;

    @OneToMany
    @JoinColumn(nullable = true)
    private List<CreditCardEntity> creditCards;

    @OneToMany
    @JoinColumn(nullable = true)
    private List<AddressEntity> addresses;

    @OneToMany(mappedBy = "customer")
    @JoinColumn(nullable = true)
    private List<OrderEntity> orders;
    
    @OneToMany(mappedBy = "customer")
    @JoinColumn(nullable = true)
    private List<SupportTicketEntity> supportTickets;

    @OneToOne
    private JacketMeasurementEntity jacketMeasurement;

    @OneToOne
    private PantsMeasurementEntity pantsMeasurement;


    //---------[Methods]---------
    public CustomerEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.appointments = new ArrayList<>();
        this.creditCards = new ArrayList<>();
        this.addresses = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.supportTickets = new ArrayList<>();
    }

    public CustomerEntity(String firstName, String lastName, String email, String password, String contactNumber) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactNumber = contactNumber;
        setPassword(password);
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        } else {
            this.password = null;
        }
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }

    public List<CreditCardEntity> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardEntity> creditCards) {
        this.creditCards = creditCards;
    }

    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public JacketMeasurementEntity getJacketMeasurement() {
        return jacketMeasurement;
    }

    public void setJacketMeasurement(JacketMeasurementEntity jacketMeasurement) {
        this.jacketMeasurement = jacketMeasurement;
    }

    public PantsMeasurementEntity getPantsMeasurement() {
        return pantsMeasurement;
    }

    public void setPantsMeasurement(PantsMeasurementEntity pantsMeasurement) {
        this.pantsMeasurement = pantsMeasurement;
    }

    public List<SupportTicketEntity> getSupportTickets() {
        return supportTickets;
    }

    public void setSupportTickets(List<SupportTicketEntity> supportTickets) {
        this.supportTickets = supportTickets;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CustomerEntity{" + "customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", contactNumber=" + contactNumber + ", appointments=" + appointments + ", creditCards=" + creditCards + ", addresses=" + addresses + ", orders=" + orders + ", jacketMeasurement=" + jacketMeasurement + ", pantsMeasurement=" + pantsMeasurement + ", supportTickets=" + supportTickets + '}';
    }

}
