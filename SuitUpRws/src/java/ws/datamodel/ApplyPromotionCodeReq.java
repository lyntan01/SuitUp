package ws.datamodel;

import entity.OrderEntity;

/**
 *
 * @author lyntan
 */
public class ApplyPromotionCodeReq {
    private String email;
    private String password;
    private OrderEntity order;
    private String promotionCode;

    public ApplyPromotionCodeReq() {
    }

    public ApplyPromotionCodeReq(String email, String password, OrderEntity order, String promotionCode) {
        this.email = email;
        this.password = password;
        this.order = order;
        this.promotionCode = promotionCode;
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

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    
}
