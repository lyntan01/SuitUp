/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomizedPantsSessionBeanLocal;
import ejb.session.stateless.FabricSessionBeanLocal;
import ejb.session.stateless.PantsCuttingSessionBeanLocal;
import entity.CustomizedPantsEntity;
import entity.FabricEntity;
import entity.PantsCuttingEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.exception.CustomizationNotFoundException;

/**
 *
 * @author meganyee
 */
@Named(value = "customizedPantsManagedBean")
@ViewScoped
public class CustomizedPantsManagedBean implements Serializable {

    @EJB
    private PantsCuttingSessionBeanLocal pantsCuttingSessionBeanLocal;

    @EJB
    private CustomizedPantsSessionBeanLocal customizedPantsSessionBeanLocal;

    @EJB
    private FabricSessionBeanLocal fabricSessionBeanLocal;
    
    @Inject
    private CreateOrderManagedBean createOrderManagedBean;
    
    private List<FabricEntity> fabrics;
    private List<PantsCuttingEntity> pantsCutting;
    
    private CustomizedPantsEntity newCustomizedPants;
    
    private Long fabricId;
    private Long pantsCuttingId;

    public CustomizedPantsManagedBean() {
        fabrics = new ArrayList<>();
        pantsCutting = new ArrayList<>();
        newCustomizedPants = new CustomizedPantsEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        fabrics = fabricSessionBeanLocal.retrieveAllFabrics();
        pantsCutting = pantsCuttingSessionBeanLocal.retrieveAllPantsCutting();
    }
    
    public void addNewCustomizedPants(ActionEvent event) {
        try {
            System.out.println("fabricId == null: " + fabricId == null);
            System.out.println("pantsCuttingId == null: " + pantsCuttingId == null);
            
            FabricEntity frabic = fabricSessionBeanLocal.retrieveFabricById(fabricId);
            PantsCuttingEntity pantsCutting = pantsCuttingSessionBeanLocal.retrievePantsCuttingById(pantsCuttingId);
            
            BigDecimal totalPrice = newCustomizedPants.getBasePrice()
                    .add(fabricSessionBeanLocal.retrieveFabricById(fabricId).getAdditionalPrice()
                            .add(pantsCuttingSessionBeanLocal.retrievePantsCuttingById(pantsCuttingId).getAdditionalPrice()));
            
            newCustomizedPants.setTotalPrice(totalPrice);
            newCustomizedPants.setPantsMeasurement(createOrderManagedBean.getCurrentCustomer().getPantsMeasurement());
            
            newCustomizedPants.setFabric(frabic);
            newCustomizedPants.setPantsCutting(pantsCutting);
            
            createOrderManagedBean.addItem(newCustomizedPants, 1);
            
//            Long productId = customizedPantsSessionBeanLocal.createNewCustomizedPants(newCustomizedPants, fabricId, pantsCuttingId, newCustomizedPants.getPantsMeasurement().getPantsMeasurementId());
            
            newCustomizedPants = new CustomizedPantsEntity();
            fabricId = null;
            pantsCuttingId = null;
            
            
//            try {
//                createOrderManagedBean.addItem(customizedPantsSessionBeanLocal.retrieveCustomizedPantsById(productId), 1);
//            } catch (CustomizedProductNotFoundException ex) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred when retrieving the new pants: " + ex.getMessage(), null));
//            }
//            
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Customized Pants created successfully (Pants ID: " + productId + ")", null));
            
            
            
//        } catch (CustomizedProductIdExistsException | PantsMeasurementNotFoundException | CustomizationNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred when creating new pants: " + ex.getMessage(), null));
//        }
        } catch (CustomizationNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred when creating retrieving customization: " + ex.getMessage(), null));
        }
    }
    
    public List<FabricEntity> getFabrics() {
        return fabrics;
    }

    public void setFabrics(List<FabricEntity> fabrics) {
        this.fabrics = fabrics;
    }

    public List<PantsCuttingEntity> getPantsCutting() {
        return pantsCutting;
    }

    public void setPantsCutting(List<PantsCuttingEntity> pantsCutting) {
        this.pantsCutting = pantsCutting;
    }

    public CustomizedPantsEntity getNewCustomizedPants() {
        return newCustomizedPants;
    }

    public void setNewCustomizedPants(CustomizedPantsEntity newCustomizedPants) {
        this.newCustomizedPants = newCustomizedPants;
    }

    public Long getFabricId() {
        return fabricId;
    }

    public void setFabricId(Long fabricId) {
        this.fabricId = fabricId;
    }

    public Long getPantsCuttingId() {
        return pantsCuttingId;
    }

    public void setPantsCuttingId(Long pantsCuttingId) {
        this.pantsCuttingId = pantsCuttingId;
    }
    
}
