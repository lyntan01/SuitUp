/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PantsCuttingEntity;
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
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author meganyee
 */
@Stateless
public class PantsCuttingSessionBean implements PantsCuttingSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PantsCuttingSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewPantsCutting(PantsCuttingEntity newPantsCuttingEntity) throws CustomizationIdExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<PantsCuttingEntity>> constraintViolations = validator.validate(newPantsCuttingEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newPantsCuttingEntity);
                em.flush();
                
                return newPantsCuttingEntity.getCustomizationId();
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
    public List<PantsCuttingEntity> retrieveAllPantsCutting() {
        Query query = em.createQuery("SELECT c FROM PantsCuttingEntity c");
        return query.getResultList();
    }
    
    @Override
    public PantsCuttingEntity retrievePantsCuttingById(Long customizationId) throws CustomizationNotFoundException {
        Query query = em.createQuery("SELECT c FROM PantsCuttingEntity c WHERE c.customizationId = :customizationId");
        query.setParameter("customizationId", customizationId);
        try {
            return (PantsCuttingEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomizationNotFoundException("Pants Cutting ID " + customizationId + " does not exists!");
        }
    }
    
    @Override
    public void updatePantsCutting(PantsCuttingEntity updatedPantsCutting) throws CustomizationNotFoundException, UpdateEntityException, InputDataValidationException {
        if (updatedPantsCutting != null && updatedPantsCutting.getCustomizationId() != null) {
            Set<ConstraintViolation<PantsCuttingEntity>> constraintViolations = validator.validate(updatedPantsCutting);
            
            if (constraintViolations.isEmpty()) {
                PantsCuttingEntity pantsCuttingToUpdate = retrievePantsCuttingById(updatedPantsCutting.getCustomizationId());
                
                if (pantsCuttingToUpdate.getCustomizationId().equals(updatedPantsCutting.getCustomizationId())) {
                    pantsCuttingToUpdate.setName(updatedPantsCutting.getName());
                    pantsCuttingToUpdate.setDescription(updatedPantsCutting.getDescription());
                    pantsCuttingToUpdate.setImage(updatedPantsCutting.getImage());
                    pantsCuttingToUpdate.setAdditionalPrice(updatedPantsCutting.getAdditionalPrice());
                } else {
                    throw new UpdateEntityException("Id does not match!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomizationNotFoundException("Pants Cutting Id cannot be found");
        }
    }
    
    @Override
    public void deletePantsCutting(Long pantsCuttingId) throws CustomizationNotFoundException {
        PantsCuttingEntity pantsCuttingToRemove = retrievePantsCuttingById(pantsCuttingId);
        
        Query query = em.createQuery("SELECT c FROM CustomizedPantsEntity c WHERE c.pantsCutting.customizationId = :pantsCuttingId");
        query.setParameter("pantsCuttingId", pantsCuttingId);
        
        if (query.getResultList().isEmpty()) {
            em.remove(pantsCuttingToRemove);
        } else {
            pantsCuttingToRemove.setIsDisabled(Boolean.TRUE);
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PantsCuttingEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
