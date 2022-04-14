/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.JacketMeasurementSessionBeanLocal;
import ejb.session.stateless.PantsMeasurementSessionBeanLocal;
import entity.CustomerEntity;
import entity.JacketMeasurementEntity;
import entity.PantsMeasurementEntity;
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
    private PantsMeasurementSessionBeanLocal pantsMeasurementSessionBean;

    @EJB
    private JacketMeasurementSessionBeanLocal jacketMeasurementSessionBean;

    
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @Inject
    private ViewCustomerManagedBean viewCustomerManagedBean;
    
    private List<CustomerEntity> customerEntities;
    private List<CustomerEntity> filteredCustomerEntities;
    
    private CustomerEntity selectedCustomerEntityToUpdate;
    private CustomerEntity selectedCustomerToUpdateMeasurement;
    private JacketMeasurementEntity jacketMeasurement;
    private PantsMeasurementEntity pantsMeasurement;
    
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
    
    public void doUpdateJacketMeasurement(ActionEvent event)
    {
        selectedCustomerToUpdateMeasurement = (CustomerEntity) event.getComponent().getAttributes().get("selectedCustomerToUpdateMeasurement");
        if(selectedCustomerToUpdateMeasurement.getJacketMeasurement()==null) {
            jacketMeasurement = new JacketMeasurementEntity();
        } else {
            jacketMeasurement = selectedCustomerToUpdateMeasurement.getJacketMeasurement();
        }
    }
    
    public void updateJacketMeasurement(ActionEvent event) {
        try {
            if (selectedCustomerToUpdateMeasurement.getJacketMeasurement() != null) {
                jacketMeasurementSessionBean.updateJacketMeasurement(jacketMeasurement);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Measurements updated successfully", null));

            } else {
                jacketMeasurementSessionBean.createNewJacketMeasurement(jacketMeasurement, selectedCustomerToUpdateMeasurement.getCustomerId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Measurements created successfully", null));

            }
        } catch (InputDataValidationException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating/creating measurement: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdatePantsMeasurement(ActionEvent event)
    {
        selectedCustomerToUpdateMeasurement = (CustomerEntity) event.getComponent().getAttributes().get("selectedCustomerToUpdateMeasurement");
        if(selectedCustomerToUpdateMeasurement.getPantsMeasurement()==null) {
            pantsMeasurement = new PantsMeasurementEntity();
        } else {
            pantsMeasurement = selectedCustomerToUpdateMeasurement.getPantsMeasurement();
        }
    }
    
    public void updatePantsMeasurement(ActionEvent event) {
        try
        {
            if(selectedCustomerToUpdateMeasurement.getPantsMeasurement()!=null) {
                pantsMeasurementSessionBean.updatePantsMeasurement(pantsMeasurement);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Measurements updated successfully", null));

            } else {
                pantsMeasurementSessionBean.createNewPantsMeasurement(pantsMeasurement, selectedCustomerToUpdateMeasurement.getCustomerId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Measurements created successfully", null));
            }

        }
        catch(InputDataValidationException | CustomerNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating/creating measurement: " + ex.getMessage(), null));
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

    public CustomerEntity getSelectedCustomerToUpdateMeasurement() {
        return selectedCustomerToUpdateMeasurement;
    }

    public void setSelectedCustomerToUpdateMeasurement(CustomerEntity selectedCustomerToUpdateMeasurement) {
        this.selectedCustomerToUpdateMeasurement = selectedCustomerToUpdateMeasurement;
    }

    public JacketMeasurementEntity getJacketMeasurement() {
        return jacketMeasurement;
    }

    public void setJacketMeasurement(JacketMeasurementEntity jacketMeasurement) {
        this.jacketMeasurement = jacketMeasurement;
    }

    public PantsMeasurementEntity getPantsMeasurement() {
        return pantsMeasurement;
    }

    public void setPantsMeasurement(PantsMeasurementEntity pantsMeasurement) {
        this.pantsMeasurement = pantsMeasurement;
    }

    
    
}
