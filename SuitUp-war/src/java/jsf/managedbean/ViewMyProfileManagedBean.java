/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.StaffSessionBeanLocal;
import entity.StaffEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.ChangePasswordException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateStaffException;

/**
 *
 * @author lyntan
 */
@Named(value = "viewMyProfileManagedBean")
@ViewScoped
public class ViewMyProfileManagedBean implements Serializable {

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;
    
    private StaffEntity staff;
    
    // Change Password
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

    public ViewMyProfileManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        staff = (StaffEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStaffEntity");
    }

    public void editProfile(ActionEvent event) {
        try {
            staffSessionBeanLocal.updateStaff(staff);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentStaffEntity", staff);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile updated successfully", null));
        }
        catch(StaffNotFoundException | UpdateStaffException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating profile: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }

    }
    
    public void changePassword(ActionEvent event) {
        
        try {
            
            System.out.println("old = " + oldPassword);
            System.out.println("new = " + newPassword);
            System.out.println("confirm = " + confirmNewPassword);
            
            if (!newPassword.equals(confirmNewPassword)) {
                throw new ChangePasswordException("New Password and Confirm New Password do not match.");
            }
            
            staff = staffSessionBeanLocal.staffChangePassword(staff.getUsername(), oldPassword, newPassword);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentStaffEntity", staff);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully", null));
            
        } catch (ChangePasswordException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            oldPassword = "";
            newPassword = "";
            confirmNewPassword = "";
        }
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

}
