/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PocketStyleEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomizationIdExistException;
import util.exception.CustomizationNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author meganyee
 */
@Stateless
public class PocketStyleSessionBean implements PocketStyleSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PocketStyleSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewPocketStyle(PocketStyleEntity newPocketStyleEntity) throws CustomizationIdExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<PocketStyleEntity>> constraintViolations = validator.validate(newPocketStyleEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newPocketStyleEntity);
                em.flush();
                
                return newPocketStyleEntity.getCustomizationId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomizationIdExistException("Customization ID already exists.");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } 
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<PocketStyleEntity> retrieveAllPocketStyles() {
        Query query = em.createQuery("SELECT c FROM PocketStyleEntity c");
        return query.getResultList();
    }
    
    @Override
    public PocketStyleEntity retrievePocketStyleById(Long customizationId) throws CustomizationNotFoundException {
        Query query = em.createQuery("SELECT c FROM PocketStyleEntity c WHERE c.customizationId = :customizationId");
        query.setParameter("customizationId", customizationId);
        try {
            return (PocketStyleEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomizationNotFoundException("Pocket Style ID " + customizationId + " does not exists!");
        }
    }
    
    @Override
    public void updatePocketStyle(PocketStyleEntity updatedPocketStyle) throws CustomizationNotFoundException, UpdateEntityException, InputDataValidationException {
        if (updatedPocketStyle != null && updatedPocketStyle.getCustomizationId() != null) {
            Set<ConstraintViolation<PocketStyleEntity>> constraintViolations = validator.validate(updatedPocketStyle);
            
            if (constraintViolations.isEmpty()) {
                PocketStyleEntity pocketStyleToUpdate = retrievePocketStyleById(updatedPocketStyle.getCustomizationId());
                
                if (pocketStyleToUpdate.getCustomizationId().equals(updatedPocketStyle.getCustomizationId())) {
                    pocketStyleToUpdate.setName(updatedPocketStyle.getName());
                    pocketStyleToUpdate.setDescription(updatedPocketStyle.getDescription());
                    pocketStyleToUpdate.setImage(updatedPocketStyle.getImage());
                    pocketStyleToUpdate.setAdditionalPrice(updatedPocketStyle.getAdditionalPrice());
                } else {
                    throw new UpdateEntityException("Id does not match!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomizationNotFoundException("Pocket Style Id cannot be found");
        }
    }
    
    @Override
    public void deletePocketStyle(Long pocketStyleId) throws CustomizationNotFoundException {
        PocketStyleEntity pocketStyleToRemove = retrievePocketStyleById(pocketStyleId);
        
        Query query = em.createQuery("SELECT c FROM CustomizedJacketEntity c WHERE c.pocketStyle.customizationId = :pocketStyleId");
        query.setParameter("pocketStyleId", pocketStyleId);
        
        if (query.getResultList().isEmpty()) {
            em.remove(pocketStyleToRemove);
        } else {
            pocketStyleToRemove.setIsDisabled(Boolean.TRUE);
        } 
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PocketStyleEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
