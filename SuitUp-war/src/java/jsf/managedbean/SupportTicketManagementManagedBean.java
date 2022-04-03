/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SupportTicketSessionBeanLocal;
import entity.SupportTicketEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.exception.SupportTicketNotFoundException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Named(value = "supportTicketManagementManagedBean")
@ViewScoped
public class SupportTicketManagementManagedBean implements Serializable {

    @EJB
    private SupportTicketSessionBeanLocal supportTicketSessionBeanLocal;

    @Inject
    private ViewSupportTicketManagedBean viewSupportTicketManagedBean;

    private List<SupportTicketEntity> supportTicketEntities;
    private List<SupportTicketEntity> filteredSupportTicketEntities;

    private SupportTicketEntity selectedSupportTicketEntityToUpdate;

    public SupportTicketManagementManagedBean() {
        supportTicketEntities = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        supportTicketEntities = supportTicketSessionBeanLocal.retrieveAllTickets();
    }

    public void doReplySupportTicket(ActionEvent event) throws Exception {
        selectedSupportTicketEntityToUpdate = (SupportTicketEntity) event.getComponent().getAttributes().get("supportTicketEntityToReply");        
    }
    
    public void replySupportTicket(ActionEvent event) {
        try {

            supportTicketSessionBeanLocal.addTicketReply(selectedSupportTicketEntityToUpdate.getTicketId(), selectedSupportTicketEntityToUpdate.getStaffReply());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Support ticket reply updated successfully", null));
        } catch (SupportTicketNotFoundException | UpdateEntityException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating support ticket reply: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public ViewSupportTicketManagedBean getViewSupportTicketManagedBean() {
        return viewSupportTicketManagedBean;
    }

    public void setViewSupportTicketManagedBean(ViewSupportTicketManagedBean viewSupportTicketManagedBean) {
        this.viewSupportTicketManagedBean = viewSupportTicketManagedBean;
    }

    public List<SupportTicketEntity> getSupportTicketEntities() {
        return supportTicketEntities;
    }

    public void setSupportTicketEntities(List<SupportTicketEntity> supportTicketEntities) {
        this.supportTicketEntities = supportTicketEntities;
    }

    public List<SupportTicketEntity> getFilteredSupportTicketEntities() {
        return filteredSupportTicketEntities;
    }

    public void setFilteredSupportTicketEntities(List<SupportTicketEntity> filteredSupportTicketEntities) {
        this.filteredSupportTicketEntities = filteredSupportTicketEntities;
    }

    public SupportTicketEntity getSelectedSupportTicketEntityToUpdate() {
        return selectedSupportTicketEntityToUpdate;
    }

    public void setSelectedSupportTicketEntityToUpdate(SupportTicketEntity selectedSupportTicketEntityToUpdate) {
        this.selectedSupportTicketEntityToUpdate = selectedSupportTicketEntityToUpdate;
    }

}
