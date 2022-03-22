/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.StandardProductSessionBeanLocal;
import ejb.session.stateless.TagSessionBeanLocal;
import entity.CategoryEntity;
import entity.StandardProductEntity;
import entity.TagEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.primefaces.event.FileUploadEvent;
import util.exception.CategoryNotFoundException;
import util.exception.CreateStandardProductException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.StandardProductNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Named(value = "standardProductManagementManagedBean")
@ViewScoped
public class StandardProductManagementManagedBean implements Serializable {

    @EJB
    private TagSessionBeanLocal tagSessionBean;

    @EJB
    private StandardProductSessionBeanLocal standardProductSessionBean;

    @EJB
    private CategorySessionBeanLocal categorySessionBean;
    
    @Inject
    private ViewStandardProductManagedBean viewStandardProductManagedBean;

    private List<TagEntity> tags;
    private List<StandardProductEntity> standardProducts;
    private List<CategoryEntity> categories;
    
    private List<TagEntity> filteredTags;
    private List<StandardProductEntity> filteredStandardProducts;
    private List<CategoryEntity> filteredCategories;
    
    private CategoryEntity newCategoryEntity;
    private StandardProductEntity newStandardProductEntity;
    private Long newCategoryId;
    private List<Long> newTagIds;
    private TagEntity newTagEntity;
    
    private CategoryEntity selectedCategoryToUpdate;
    private StandardProductEntity selectedStandardProductToUpdate;
    private Long categoryIdUpdate;
    private List<Long> tagIdsUpdate;
    private TagEntity selectedTagToUpdate;
    
    public StandardProductManagementManagedBean() {
        newCategoryEntity = new CategoryEntity();
        newStandardProductEntity = new StandardProductEntity();
        newTagEntity = new TagEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        tags = tagSessionBean.retrieveAllTags();
        standardProducts = standardProductSessionBean.retrieveAllStandardProducts();
        categories = categorySessionBean.retrieveAllCategories();
    }
    
    //<----------------------------CREATE--------------------------------------->
    
