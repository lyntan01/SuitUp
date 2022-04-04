/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.SupportTicketEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author lyntan
 */
@Named(value = "viewSupportTicketManagedBean")
@ViewScoped
public class ViewSupportTicketManagedBean implements Serializable {

    private SupportTicketEntity supportTicketEntityToView;

    public ViewSupportTicketManagedBean() {
    }

    public SupportTicketEntity getSupportTicketEntityToView() {
        return supportTicketEntityToView;
    }

    public void setSupportTicketEntityToView(SupportTicketEntity supportTicketEntityToView) {
        this.supportTicketEntityToView = supportTicketEntityToView;
    }
    
}
