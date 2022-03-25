/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CategorySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    //assume that category cannot be created with a bunch of products tagged -> products to be tagged later
    @Override
    public Long createNewCategory(CategoryEntity newCategoryEntity) throws UnknownPersistenceException, InputDataValidationException, CreateNewCategoryException
    {
        Set<ConstraintViolation<CategoryEntity>>constraintViolations = validator.validate(newCategoryEntity);
       
        if (constraintViolations.isEmpty()) {
            try {
                Query query = em.createQuery("SELECT c FROM CategoryEntity c WHERE c.name = :name");
                query.setParameter("name", newCategoryEntity.getName());
                if (query.getResultList().isEmpty()) {
                    em.persist(newCategoryEntity);
                    em.flush();
                    return newCategoryEntity.getCategoryId();

                } else {
                    throw new CreateNewCategoryException("There is an existing category with the same category name");
                }
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    

    @Override
    public List<CategoryEntity> retrieveAllCategories()
    {
        Query query = em.createQuery("SELECT c FROM CategoryEntity c ORDER BY c.name ASC");
        List<CategoryEntity> categoryEntities = query.getResultList();
        
        for(CategoryEntity categoryEntity:categoryEntities)
        {
            categoryEntity.getStandardProducts().size();
        }
        
        return categoryEntities;
    }
    
    
    @Override
    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException
    {
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
        
        if(categoryEntity != null)
        {
            categoryEntity.getStandardProducts().size();
            return categoryEntity;
        }
        else
        {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }               
    }
    
    
    
    //change name and description
    @Override
    public void updateCategory(CategoryEntity categoryEntity) throws InputDataValidationException, CategoryNotFoundException, UpdateEntityException
    {
        Set<ConstraintViolation<CategoryEntity>>constraintViolations = validator.validate(categoryEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(categoryEntity.getCategoryId()!= null)
            {
                CategoryEntity categoryEntityToUpdate = retrieveCategoryByCategoryId(categoryEntity.getCategoryId());
                
                Query query = em.createQuery("SELECT c FROM CategoryEntity c WHERE c.name = :inName AND c.categoryId != :inCategoryId");
                query.setParameter("inName", categoryEntity.getName());
                query.setParameter("inCategoryId", categoryEntity.getCategoryId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateEntityException("The name of the category to be updated is duplicated");
                }
                else 
                {
                categoryEntityToUpdate.setName(categoryEntity.getName());
                categoryEntityToUpdate.setDescription(categoryEntity.getDescription());  
                }
            }
            else
            {
                throw new CategoryNotFoundException("Category ID not provided for category to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteEntityException
    {
        CategoryEntity categoryEntityToRemove = retrieveCategoryByCategoryId(categoryId);
        
        if(!categoryEntityToRemove.getStandardProducts().isEmpty())
        {
            throw new DeleteEntityException("Error: Unable to delete category because there are products under it");
        }
        else
        {            
            em.remove(categoryEntityToRemove);
        }                
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CategoryEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    
}
