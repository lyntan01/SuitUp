/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ColourSessionBeanLocal;
import ejb.session.stateless.FabricSessionBeanLocal;
import ejb.session.stateless.JacketStyleSessionBeanLocal;
import ejb.session.stateless.PantsCuttingSessionBeanLocal;
import ejb.session.stateless.PocketStyleSessionBeanLocal;
import entity.ColourEntity;
import entity.FabricEntity;
import entity.JacketStyleEntity;
import entity.PantsCuttingEntity;
import entity.PocketStyleEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import util.exception.ColourIdExistException;
import util.exception.ColourNotFoundException;
import util.exception.CustomizationIdExistException;
import util.exception.CustomizationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Named(value = "customizationManagementManagedBean")
@ViewScoped
public class CustomizationManagementManagedBean implements Serializable {

    @EJB
    private PantsCuttingSessionBeanLocal pantsCuttingSessionBean;

    @EJB
    private FabricSessionBeanLocal fabricSessionBean;

    @EJB
    private PocketStyleSessionBeanLocal pocketStyleSessionBean;

    @EJB
    private JacketStyleSessionBeanLocal jacketStyleSessionBean;

    @EJB
    private ColourSessionBeanLocal colourSessionBean;
    
    @Inject
    private ViewCustomizationProductManagedBean viewCustomizationProductManagedBean;

    private List<ColourEntity> colours;
    private List<JacketStyleEntity> jacketStyles;
    private List<PocketStyleEntity> pocketStyles;
    private List<FabricEntity> fabrics;
    private List<PantsCuttingEntity> pantsCuttings;
    
    private List<ColourEntity> filteredColours;
    private List<JacketStyleEntity> filteredJacketStyles;
    private List<PocketStyleEntity> filteredPocketStyles;
    private List<FabricEntity> filteredFabrics;
    private List<PantsCuttingEntity> filteredPantsCuttings;
    
    private ColourEntity newColour;
    private JacketStyleEntity newJacketStyle;
    private PocketStyleEntity newPocketStyle;
    private FabricEntity newFabric;
    private Long newColourId;
    private PantsCuttingEntity newPantsCutting;
    
    private ColourEntity selectedColourToUpdate;
    private JacketStyleEntity selectedJacketStyleToUpdate;
    private PocketStyleEntity selectedPocketStyleToUpdate;
    private FabricEntity selectedFabricToUpdate;
    private Long selectedColourIdUpdate;
    private PantsCuttingEntity selectedPantsCuttingToUpdate; 
    
    
    public CustomizationManagementManagedBean() {
        newColour = new ColourEntity();
        newJacketStyle = new JacketStyleEntity();
        newPocketStyle = new PocketStyleEntity();
        newFabric = new FabricEntity();
        newPantsCutting = new PantsCuttingEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        colours = colourSessionBean.retrieveAllColours();
        jacketStyles = jacketStyleSessionBean.retrieveAllJacketStyles();
        pocketStyles = pocketStyleSessionBean.retrieveAllPocketStyles();
        fabrics = fabricSessionBean.retrieveAllFabrics();
        pantsCuttings = pantsCuttingSessionBean.retrieveAllPantsCutting();
    }
    
    //<----------------------------CREATE--------------------------------------->
    public void createNewColour(ActionEvent event) {
        try {
            Long colourId = colourSessionBean.createNewColour(newColour);
            ColourEntity colour = colourSessionBean.retrieveColourByColourId(colourId);
            colours.add(colour);
            
            if(filteredColours != null) {
                filteredColours.add(colour);
            }
            newColour = new ColourEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New colour created successfully (Colour ID: " + colourId + ")", null));
        } catch (ColourIdExistException | ColourNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error occurred when creating new Colour: " + ex.getMessage(), null));
        }
    }
    
