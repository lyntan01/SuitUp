/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AppointmentSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.AppointmentEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

/**
 *
 * @author keithcharleschan
 */
@Named(value = "appointmentManagementManagedBean")
@ViewScoped
public class AppointmentManagementManagedBean implements Serializable {

    @EJB
    private AppointmentSessionBeanLocal appointmentSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @Inject
    private ViewAppointmentManagedBean viewAppointmentManagedBean;

    private List<AppointmentEntity> appointmentEntities;
    private List<AppointmentEntity> filteredAppointmentEntities;

    private AppointmentEntity selectedAppointmentEntityToUpdate;
    private CustomerEntity customerOfAppointmentToPayFor;
    private List<CreditCardEntity> creditCards;

    public AppointmentManagementManagedBean() {
        appointmentEntities = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        appointmentEntities = appointmentSessionBeanLocal.retrieveAllAppointments();
    }

    public void doUpdateAppointment(ActionEvent event) {
        selectedAppointmentEntityToUpdate = (AppointmentEntity) event.getComponent().getAttributes().get("appointmentEntityToUpdate");
    }

    public void doPaymentForAppointment(ActionEvent event) throws Exception {
        selectedAppointmentEntityToUpdate = (AppointmentEntity) event.getComponent().getAttributes().get("appointmentEntityToPayFor");
        Long customerId = selectedAppointmentEntityToUpdate.getCustomer().getCustomerId();
        customerOfAppointmentToPayFor = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        creditCards = customerOfAppointmentToPayFor.getCreditCards();
        
    }

    public ViewAppointmentManagedBean getViewAppointmentManagedBean() {
        return viewAppointmentManagedBean;
    }

    public void setViewAppointmentManagedBean(ViewAppointmentManagedBean viewAppointmentManagedBean) {
        this.viewAppointmentManagedBean = viewAppointmentManagedBean;
    }

    public List<AppointmentEntity> getAppointmentEntities() {
        return appointmentEntities;
    }

    public void setAppointmentEntities(List<AppointmentEntity> appointmentEntities) {
        this.appointmentEntities = appointmentEntities;
    }

    public List<AppointmentEntity> getFilteredAppointmentEntities() {
        return filteredAppointmentEntities;
    }

    public void setFilteredAppointmentEntities(List<AppointmentEntity> filteredAppointmentEntities) {
        this.filteredAppointmentEntities = filteredAppointmentEntities;
    }

    public AppointmentEntity getSelectedAppointmentEntityToUpdate() {
        return selectedAppointmentEntityToUpdate;
    }

    public void setSelectedAppointmentEntityToUpdate(AppointmentEntity selectedAppointmentEntityToUpdate) {
        this.selectedAppointmentEntityToUpdate = selectedAppointmentEntityToUpdate;
    }

    public CustomerEntity getCustomerOfAppointmentToPayFor() {
        return customerOfAppointmentToPayFor;
    }

    public void setCustomerOfAppointmentToPayFor(CustomerEntity customerOfAppointmentToPayFor) {
        this.customerOfAppointmentToPayFor = customerOfAppointmentToPayFor;
    }

    public List<CreditCardEntity> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardEntity> creditCards) {
        this.creditCards = creditCards;
    }
    
    
    
    

}
