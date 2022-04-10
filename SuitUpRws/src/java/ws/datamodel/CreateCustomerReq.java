/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws.datamodel;

import entity.CustomerEntity;

/**
 *
 * @author keithcharleschan
 */
public class CreateCustomerReq {
    
    private CustomerEntity newCustomer;

    public CreateCustomerReq() {
    }

    public CreateCustomerReq(CustomerEntity newCustomer) {
        this.newCustomer = newCustomer;
    }

    public CustomerEntity getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(CustomerEntity newCustomer) {
        this.newCustomer = newCustomer;
    }
    
}
