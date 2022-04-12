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
public class UpdateProfileReq {

    private CustomerEntity currentCustomer;
  
    public UpdateProfileReq() {
    }

    public UpdateProfileReq(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

 
    

   

}
