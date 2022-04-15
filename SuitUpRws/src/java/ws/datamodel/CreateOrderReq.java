package ws.datamodel;

import entity.OrderEntity;

/**
 *
 * @author lyntan
 */
public class CreateOrderReq {
    
    private String email;
    private String password;
    private OrderEntity order;
    private Long addressId;
    
    public CreateOrderReq() {
    }

    public CreateOrderReq(String email, String password, OrderEntity order, Long addressId) {
        this.email = email;
        this.password = password;
        this.order = order;
        this.addressId = addressId;
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

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    
    
}
