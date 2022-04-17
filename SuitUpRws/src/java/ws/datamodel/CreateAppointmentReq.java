/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.StoreEntity;

/**
 *
 * @author lyntan
 */
public class CreateAppointmentReq {
    
    private CustomerEntity customer;
    private AppointmentEntity appointment;
    private StoreEntity store;
    private String password;

    public CreateAppointmentReq(CustomerEntity customer, AppointmentEntity appointment, StoreEntity store, String password) {
        this.customer = customer;
        this.appointment = appointment;
        this.store = store;
        this.password = password;
    }

    public CreateAppointmentReq() {
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AppointmentEntity getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentEntity appointment) {
        this.appointment = appointment;
    }

    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
