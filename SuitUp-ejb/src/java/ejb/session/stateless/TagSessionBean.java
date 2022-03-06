/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StandardProductEntity;
import entity.TagEntity;
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
import util.exception.TagNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Stateless
public class TagSessionBean implements TagSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public TagSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public TagEntity createNewTag(TagEntity newTagEntity) throws UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<TagEntity>>constraintViolations = validator.validate(newTagEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newTagEntity);
                em.flush();

                return newTagEntity;
                
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    

    @Override
    public List<TagEntity> retrieveAllTags()
    {
        Query query = em.createQuery("SELECT t FROM TagEntity t ORDER BY t.name ASC");
        List<TagEntity> tagEntities = query.getResultList();
        
        for(TagEntity tagEntity:tagEntities)
        {
            tagEntity.getStandardProducts().size();
        }
        
        return tagEntities;
    }
    
    
    @Override
    public TagEntity retrieveTagByTagId(Long tagId) throws TagNotFoundException
    {
        TagEntity tagEntity = em.find(TagEntity.class, tagId);
        
        if(tagEntity != null)
        {
            return tagEntity;
        }
        else
        {
            throw new TagNotFoundException("Tag ID " + tagId + " does not exist!");
        }               
    }
    
    
    @Override
    public void updateTag(TagEntity tagEntity) throws InputDataValidationException, TagNotFoundException, UpdateEntityException
    {
        Set<ConstraintViolation<TagEntity>>constraintViolations = validator.validate(tagEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(tagEntity.getTagId()!= null)
            {
                TagEntity tagEntityToUpdate = retrieveTagByTagId(tagEntity.getTagId());
                
                Query query = em.createQuery("SELECT c FROM TagEntity c WHERE c.name = :inName AND c.tagId <> :inTagId");
                query.setParameter("inName", tagEntity.getName());
                query.setParameter("inTagId", tagEntity.getTagId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateEntityException("The name of the tag to be updated is duplicated");
                }
                
                tagEntityToUpdate.setName(tagEntity.getName());                                          
            }
            else
            {
                throw new TagNotFoundException("Tag ID not provided for tag to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
     
    @Override
    public void deleteTag(Long tagId) throws TagNotFoundException, DeleteEntityException
    {
        TagEntity tagEntityToRemove = retrieveTagByTagId(tagId);
        
        for(StandardProductEntity product: tagEntityToRemove.getStandardProducts()) {
            product.getTags().remove(tagEntityToRemove);
        }
        
        tagEntityToRemove.getStandardProducts().clear();
        em.remove(tagEntityToRemove);             
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TagEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
