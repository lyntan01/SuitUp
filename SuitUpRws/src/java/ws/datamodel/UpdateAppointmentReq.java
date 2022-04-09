/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.AppointmentEntity;

/**
 *
 * @author lyntan
 */
public class UpdateAppointmentReq {
    
    private String email;
    private String password;
    private AppointmentEntity appointment;
    private Long storeId;

    public UpdateAppointmentReq(String email, String password, AppointmentEntity appointment, Long storeId) {
        this.email = email;
        this.password = password;
        this.appointment = appointment;
        this.storeId = storeId;
    }

    public UpdateAppointmentReq() {
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

    public AppointmentEntity getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentEntity appointment) {
        this.appointment = appointment;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
