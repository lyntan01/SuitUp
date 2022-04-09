package ws.datamodel;

import entity.AddressEntity;

/**
 *
 * @author lyntan
 */
public class UpdateAddressReq {
    
    private String email;
    private String password;
    private AddressEntity address;
    
    public UpdateAddressReq(String email, String password, AddressEntity address) {
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public UpdateAddressReq() {
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
        this.password = password;
    }

    public AddressEntity getAddressEntity() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }
    
    
    
}
