/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.StaffEntity;
import java.io.IOException;
import javafx.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author lyntan
 */
@Named(value = "viewMyProfileManagedBean")
@RequestScoped
public class ViewMyProfileManagedBean {

    private StaffEntity staff;
    
    public ViewMyProfileManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {   
        staff = (StaffEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStaffEntity");
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }
    
}
