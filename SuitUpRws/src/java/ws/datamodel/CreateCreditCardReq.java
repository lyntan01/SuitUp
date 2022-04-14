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
public class CreateCreditCardReq {

    private String email;
    private String password;
    private CreditCardEntity newCreditCard;

    public CreateCreditCardReq() {
    }

    public CreateCreditCardReq(String email, String password, CreditCardEntity newCreditCard) {
        this.email = email;
        this.password = password;
        this.newCreditCard = newCreditCard;
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

    public CreditCardEntity getNewCreditCard() {
        return newCreditCard;
    }

    public void setNewCreditCard(CreditCardEntity newCreditCard) {
        this.newCreditCard = newCreditCard;
    }

}
