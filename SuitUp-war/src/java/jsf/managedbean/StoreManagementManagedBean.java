/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AddressSessionBeanLocal;
import ejb.session.stateless.StoreSessionBeanLocal;
import entity.AddressEntity;
import entity.StoreEntity;
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
import util.exception.AddressNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.StoreNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Named(value = "storeManagementManagedBean")
@ViewScoped
public class StoreManagementManagedBean implements Serializable {

    @EJB
    private AddressSessionBeanLocal addressSessionBeanLocal;

    @EJB
    private StoreSessionBeanLocal storeSessionBeanLocal;

    private List<StoreEntity> storeEntities;
    private List<StoreEntity> filteredStoreEntities;

    private StoreEntity newStoreEntity;
    private AddressEntity newAddressEntity;

    private StoreEntity selectedStoreEntityToUpdate;
    private AddressEntity addressEntityToUpdate;

    private StoreEntity storeToDelete;

    public StoreManagementManagedBean() {
        storeEntities = new ArrayList<>();
        newStoreEntity = new StoreEntity();
        newAddressEntity = new AddressEntity();
    }

    @PostConstruct
    public void postConstruct() {
        storeEntities = storeSessionBeanLocal.retrieveAllStores();
    }

    public void createNewStore(ActionEvent event) {
        try {
            
            newAddressEntity.setPhoneNumber(newStoreEntity.getContactNumber());
            
            Long storeId = storeSessionBeanLocal.createNewStore(newStoreEntity);
            addressSessionBeanLocal.createNewStoreAddress(newAddressEntity, storeId);
            StoreEntity newStore = storeSessionBeanLocal.retrieveStoreByStoreId(storeId);
            storeEntities.add(newStore);

            if (filteredStoreEntities != null) {
                filteredStoreEntities.add(newStore);
            }

            newStoreEntity = new StoreEntity();
            newAddressEntity = new AddressEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New store created successfully (Store ID: " + storeId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException | StoreNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred when creating new store: " + ex.getMessage(), null));
        }

    }

    public void doUpdateStore(ActionEvent event) {
        selectedStoreEntityToUpdate = (StoreEntity) event.getComponent().getAttributes().get("storeEntityToUpdate");
        addressEntityToUpdate = selectedStoreEntityToUpdate.getAddress();
    }

    public void updateStore(ActionEvent event) {
        try {
            storeSessionBeanLocal.updateStore(selectedStoreEntityToUpdate);
            addressSessionBeanLocal.updateAddress(addressEntityToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Store updated successfully", null));
        } catch (StoreNotFoundException | UpdateEntityException | InputDataValidationException | AddressNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating store: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteStore(ActionEvent event) {
        try {
            storeToDelete = (StoreEntity) event.getComponent().getAttributes().get("storeToDelete");
            storeSessionBeanLocal.deleteStore(storeToDelete.getStoreId());

            storeEntities.remove(storeToDelete);

            if (filteredStoreEntities != null) {
                filteredStoreEntities.remove(storeToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, storeToDelete.getStoreName() + " Store deleted successfully", null));
        } catch (StoreNotFoundException | DeleteEntityException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting store: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<StoreEntity> getStoreEntities() {
        return storeEntities;
    }

    public void setStoreEntities(List<StoreEntity> storeEntities) {
        this.storeEntities = storeEntities;
    }

    public List<StoreEntity> getFilteredStoreEntities() {
        return filteredStoreEntities;
    }

    public void setFilteredStoreEntities(List<StoreEntity> filteredStoreEntities) {
        this.filteredStoreEntities = filteredStoreEntities;
    }

    public StoreEntity getSelectedStoreEntityToUpdate() {
        return selectedStoreEntityToUpdate;
    }

    public void setSelectedStoreEntityToUpdate(StoreEntity selectedStoreEntityToUpdate) {
        this.selectedStoreEntityToUpdate = selectedStoreEntityToUpdate;
    }

    public AddressEntity getAddressEntityToUpdate() {
        return addressEntityToUpdate;
    }

    public void setAddressEntityToUpdate(AddressEntity addressEntityToUpdate) {
        this.addressEntityToUpdate = addressEntityToUpdate;
    }

    public StoreEntity getStoreToDelete() {
        return storeToDelete;
    }

    public void setStoreToDelete(StoreEntity storeToDelete) {
        this.storeToDelete = storeToDelete;
    }

    public StoreEntity getNewStoreEntity() {
        return newStoreEntity;
    }

    public void setNewStoreEntity(StoreEntity newStoreEntity) {
        this.newStoreEntity = newStoreEntity;
    }

    public AddressEntity getNewAddressEntity() {
        return newAddressEntity;
    }

    public void setNewAddressEntity(AddressEntity newAddressEntity) {
        this.newAddressEntity = newAddressEntity;
    }

}
