/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomizedJacketSessionBeanLocal;
import ejb.session.stateless.FabricSessionBeanLocal;
import ejb.session.stateless.JacketStyleSessionBeanLocal;
import ejb.session.stateless.PocketStyleSessionBeanLocal;
import entity.CustomizedJacketEntity;
import entity.FabricEntity;
import entity.JacketStyleEntity;
import entity.PocketStyleEntity;
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
import util.exception.CustomizedProductIdExistsException;
import util.exception.InputDataValidationException;
import util.exception.JacketMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author meganyee
 */
@Named(value = "customizedJacketManagedBean")
@ViewScoped
public class CustomizedJacketManagedBean implements Serializable {


    @EJB
    private CustomizedJacketSessionBeanLocal customizedJacketSessionBeanLocal;

    @EJB
    private PocketStyleSessionBeanLocal pocketStyleSessionBeanLocal;

    @EJB
    private JacketStyleSessionBeanLocal jacketStyleSessionBeanLocal;

    @EJB
    private FabricSessionBeanLocal fabricSessionBeanLocal;

    @Inject
    private CreateOrderManagedBean createOrderManagedBean;

    private List<FabricEntity> fabrics;
    private List<JacketStyleEntity> jacketStyles;
    private List<PocketStyleEntity> pocketStyles;

    private CustomizedJacketEntity newCustomizedJacket;
    
    private Long innerFabricId;
    private Long outerFabricId;
    private Long jacketStyleId;
    private Long pocketStyleId;

    public CustomizedJacketManagedBean() {
        fabrics = new ArrayList<>();
        jacketStyles = new ArrayList<>();
        pocketStyles = new ArrayList<>();
        newCustomizedJacket = new CustomizedJacketEntity();
    }

    @PostConstruct
    public void postConstruct() {
        fabrics = fabricSessionBeanLocal.retrieveAllFabrics();
        jacketStyles = jacketStyleSessionBeanLocal.retrieveAllJacketStyles();
        pocketStyles = pocketStyleSessionBeanLocal.retrieveAllPocketStyles();
    }

    public void createNewCustomizedJacket(ActionEvent event) {
        try {
           
            BigDecimal totalPrice = newCustomizedJacket.getBasePrice().add(fabricSessionBeanLocal.retrieveFabricById(outerFabricId).getAdditionalPrice()
                    .add(fabricSessionBeanLocal.retrieveFabricById(innerFabricId).getAdditionalPrice()
                            .add(jacketStyleSessionBeanLocal.retrieveJacketStyleById(jacketStyleId).getAdditionalPrice()
                                .add(pocketStyleSessionBeanLocal.retrievePocketStyleById(pocketStyleId).getAdditionalPrice()))));
            
            newCustomizedJacket.setTotalPrice(totalPrice);
            newCustomizedJacket.setJacketMeasurement(createOrderManagedBean.getCurrentCustomer().getJacketMeasurement());
            
            newCustomizedJacket.setInnerFabric(fabricSessionBeanLocal.retrieveFabricById(innerFabricId));
            newCustomizedJacket.setOuterFabric(fabricSessionBeanLocal.retrieveFabricById(outerFabricId));
            newCustomizedJacket.setJacketStyle(jacketStyleSessionBeanLocal.retrieveJacketStyleById(jacketStyleId));
            newCustomizedJacket.setPocketStyle(pocketStyleSessionBeanLocal.retrievePocketStyleById(pocketStyleId));
            
            Long productId = customizedJacketSessionBeanLocal.createNewCustomizedJacket(newCustomizedJacket, pocketStyleId, jacketStyleId, innerFabricId, outerFabricId, newCustomizedJacket.getJacketMeasurement().getJacketMeasurementId());
            
            createOrderManagedBean.addItem(newCustomizedJacket, 1);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Customized Jacket created successfully (Jacket ID: " + productId + ")", null));
            
            newCustomizedJacket = null;
            innerFabricId = null;
            outerFabricId = null;
            pocketStyleId = null;
            jacketStyleId = null;
            
        } catch (CustomizedProductIdExistsException | JacketMeasurementNotFoundException | CustomizationNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occurred when creating new jacket: " + ex.getMessage(), null));
        }
    }

    public List<FabricEntity> getFabrics() {
        return fabrics;
    }

    public void setFabrics(List<FabricEntity> fabrics) {
        this.fabrics = fabrics;
    }

    public List<JacketStyleEntity> getJacketStyles() {
        return jacketStyles;
    }

    public void setJacketStyles(List<JacketStyleEntity> jacketStyles) {
        this.jacketStyles = jacketStyles;
    }

    public List<PocketStyleEntity> getPocketStyles() {
        return pocketStyles;
    }

    public void setPocketStyles(List<PocketStyleEntity> pocketStyles) {
        this.pocketStyles = pocketStyles;
    }

    public CustomizedJacketEntity getNewCustomizedJacket() {
        return newCustomizedJacket;
    }

    public void setNewCustomizedJacket(CustomizedJacketEntity newCustomizedJacket) {
        this.newCustomizedJacket = newCustomizedJacket;
    }

    public Long getInnerFabricId() {
        return innerFabricId;
    }

    public void setInnerFabricId(Long innerFabricId) {
        this.innerFabricId = innerFabricId;
    }

    public Long getOuterFabricId() {
        return outerFabricId;
    }

    public void setOuterFabricId(Long outerFabricId) {
        this.outerFabricId = outerFabricId;
    }

    public Long getJacketStyleId() {
        return jacketStyleId;
    }

    public void setJacketStyleId(Long jacketStyleId) {
        this.jacketStyleId = jacketStyleId;
    }

    public Long getPocketStyleId() {
        return pocketStyleId;
    }

    public void setPocketStyleId(Long pocketStyleId) {
        this.pocketStyleId = pocketStyleId;
    }
}