    public void createNewStandardProduct(ActionEvent event) throws StandardProductNotFoundException {
        try {
            Long newStandardProductId = standardProductSessionBean.createNewStandardProduct(newStandardProductEntity, newCategoryId, newTagIds);
            standardProducts.add(newStandardProductEntity);

            if (filteredStandardProducts != null) {
                filteredStandardProducts.add(newStandardProductEntity);
            }
            newStandardProductEntity = new StandardProductEntity();
            newCategoryId = null;
            newTagIds = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New product created successfully (Product ID: " + newStandardProductId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException | CreateStandardProductException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error occurred when creating new Standard Product: " + ex.getMessage(), null));
        }
    }

    public void createNewCategory(ActionEvent event) {
        try {
            Long categoryId = categorySessionBean.createNewCategory(newCategoryEntity);
            if (filteredCategories != null) {
                filteredCategories.add(newCategoryEntity);
            }
            newCategoryEntity = new CategoryEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New category created successfully (Category ID: " + categoryId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error occurred when creating new Category: " + ex.getMessage(), null));
        }
    }
    
    public void createNewTag (ActionEvent event) {
        try {
            Long newTagId = tagSessionBean.createNewTag(newTagEntity);
            if (filteredTags != null) {
                filteredTags.add(newTagEntity);
            }
            newTagEntity = new TagEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New tag created successfully (Tag ID: " + newTagId + ")", null));
        } catch (UnknownPersistenceException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error occurred when creating new tag: " + ex.getMessage(), null));
        }
    }
    
    //<----------------------------DELETE--------------------------------------->
    public void deleteCategory(ActionEvent event)
    {
        try
        {
            CategoryEntity categoryEntityToDelete = (CategoryEntity) event.getComponent().getAttributes().get("categoryToDelete");
            categorySessionBean.deleteCategory(categoryEntityToDelete.getCategoryId());

            categories.remove(categoryEntityToDelete);

            if (filteredCategories != null)
            {
                filteredCategories.remove(categoryEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, categoryEntityToDelete.getName() + " deleted successfully", null));
            
        } 
        catch (CategoryNotFoundException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting category: " + ex.getMessage(), null));
        }  
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }    
    }
    
    public void deleteTag(ActionEvent event)
    {
        try
        {
            TagEntity tagToDelete = (TagEntity) event.getComponent().getAttributes().get("tagToDelete");
            tagSessionBean.deleteTag(tagToDelete.getTagId());

            tags.remove(tagToDelete);

            if (filteredTags != null)
            {
                filteredTags.remove(tagToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, tagToDelete.getName() + " deleted successfully", null));
        } 
        catch (TagNotFoundException | DeleteEntityException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting tag: " + ex.getMessage(), null));
        } 
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        } 
    }
    
    public void deleteStandardProduct(ActionEvent event)
    {
        try
        {
            StandardProductEntity standardProductToDelete = (StandardProductEntity)event.getComponent().getAttributes().get("standardProductToDelete");
            standardProductSessionBean.deleteStandardProduct(standardProductToDelete.getProductId());
            
            standardProducts.remove(standardProductToDelete);
            
            if(filteredStandardProducts != null)
            {
                filteredStandardProducts.remove(standardProductToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted successfully", null));
        }
        catch(StandardProductNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting product: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
    //<----------------------------UPDATE--------------------------------------->
    public void doUpdateStandardProduct(ActionEvent event)
    {
        selectedStandardProductToUpdate = (StandardProductEntity)event.getComponent().getAttributes().get("selectedStandardProductToUpdate");
        
        categoryIdUpdate = selectedStandardProductToUpdate.getCategory().getCategoryId();
        tagIdsUpdate = new ArrayList<>();

        for(TagEntity tagEntity:selectedStandardProductToUpdate.getTags())
        {
            tagIdsUpdate.add(tagEntity.getTagId());
        }
    }
    
    
    
    public void updateStandardProduct(ActionEvent event)
    {        
        if(categoryIdUpdate == 0)
        {
            categoryIdUpdate = null;
        }                
        
        try
        {
            standardProductSessionBean.updateStandardProduct(selectedStandardProductToUpdate, categoryIdUpdate, tagIdsUpdate);
                        
            for(CategoryEntity ce:categories)
            {
                if(ce.getCategoryId().equals(categoryIdUpdate))
                {
                    selectedStandardProductToUpdate.setCategory(ce);
                    break;
                }                
            }
            
            selectedStandardProductToUpdate.getTags().clear();
            
            for(TagEntity te:tags)
            {
                if(tagIdsUpdate.contains(te.getTagId()))
                {
                    selectedStandardProductToUpdate.getTags().add(te);
                }                
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product updated successfully", null));
        }
        catch(StandardProductNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating product: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateCategory(ActionEvent event)
    {
        selectedCategoryToUpdate = (CategoryEntity) event.getComponent().getAttributes().get("selectedCategoryToUpdate");
    }

    public void updateCategory(ActionEvent event)
    {
        try
        {
            categorySessionBean.updateCategory(selectedCategoryToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category updated successfully", null));
        } 
        catch (CategoryNotFoundException | InputDataValidationException | UpdateEntityException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating size: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateTag(ActionEvent event)
    {
        selectedTagToUpdate = (TagEntity) event.getComponent().getAttributes().get("selectedTagToUpdate");
    }

    public void updateTag(ActionEvent event)
    {
        try
        {
            tagSessionBean.updateTag(selectedTagToUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tag updated successfully", null));
        } 
        catch (TagNotFoundException | InputDataValidationException | UpdateEntityException ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating size: " + ex.getMessage(), null));
        }
    }
        //<----------------------FILE UPLOAD----------------------------->

    public void fileUpload(FileUploadEvent event)
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

            newStandardProductEntity.setImage(event.getFile().getFileName());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        }
        catch (IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    
    //<----------------------GET AND SET ATTRIBUTES----------------------------->

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    public List<StandardProductEntity> getStandardProducts() {
        return standardProducts;
    }

    public void setStandardProducts(List<StandardProductEntity> standardProducts) {
        this.standardProducts = standardProducts;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public List<TagEntity> getFilteredTags() {
        return filteredTags;
    }

    public void setFilteredTags(List<TagEntity> filteredTags) {
        this.filteredTags = filteredTags;
    }

    public List<StandardProductEntity> getFilteredStandardProducts() {
        return filteredStandardProducts;
    }

    public void setFilteredStandardProducts(List<StandardProductEntity> filteredStandardProducts) {
        this.filteredStandardProducts = filteredStandardProducts;
    }

    public List<CategoryEntity> getFilteredCategories() {
        return filteredCategories;
    }

    public void setFilteredCategories(List<CategoryEntity> filteredCategories) {
        this.filteredCategories = filteredCategories;
    }

    public CategoryEntity getNewCategoryEntity() {
        return newCategoryEntity;
    }

    public void setNewCategoryEntity(CategoryEntity newCategoryEntity) {
        this.newCategoryEntity = newCategoryEntity;
    }

    public StandardProductEntity getNewStandardProductEntity() {
        return newStandardProductEntity;
    }

    public void setNewStandardProductEntity(StandardProductEntity newStandardProductEntity) {
        this.newStandardProductEntity = newStandardProductEntity;
    }

    public Long getNewCategoryId() {
        return newCategoryId;
    }

    public void setNewCategoryId(Long newCategoryId) {
        this.newCategoryId = newCategoryId;
    }

    public List<Long> getNewTagIds() {
        return newTagIds;
    }

    public void setNewTagIds(List<Long> newTagIds) {
        this.newTagIds = newTagIds;
    }

    public TagEntity getNewTagEntity() {
        return newTagEntity;
    }

    public void setNewTagEntity(TagEntity newTagEntity) {
        this.newTagEntity = newTagEntity;
    }

    public CategoryEntity getSelectedCategoryToUpdate() {
        return selectedCategoryToUpdate;
    }

    public void setSelectedCategoryToUpdate(CategoryEntity selectedCategoryToUpdate) {
        this.selectedCategoryToUpdate = selectedCategoryToUpdate;
    }

    public StandardProductEntity getSelectedStandardProductToUpdate() {
        return selectedStandardProductToUpdate;
    }

    public void setSelectedStandardProductToUpdate(StandardProductEntity selectedStandardProductToUpdate) {
        this.selectedStandardProductToUpdate = selectedStandardProductToUpdate;
    }

    public TagEntity getSelectedTagToUpdate() {
        return selectedTagToUpdate;
    }

    public void setSelectedTagToUpdate(TagEntity selectedTagToUpdate) {
        this.selectedTagToUpdate = selectedTagToUpdate;
    }

    public Long getCategoryIdUpdate() {
        return categoryIdUpdate;
    }

    public void setCategoryIdUpdate(Long categoryIdUpdate) {
        this.categoryIdUpdate = categoryIdUpdate;
    }

    public List<Long> getTagIdsUpdate() {
        return tagIdsUpdate;
    }

    public void setTagIdsUpdate(List<Long> tagIdsUpdate) {
        this.tagIdsUpdate = tagIdsUpdate;
    }

    public ViewStandardProductManagedBean getViewStandardProductManagedBean() {
        return viewStandardProductManagedBean;
    }

    public void setViewStandardProductManagedBean(ViewStandardProductManagedBean viewStandardProductManagedBean) {
        this.viewStandardProductManagedBean = viewStandardProductManagedBean;
    }

}
