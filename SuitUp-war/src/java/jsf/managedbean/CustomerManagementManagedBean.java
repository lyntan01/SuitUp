/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.CustomerEntity;
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
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author lyntan
 */
@Named(value = "customerManagementManagedBean")
@ViewScoped
public class CustomerManagementManagedBean implements Serializable {

    
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @Inject
    private ViewCustomerManagedBean viewCustomerManagedBean;
    
    private List<CustomerEntity> customerEntities;
    private List<CustomerEntity> filteredCustomerEntities;
    
    private CustomerEntity selectedCustomerEntityToUpdate;
  
    public CustomerManagementManagedBean() {
        customerEntities = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct() {
        customerEntities = customerSessionBeanLocal.retrieveAllCustomers();
    }
    
    public void doUpdateCustomer(ActionEvent event)
    {
        selectedCustomerEntityToUpdate = (CustomerEntity) event.getComponent().getAttributes().get("customerEntityToUpdate");
    }
    
    public void updateCustomer(ActionEvent event) {
        try
        {
            customerSessionBeanLocal.updateCustomer(selectedCustomerEntityToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer updated successfully", null));
        }
        catch(CustomerNotFoundException | UpdateCustomerException | InputDataValidationException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating customer: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public ViewCustomerManagedBean getViewCustomerManagedBean() {
        return viewCustomerManagedBean;
    }

    public void setViewCustomerManagedBean(ViewCustomerManagedBean viewCustomerManagedBean) {
        this.viewCustomerManagedBean = viewCustomerManagedBean;
    }

    public List<CustomerEntity> getCustomerEntities() {
        return customerEntities;
    }

    public void setCustomerEntities(List<CustomerEntity> customerEntities) {
        this.customerEntities = customerEntities;
    }

    public List<CustomerEntity> getFilteredCustomerEntities() {
        return filteredCustomerEntities;
    }

    public void setFilteredCustomerEntities(List<CustomerEntity> filteredCustomerEntities) {
        this.filteredCustomerEntities = filteredCustomerEntities;
    }

    public CustomerEntity getSelectedCustomerEntityToUpdate() {
        return selectedCustomerEntityToUpdate;
    }

    public void setSelectedCustomerEntityToUpdate(CustomerEntity selectedCustomerEntityToUpdate) {
        this.selectedCustomerEntityToUpdate = selectedCustomerEntityToUpdate;
    }
    
    
}