    public void createNewJacketStyle(ActionEvent event) {
        try {
            Long jacketStyleId = jacketStyleSessionBean.createNewJacketStyle(newJacketStyle);
            JacketStyleEntity jacketStyle = jacketStyleSessionBean.retrieveJacketStyleById(jacketStyleId);
            jacketStyles.add(jacketStyle);
            if (filteredJacketStyles != null) {
                filteredJacketStyles.add(jacketStyle);
            }
            newJacketStyle = new JacketStyleEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Jacket Style created successfully (Jacket Style ID: " + jacketStyleId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException | CustomizationIdExistException | CustomizationNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error occurred when creating new Jacket Style: " + ex.getMessage(), null));
        }
    }
    
    public void createNewPocketStyle(ActionEvent event) {
        try {
            Long pocketStyleId = pocketStyleSessionBean.createNewPocketStyle(newPocketStyle);
            PocketStyleEntity pocketStyle = pocketStyleSessionBean.retrievePocketStyleById(pocketStyleId);
            pocketStyles.add(pocketStyle);
            if (filteredPocketStyles != null) {
                filteredPocketStyles.add(pocketStyle);
            }
            newPocketStyle = new PocketStyleEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Pocket Style created successfully (Pocket Style ID: " + pocketStyleId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException | CustomizationIdExistException | CustomizationNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error occurred when creating new Pocket Style: " + ex.getMessage(), null));
        }
    }
    
    public void createNewFabric(ActionEvent event) {
        try {
            Long fabricId = fabricSessionBean.createNewFabric(newFabric, newColourId);
            FabricEntity fabric = fabricSessionBean.retrieveFabricById(fabricId);
            fabrics.add(fabric);
            if (filteredFabrics != null) {
                filteredFabrics.add(fabric);
            }
            newFabric = new FabricEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New fabric created successfully (Fabric ID: " + fabricId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException | CustomizationIdExistException | CustomizationNotFoundException | ColourNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error occurred when creating new Fabric: " + ex.getMessage(), null));
        }
    }
    
    public void createNewPantsCutting(ActionEvent event) {
        try {
            Long pantsCuttingId = pantsCuttingSessionBean.createNewPantsCutting(newPantsCutting);
            PantsCuttingEntity pantsCutting = pantsCuttingSessionBean.retrievePantsCuttingById(pantsCuttingId);
            pantsCuttings.add(pantsCutting);
            if (filteredPantsCuttings != null) {
                filteredPantsCuttings.add(pantsCutting);
            }
            newPantsCutting = new PantsCuttingEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Pants Cutting created successfully (Pants Cutting ID: " + pantsCuttingId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException | CustomizationIdExistException | CustomizationNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error occurred when creating new Pants Cutting: " + ex.getMessage(), null));
        }
    }
    //<----------------------------UPDATE--------------------------------------->
    public void doUpdateColour(ActionEvent event)
    {
        selectedColourToUpdate = (ColourEntity) event.getComponent().getAttributes().get("selectedColourToUpdate");
    }

    public void updateColour(ActionEvent event)
    {
        try
        {
            colourSessionBean.updateColour(selectedColourToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Colour updated successfully", null));
        } 
        catch (ColourNotFoundException | InputDataValidationException | UpdateEntityException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating colour: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateJacketStyle(ActionEvent event)
    {
        selectedJacketStyleToUpdate = (JacketStyleEntity) event.getComponent().getAttributes().get("selectedJacketStyleToUpdate");
    }

    public void updateJacketStyle(ActionEvent event)
    {
        try
        {
            jacketStyleSessionBean.updateJacketStyle(selectedJacketStyleToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Jacket Style updated successfully", null));
        } 
        catch (InputDataValidationException | UpdateEntityException | CustomizationNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Jacket Style: " + ex.getMessage(), null));
        } 
    }
    
    public void doUpdatePocketStyle(ActionEvent event)
    {
        selectedPocketStyleToUpdate = (PocketStyleEntity) event.getComponent().getAttributes().get("selectedPocketStyleToUpdate");
    }

    public void updatePocketStyle(ActionEvent event)
    {
        try
        {
            pocketStyleSessionBean.updatePocketStyle(selectedPocketStyleToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pocket Style updated successfully", null));
        } 
        catch (CustomizationNotFoundException | InputDataValidationException | UpdateEntityException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Pocket Style: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateFabric(ActionEvent event)
    {
        selectedFabricToUpdate = (FabricEntity) event.getComponent().getAttributes().get("selectedFabricToUpdate");
        selectedColourIdUpdate = selectedFabricToUpdate.getColour().getColourId();

    }

    public void updateFabric(ActionEvent event)
    {
        try
        {
            fabricSessionBean.updateFabric(selectedFabricToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Fabric updated successfully", null));
        } 
        catch (CustomizationNotFoundException | InputDataValidationException | UpdateEntityException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating fabric: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdatePantsCutting(ActionEvent event)
    {
        selectedPantsCuttingToUpdate = (PantsCuttingEntity) event.getComponent().getAttributes().get("selectedPantsCuttingToUpdate");
    }

    public void updatePantsCutting(ActionEvent event)
    {
        try
        {
            pantsCuttingSessionBean.updatePantsCutting(selectedPantsCuttingToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "PantsCutting updated successfully", null));
        } 
        catch (CustomizationNotFoundException | InputDataValidationException | UpdateEntityException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating pantsCutting: " + ex.getMessage(), null));
        }
    }
    //<----------------------------DELETE--------------------------------------->

    public void deleteColour(ActionEvent event) {
        try {
            ColourEntity colourToDelete = (ColourEntity) event.getComponent().getAttributes().get("colourToDelete");
            colourSessionBean.deleteColour(colourToDelete.getColourId());

            colours.remove(colourToDelete);

            if (filteredColours != null) {
                filteredColours.remove(colourToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, colourToDelete.getName() + " deleted successfully", null));

        } catch (ColourNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting colour: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deleteJacketStyle(ActionEvent event)
    {
        try
        {
            JacketStyleEntity jacketStyleToDelete = (JacketStyleEntity) event.getComponent().getAttributes().get("jacketStyleToDelete");
            jacketStyleSessionBean.deleteJacketStyle(jacketStyleToDelete.getCustomizationId());

            getJacketStyles().remove(jacketStyleToDelete);

            if (getFilteredJacketStyles() != null)
            {
                getFilteredJacketStyles().remove(jacketStyleToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, jacketStyleToDelete.getName() + " deleted successfully", null));
            
        } 
        catch (CustomizationNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting jacketStyle: " + ex.getMessage(), null));
        }  
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }    
    }
    
    public void deletePocketStyle(ActionEvent event)
    {
        try
        {
            PocketStyleEntity pocketStyleToDelete = (PocketStyleEntity) event.getComponent().getAttributes().get("pocketStyleToDelete");
            pocketStyleSessionBean.deletePocketStyle(pocketStyleToDelete.getCustomizationId());

            getPocketStyles().remove(pocketStyleToDelete);

            if (getFilteredPocketStyles() != null)
            {
                getFilteredPocketStyles().remove(pocketStyleToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, pocketStyleToDelete.getName() + " deleted successfully", null));
            
        } 
        catch (CustomizationNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting pocketStyle: " + ex.getMessage(), null));
        }  
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }    
    }
    
    public void deleteFabric(ActionEvent event)
    {
        try
        {
            FabricEntity fabricToDelete = (FabricEntity) event.getComponent().getAttributes().get("fabricToDelete");
            fabricSessionBean.deleteFabric(fabricToDelete.getCustomizationId());

            getFabrics().remove(fabricToDelete);

            if (getFilteredFabrics() != null)
            {
                getFilteredFabrics().remove(fabricToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, fabricToDelete.getName() + " deleted successfully", null));
            
        } 
        catch (CustomizationNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting fabric: " + ex.getMessage(), null));
        }  
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }    
    }
    
    public void deletePantsCutting(ActionEvent event)
    {
        try
        {
            PantsCuttingEntity pantsCuttingToDelete = (PantsCuttingEntity) event.getComponent().getAttributes().get("pantsCuttingToDelete");
            pantsCuttingSessionBean.deletePantsCutting(pantsCuttingToDelete.getCustomizationId());

            getPantsCuttings().remove(pantsCuttingToDelete);

            if (getFilteredPantsCuttings() != null)
            {
                getFilteredPantsCuttings().remove(pantsCuttingToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, pantsCuttingToDelete.getName() + " deleted successfully", null));
            
        } 
        catch (CustomizationNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting pantsCutting: " + ex.getMessage(), null));
        }  
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }    
    }
    //<----------------------FILE UPLOAD----------------------------->
    public void handlePocketStyleFileUpload(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            newPocketStyle.setImage(event.getFile().getFileName());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        }
        catch (IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void handleJacketStyleFileUpload(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            newJacketStyle.setImage(event.getFile().getFileName());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        }
        catch (IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void handleFabricFileUpload(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            newFabric.setImage(event.getFile().getFileName());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        }
        catch (IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }
    
    public void handlePantsCuttingFileUpload(FileUploadEvent event)
    {
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            newPantsCutting.setImage(event.getFile().getFileName());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        }
        catch (IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    

    //<----------------------------GETTERS AND SETTERS--------------------------------------->

    public List<ColourEntity> getColours() {
        return colours;
    }

    public void setColours(List<ColourEntity> colours) {
        this.colours = colours;
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

    public List<FabricEntity> getFabrics() {
        return fabrics;
    }

    public void setFabrics(List<FabricEntity> fabrics) {
        this.fabrics = fabrics;
    }

    public List<PantsCuttingEntity> getPantsCuttings() {
        return pantsCuttings;
    }

    public void setPantsCuttings(List<PantsCuttingEntity> pantsCuttings) {
        this.pantsCuttings = pantsCuttings;
    }

    public List<ColourEntity> getFilteredColours() {
        return filteredColours;
    }

    public void setFilteredColours(List<ColourEntity> filteredColours) {
        this.filteredColours = filteredColours;
    }

    public List<JacketStyleEntity> getFilteredJacketStyles() {
        return filteredJacketStyles;
    }

    public void setFilteredJacketStyles(List<JacketStyleEntity> filteredJacketStyles) {
        this.filteredJacketStyles = filteredJacketStyles;
    }

    public List<PocketStyleEntity> getFilteredPocketStyles() {
        return filteredPocketStyles;
    }

    public void setFilteredPocketStyles(List<PocketStyleEntity> filteredPocketStyles) {
        this.filteredPocketStyles = filteredPocketStyles;
    }

    public List<FabricEntity> getFilteredFabrics() {
        return filteredFabrics;
    }

    public void setFilteredFabrics(List<FabricEntity> filteredFabrics) {
        this.filteredFabrics = filteredFabrics;
    }

    public List<PantsCuttingEntity> getFilteredPantsCuttings() {
        return filteredPantsCuttings;
    }

    public void setFilteredPantsCuttings(List<PantsCuttingEntity> filteredPantsCuttings) {
        this.filteredPantsCuttings = filteredPantsCuttings;
    }

    public ColourEntity getNewColour() {
        return newColour;
    }

    public void setNewColour(ColourEntity newColour) {
        this.newColour = newColour;
    }

    public JacketStyleEntity getNewJacketStyle() {
        return newJacketStyle;
    }

    public void setNewJacketStyle(JacketStyleEntity newJacketStyle) {
        this.newJacketStyle = newJacketStyle;
    }

    public PocketStyleEntity getNewPocketStyle() {
        return newPocketStyle;
    }

    public void setNewPocketStyle(PocketStyleEntity newPocketStyle) {
        this.newPocketStyle = newPocketStyle;
    }

    public FabricEntity getNewFabric() {
        return newFabric;
    }

    public void setNewFabric(FabricEntity newFabric) {
        this.newFabric = newFabric;
    }

    public Long getNewColourId() {
        return newColourId;
    }

    public void setNewColourId(Long newColourId) {
        this.newColourId = newColourId;
    }

    public PantsCuttingEntity getNewPantsCutting() {
        return newPantsCutting;
    }

    public void setNewPantsCutting(PantsCuttingEntity newPantsCutting) {
        this.newPantsCutting = newPantsCutting;
    }

    public ColourEntity getSelectedColourToUpdate() {
        return selectedColourToUpdate;
    }

    public void setSelectedColourToUpdate(ColourEntity selectedColourToUpdate) {
        this.selectedColourToUpdate = selectedColourToUpdate;
    }

    public JacketStyleEntity getSelectedJacketStyleToUpdate() {
        return selectedJacketStyleToUpdate;
    }

    public void setSelectedJacketStyleToUpdate(JacketStyleEntity selectedJacketStyleToUpdate) {
        this.selectedJacketStyleToUpdate = selectedJacketStyleToUpdate;
    }

    public PocketStyleEntity getSelectedPocketStyleToUpdate() {
        return selectedPocketStyleToUpdate;
    }

    public void setSelectedPocketStyleToUpdate(PocketStyleEntity selectedPocketStyleToUpdate) {
        this.selectedPocketStyleToUpdate = selectedPocketStyleToUpdate;
    }

    public FabricEntity getSelectedFabricToUpdate() {
        return selectedFabricToUpdate;
    }

    public void setSelectedFabricToUpdate(FabricEntity selectedFabricToUpdate) {
        this.selectedFabricToUpdate = selectedFabricToUpdate;
    }

    public Long getSelectedColourIdUpdate() {
        return selectedColourIdUpdate;
    }

    public void setSelectedColourIdUpdate(Long selectedColourIdUpdate) {
        this.selectedColourIdUpdate = selectedColourIdUpdate;
    }

    public PantsCuttingEntity getSelectedPantsCuttingToUpdate() {
        return selectedPantsCuttingToUpdate;
    }

    public void setSelectedPantsCuttingToUpdate(PantsCuttingEntity selectedPantsCuttingToUpdate) {
        this.selectedPantsCuttingToUpdate = selectedPantsCuttingToUpdate;
    }

    public ViewCustomizationProductManagedBean getViewCustomizationProductManagedBean() {
        return viewCustomizationProductManagedBean;
    }

    public void setViewCustomizationProductManagedBean(ViewCustomizationProductManagedBean viewCustomizationProductManagedBean) {
        this.viewCustomizationProductManagedBean = viewCustomizationProductManagedBean;
    }


}
