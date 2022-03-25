/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.AppointmentEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author keithcharleschan
 */
@Named(value = "viewAppointmentManagedBean")
@ViewScoped
public class ViewAppointmentManagedBean implements Serializable {

     private AppointmentEntity appointmentEntityToView;

    public ViewAppointmentManagedBean() {
    }

    public AppointmentEntity getAppointmentEntityToView() {
        return appointmentEntityToView;
    }

    public void setAppointmentEntityToView(AppointmentEntity appointmentEntityToView) {
        this.appointmentEntityToView = appointmentEntityToView;
    }
    
}
