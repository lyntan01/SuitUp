/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws.datamodel;

import entity.CreditCardEntity;

/**
 *
 * @author keithcharleschan
 */
public class UpdateCreditCardReq {
    
   private String email;
   private String password;
   private CreditCardEntity creditCardEntity;
   private Long expiryDate;

    public UpdateCreditCardReq() {
    }

    public UpdateCreditCardReq(String email, String password, CreditCardEntity newCreditCard, Long expiryDate) {
        this.email = email;
        this.password = password;
        this.creditCardEntity = newCreditCard;
        this.expiryDate = expiryDate;
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

    public CreditCardEntity getCreditCardEntity() {
        return creditCardEntity;
    }

    public void setCreditCardEntity(CreditCardEntity creditCardEntity) {
        this.creditCardEntity = creditCardEntity;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }
   
   
    

}
