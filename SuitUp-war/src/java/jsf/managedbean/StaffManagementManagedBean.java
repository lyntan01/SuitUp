/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.StoreSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.StoreEntity;
import entity.StaffEntity;
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
import util.enumeration.AccessRightEnum;
import util.exception.StoreNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateStaffException;

/**
 *
 * @author lyntan
 */
@Named(value = "staffManagementManagedBean")
@ViewScoped
public class StaffManagementManagedBean implements Serializable {

    @EJB
    private StoreSessionBeanLocal storeSessionBeanLocal;

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;

    private List<StoreEntity> stores;
    private List<StaffEntity> staffEntities;
    private List<StaffEntity> filteredStaffEntities;

    private StaffEntity newStaffEntity;
    private Long newStoreId;
    private String newAccessRightEnumString;

    private StaffEntity selectedStaffEntityToUpdate;
    private Long storeIdUpdate;
    private String accessRightEnumStringUpdate;

    private StaffEntity staffToDelete;

    public StaffManagementManagedBean() {
        staffEntities = new ArrayList<>();
        stores = new ArrayList<>();
        newStaffEntity = new StaffEntity();
    }

    @PostConstruct
    public void postConstruct() {
        staffEntities = staffSessionBeanLocal.retrieveAllStaffs();
        stores = storeSessionBeanLocal.retrieveAllStores();
    }

    public void createNewStaff(ActionEvent event) {
        try {
                        
            newStaffEntity.setAccessRightEnum(AccessRightEnum.valueOf(newAccessRightEnumString));
            
            StoreEntity storeEntity = newStoreId == null ? null : storeSessionBeanLocal.retrieveStoreByStoreId(newStoreId);
            newStaffEntity.setStore(storeEntity);
            
            Long staffId = staffSessionBeanLocal.createNewStaff(newStaffEntity);
            StaffEntity newStaff = staffSessionBeanLocal.retrieveStaffByStaffId(staffId);
            staffEntities.add(newStaff);

            if (filteredStaffEntities != null) {
                filteredStaffEntities.add(newStaff);
            }

            newStaffEntity = new StaffEntity();
            newStoreId = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New staff created successfully (Staff ID: " + staffId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException | StaffNotFoundException | StaffUsernameExistException | StoreNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred when creating new staff: " + ex.getMessage(), null));
        }

    }

    public void doUpdateStaff(ActionEvent event) {
        selectedStaffEntityToUpdate = (StaffEntity) event.getComponent().getAttributes().get("staffEntityToUpdate");
        storeIdUpdate = selectedStaffEntityToUpdate.getStore().getStoreId();
        accessRightEnumStringUpdate = selectedStaffEntityToUpdate.getAccessRightEnum().name();
    }

    public void updateStaff(ActionEvent event) {
        
        if (storeIdUpdate == 0) {
            storeIdUpdate = null;
        }
        
        try {
            
            StoreEntity updatedStoreEntity = storeIdUpdate == null ? null : storeSessionBeanLocal.retrieveStoreByStoreId(storeIdUpdate);
            selectedStaffEntityToUpdate.setStore(updatedStoreEntity);
            selectedStaffEntityToUpdate.setAccessRightEnum(AccessRightEnum.valueOf(accessRightEnumStringUpdate));
            staffSessionBeanLocal.updateStaff(selectedStaffEntityToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Staff updated successfully", null));
        } catch (StaffNotFoundException | UpdateStaffException | InputDataValidationException | StoreNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating staff: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteStaff(ActionEvent event) {
        try {
            staffToDelete = (StaffEntity) event.getComponent().getAttributes().get("staffToDelete");
            staffSessionBeanLocal.deleteStaff(staffToDelete.getStaffId());

            staffEntities.remove(staffToDelete);

            if (filteredStaffEntities != null) {
                filteredStaffEntities.remove(staffToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, staffToDelete.getFirstName() + " " + staffToDelete.getLastName() + " deleted successfully", null));
        } catch (StaffNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting staff: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<StaffEntity> getStaffEntities() {
        return staffEntities;
    }

    public void setStaffEntities(List<StaffEntity> staffEntities) {
        this.staffEntities = staffEntities;
    }

    public List<StaffEntity> getFilteredStaffEntities() {
        return filteredStaffEntities;
    }

    public void setFilteredStaffEntities(List<StaffEntity> filteredStaffEntities) {
        this.filteredStaffEntities = filteredStaffEntities;
    }

    public StaffEntity getSelectedStaffEntityToUpdate() {
        return selectedStaffEntityToUpdate;
    }

    public void setSelectedStaffEntityToUpdate(StaffEntity selectedStaffEntityToUpdate) {
        this.selectedStaffEntityToUpdate = selectedStaffEntityToUpdate;
    }

    public StaffEntity getStaffToDelete() {
        return staffToDelete;
    }

    public void setStaffToDelete(StaffEntity staffToDelete) {
        this.staffToDelete = staffToDelete;
    }

    public StaffEntity getNewStaffEntity() {
        return newStaffEntity;
    }

    public void setNewStaffEntity(StaffEntity newStaffEntity) {
        this.newStaffEntity = newStaffEntity;
    }

    public List<StoreEntity> getStores() {
        return stores;
    }

    public void setStores(List<StoreEntity> stores) {
        this.stores = stores;
    }

    public Long getNewStoreId() {
        return newStoreId;
    }

    public void setNewStoreId(Long newStoreId) {
        this.newStoreId = newStoreId;
    }

    public Long getStoreIdUpdate() {
        return storeIdUpdate;
    }

    public void setStoreIdUpdate(Long storeIdUpdate) {
        this.storeIdUpdate = storeIdUpdate;
    }

    public String getNewAccessRightEnumString() {
        return newAccessRightEnumString;
    }

    public void setNewAccessRightEnumString(String newAccessRightEnumString) {
        this.newAccessRightEnumString = newAccessRightEnumString;
    }

    public String getAccessRightEnumStringUpdate() {
        return accessRightEnumStringUpdate;
    }

    public void setAccessRightEnumStringUpdate(String accessRightEnumStringUpdate) {
        this.accessRightEnumStringUpdate = accessRightEnumStringUpdate;
    }
    
}
