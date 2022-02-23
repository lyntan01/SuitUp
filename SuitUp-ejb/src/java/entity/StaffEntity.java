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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.AccessRightEnum;
import util.security.CryptographicHelper;

/**
 *
 * @author lyntan
 */
@Entity
public class StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private AccessRightEnum accessRightEnum;
    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(min = 4, max = 32)
    private String username;
    // Updated in v4.5 to use CHAR instead of VARCHAR
    // @Column(nullable = false, length = 32)
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    // The following bean validation constraint is not applicable since we are only storing the password hashsum which is always 128 bit represented as 32 characters (16 hexadecimal digits)
    // @Size(min = 8, max = 32)
    private String password;
    // Newly added in v4.5
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;
    
    @OneToMany
    @JoinColumn(nullable = true)
    private List<ManufacturingIssueEntity> manufacturingIssues;

    public StaffEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.manufacturingIssues = new ArrayList<>();
    }

    public StaffEntity(String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        setPassword(password);
    }
    
    
    

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the staffId fields are not set
        if (!(object instanceof StaffEntity)) {
            return false;
        }
        StaffEntity other = (StaffEntity) object;
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StaffEntity[ id=" + staffId + " ]";
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

    public AccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    public void setAccessRightEnum(AccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)
    {
        if(password != null)
        {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        }
        else
        {
            this.password = null;
        }
    }

    public List<ManufacturingIssueEntity> getManufacturingIssues() {
        return manufacturingIssues;
    }

    public void setManufacturingIssues(List<ManufacturingIssueEntity> manufacturingIssues) {
        this.manufacturingIssues = manufacturingIssues;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
}
