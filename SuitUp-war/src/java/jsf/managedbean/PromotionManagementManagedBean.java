/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PromotionSessionBeanLocal;
import ejb.session.stateless.StoreSessionBeanLocal;
import entity.AbsolutePromotionEntity;
import entity.PercentagePromotionEntity;
import entity.PromotionEntity;
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
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.PromotionCodeExistException;
import util.exception.PromotionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Named(value = "promotionManagementManagedBean")
@ViewScoped
public class PromotionManagementManagedBean implements Serializable {

    @EJB
    private StoreSessionBeanLocal storeSessionBeanLocal;

    @EJB
    private PromotionSessionBeanLocal promotionSessionBeanLocal;

    private List<PromotionEntity> promotionEntities;
    private List<PromotionEntity> filteredPromotionEntities;

    private AbsolutePromotionEntity newAbsolutePromotionEntity;
    private PercentagePromotionEntity newPercentagePromotionEntity;

    private PromotionEntity selectedPromotionEntityToUpdate; //  can only update the date

    private PromotionEntity promotionToDelete;
    
    public PromotionManagementManagedBean() {
        promotionEntities = new ArrayList<>();
        newAbsolutePromotionEntity = new AbsolutePromotionEntity();
        newPercentagePromotionEntity = new PercentagePromotionEntity();
    }

    @PostConstruct
    public void postConstruct() {
        promotionEntities = promotionSessionBeanLocal.retrieveAllPromotions();
    }
    
    public void createNewAbsolutePromotion(ActionEvent event) {
        try {
                        
            Long promotionId = promotionSessionBeanLocal.createNewPromotion(newAbsolutePromotionEntity);
            AbsolutePromotionEntity newAbsolutePromotion = (AbsolutePromotionEntity) promotionSessionBeanLocal.retrievePromotionByPromotionId(promotionId);
            promotionEntities.add(newAbsolutePromotion);

            if (filteredPromotionEntities != null) {
                filteredPromotionEntities.add(newAbsolutePromotion);
            }

            newAbsolutePromotion = new AbsolutePromotionEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New absolute promotion created successfully (Promotion ID: " + promotionId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException | PromotionNotFoundException | PromotionCodeExistException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred when creating new promotion: " + ex.getMessage(), null));
        }

    }
    
    public void createNewPercentagePromotion(ActionEvent event) {
        try {
                        
            Long promotionId = promotionSessionBeanLocal.createNewPromotion(newPercentagePromotionEntity);
            PercentagePromotionEntity newPercentagePromotion = (PercentagePromotionEntity) promotionSessionBeanLocal.retrievePromotionByPromotionId(promotionId);
            promotionEntities.add(newPercentagePromotion);

            if (filteredPromotionEntities != null) {
                filteredPromotionEntities.add(newPercentagePromotion);
            }

            newPercentagePromotion = new PercentagePromotionEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New percentage promotion created successfully (Promotion ID: " + promotionId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException | PromotionNotFoundException | PromotionCodeExistException ex) {
            System.out.println("\n\n\n\n\n" + ex.toString() + "\n\n\n\n");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred when creating new promotion: " + ex.getMessage(), null));
        }

    }

    public void doUpdatePromotion(ActionEvent event) {
        selectedPromotionEntityToUpdate = (PromotionEntity) event.getComponent().getAttributes().get("promotionEntityToUpdate");
    }

    public void updatePromotion(ActionEvent event) {
        
        try {
            
            promotionSessionBeanLocal.updatePromotion(selectedPromotionEntityToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Promotion updated successfully", null));
        } catch (PromotionNotFoundException | UpdateEntityException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating promotion: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deletePromotion(ActionEvent event) {
        try {
            promotionToDelete = (PromotionEntity) event.getComponent().getAttributes().get("promotionToDelete");
            promotionSessionBeanLocal.deletePromotion(promotionToDelete.getPromotionId());

            promotionEntities.remove(promotionToDelete);

            if (filteredPromotionEntities != null) {
                filteredPromotionEntities.remove(promotionToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Promotion " + promotionToDelete.getPromotionCode() + " deleted successfully", null));
        } catch (PromotionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting promotion: " + ex.getMessage(), null));
        } catch (DeleteEntityException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting promotion: " + ex.getMessage() + "\nTo disable this promotion, please update its expiry date to an earlier date.", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public List<PromotionEntity> getPromotionEntities() {
        return promotionEntities;
    }

    public void setPromotionEntities(List<PromotionEntity> promotionEntities) {
        this.promotionEntities = promotionEntities;
    }

    public List<PromotionEntity> getFilteredPromotionEntities() {
        return filteredPromotionEntities;
    }

    public void setFilteredPromotionEntities(List<PromotionEntity> filteredPromotionEntities) {
        this.filteredPromotionEntities = filteredPromotionEntities;
    }

    public PromotionEntity getSelectedPromotionEntityToUpdate() {
        return selectedPromotionEntityToUpdate;
    }

    public void setSelectedPromotionEntityToUpdate(PromotionEntity selectedPromotionEntityToUpdate) {
        this.selectedPromotionEntityToUpdate = selectedPromotionEntityToUpdate;
    }

    public PromotionEntity getPromotionToDelete() {
        return promotionToDelete;
    }

    public void setPromotionToDelete(PromotionEntity promotionToDelete) {
        this.promotionToDelete = promotionToDelete;
    }

    public AbsolutePromotionEntity getNewAbsolutePromotionEntity() {
        return newAbsolutePromotionEntity;
    }

    public void setNewAbsolutePromotionEntity(AbsolutePromotionEntity newAbsolutePromotionEntity) {
        this.newAbsolutePromotionEntity = newAbsolutePromotionEntity;
    }

    public PercentagePromotionEntity getNewPercentagePromotionEntity() {
        return newPercentagePromotionEntity;
    }

    public void setNewPercentagePromotionEntity(PercentagePromotionEntity newPercentagePromotionEntity) {
        this.newPercentagePromotionEntity = newPercentagePromotionEntity;
    }
    
}
