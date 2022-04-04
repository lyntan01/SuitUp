/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.StandardProductEntity;
import entity.TagEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
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
import util.exception.CreateStandardProductException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.StandardProductInsufficientQuantityOnHandException;
import util.exception.StandardProductNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Stateless
public class StandardProductSessionBean implements StandardProductSessionBeanLocal {

    @EJB
    private TagSessionBeanLocal tagSessionBean;

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StandardProductSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    //assumed that category and tags have to be created before product is 
    @Override
    public Long createNewStandardProduct(StandardProductEntity newStandardProductEntity, Long categoryId, List<Long> tagsId) throws UnknownPersistenceException, InputDataValidationException, CreateStandardProductException {
        Set<ConstraintViolation<StandardProductEntity>> constraintViolations = validator.validate(newStandardProductEntity);

        if (constraintViolations.isEmpty()) {
            try {
                CategoryEntity category = categorySessionBean.retrieveCategoryByCategoryId(categoryId);
                category.getStandardProducts().add(newStandardProductEntity);
                newStandardProductEntity.setCategory(category);
                for(Long tagId: tagsId) {
                    TagEntity tag = tagSessionBean.retrieveTagByTagId(tagId);
                    tag.getStandardProducts().add(newStandardProductEntity);
                    newStandardProductEntity.getTags().add(tag);
                }
                em.persist(newStandardProductEntity);
                em.flush();
                return newStandardProductEntity.getProductId();
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            } catch (CategoryNotFoundException | TagNotFoundException ex) {
                throw new CreateStandardProductException("Error occured when creating product: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<StandardProductEntity> retrieveAllStandardProducts() {
        Query query = em.createQuery("SELECT s FROM StandardProductEntity s");
        List<StandardProductEntity> standardProductEntities = query.getResultList();
        
        for(StandardProductEntity standardProductEntity: standardProductEntities)
        {
            standardProductEntity.getCategory();
            standardProductEntity.getTags().size();
        }
        
        return standardProductEntities;
    }
    
    @Override
    public void debitQuantityOnHand(Long productId, int quantity) throws StandardProductInsufficientQuantityOnHandException, StandardProductNotFoundException {
        StandardProductEntity standardProductEntity = retrieveStandardProductByStandardProductId(productId);
        if (standardProductEntity.getQuantityInStock() >= quantity) {
            standardProductEntity.setQuantityInStock(standardProductEntity.getQuantityInStock() - quantity);
        } else {
            throw new StandardProductInsufficientQuantityOnHandException("Error: Unable to fulfil order because " + standardProductEntity.getName() + " does not have enough quantity in stock");
        }
    }

    @Override
    public StandardProductEntity retrieveStandardProductByStandardProductId(Long standardProductId) throws StandardProductNotFoundException {
        StandardProductEntity standardProductEntity = em.find(StandardProductEntity.class, standardProductId);

        if (standardProductEntity != null) {
            standardProductEntity.getCategory();
            standardProductEntity.getTags().size();
            return standardProductEntity;
        } else {
            throw new StandardProductNotFoundException("StandardProduct ID " + standardProductId + " does not exist!");
        }
    }
    
    //if category and/or tags remain unchange -> null 
    @Override
    public void updateStandardProduct(StandardProductEntity standardProductEntity, Long categoryId, List<Long> tagsId) throws StandardProductNotFoundException, UpdateEntityException, InputDataValidationException {
        if (standardProductEntity != null && standardProductEntity.getProductId() != null) {
            Set<ConstraintViolation<StandardProductEntity>> constraintViolations = validator.validate(standardProductEntity);

            if (constraintViolations.isEmpty()) {
                StandardProductEntity standardProductEntityToUpdate = retrieveStandardProductByStandardProductId(standardProductEntity.getProductId());

                if (standardProductEntityToUpdate.getProductId().equals(standardProductEntity.getProductId())) {
                    standardProductEntityToUpdate.setName(standardProductEntity.getName());
                    standardProductEntityToUpdate.setDescription(standardProductEntity.getDescription());
                    standardProductEntityToUpdate.setImage(standardProductEntity.getImage());
                    standardProductEntityToUpdate.setSkuCode(standardProductEntity.getSkuCode());
                    standardProductEntityToUpdate.setUnitPrice(standardProductEntity.getUnitPrice());
                    standardProductEntityToUpdate.setQuantityInStock(standardProductEntity.getQuantityInStock());
                    standardProductEntityToUpdate.setReorderQuantity(standardProductEntity.getReorderQuantity());
                    if(categoryId != null) {
                        try {
                            CategoryEntity category = categorySessionBean.retrieveCategoryByCategoryId(categoryId);
                            standardProductEntityToUpdate.getCategory().getStandardProducts().remove(standardProductEntityToUpdate);
                            standardProductEntityToUpdate.setCategory(category);
                            category.getStandardProducts().add(standardProductEntityToUpdate);
                        } catch (CategoryNotFoundException ex) {
                            throw new UpdateEntityException("Error occured when updating StandardProduct with ID " + standardProductEntity.getProductId() + " : " + ex.getMessage());
                        }
                    } 
                    if(!tagsId.isEmpty()) {
                        for(TagEntity tag: standardProductEntityToUpdate.getTags()) {
                            tag.getStandardProducts().remove(standardProductEntityToUpdate);
                        }
                        standardProductEntityToUpdate.getTags().clear();
                        for(Long tagId: tagsId) {
                            try {
                                TagEntity tag = tagSessionBean.retrieveTagByTagId(tagId);
                                tag.getStandardProducts().add(standardProductEntityToUpdate);
                                standardProductEntityToUpdate.getTags().add(tag);
                            } catch (TagNotFoundException ex) {
                                throw new UpdateEntityException("Error occured when updating StandardProduct with ID " + standardProductEntity.getProductId() + " : " + ex.getMessage());
                            }
                        }
                    }
                } else {
                    throw new UpdateEntityException("StandardProduct ID of standardProduct record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new StandardProductNotFoundException("StandardProduct ID not provided for standardProduct to be updated");
        }
    }

    @Override
    public void deleteStandardProduct(Long standardProductId) throws StandardProductNotFoundException, DeleteEntityException {
        StandardProductEntity standardProductEntityToRemove = retrieveStandardProductByStandardProductId(standardProductId);
        
        Query query = em.createQuery("SELECT o FROM OrderLineEntity o WHERE o.productEntity.name = :name");
        query.setParameter("name", standardProductEntityToRemove.getName());
        
        if(query.getResultList().isEmpty())
        {
            // Disassociation
            for (TagEntity tag : standardProductEntityToRemove.getTags()) {
                tag.getStandardProducts().remove(standardProductEntityToRemove);
            }
            standardProductEntityToRemove.getTags().clear();
            standardProductEntityToRemove.getCategory().getStandardProducts().remove(standardProductEntityToRemove);
            standardProductEntityToRemove.getCategory().setCategoryId(null);
            em.remove(standardProductEntityToRemove);
        }
        else
        {
            // Prevent deleting standardProducts with existing order line entity 
            throw new DeleteEntityException("StandardProduct ID " + standardProductId + " is associated with existing order(s) and cannot be deleted!");
        }
        
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<StandardProductEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    
}
