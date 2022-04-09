/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.JacketStyleEntity;
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
public class JacketStyleSessionBean implements JacketStyleSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public JacketStyleSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewJacketStyle(JacketStyleEntity newJacketStyleEntity) throws CustomizationIdExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<JacketStyleEntity>> constraintViolations = validator.validate(newJacketStyleEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newJacketStyleEntity);
                em.flush();
                
                return newJacketStyleEntity.getCustomizationId();
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
    public List<JacketStyleEntity> retrieveAllJacketStyles() {
        Query query = em.createQuery("SELECT c FROM JacketStyleEntity c");
        return query.getResultList();
    }
    
    @Override
    public JacketStyleEntity retrieveJacketStyleById(Long customizationId) throws CustomizationNotFoundException {
        Query query = em.createQuery("SELECT c FROM JacketStyleEntity c WHERE c.customizationId = :customizationId");
        query.setParameter("customizationId", customizationId);
        try {
            return (JacketStyleEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomizationNotFoundException("Jacket Style ID " + customizationId + " does not exists!");
        }
    }
    
    @Override
    public void updateJacketStyle(JacketStyleEntity updatedJacketStyle) throws CustomizationNotFoundException, UpdateEntityException, InputDataValidationException {
        if (updatedJacketStyle != null && updatedJacketStyle.getCustomizationId() != null) {
            Set<ConstraintViolation<JacketStyleEntity>> constraintViolations = validator.validate(updatedJacketStyle);
            
            if (constraintViolations.isEmpty()) {
                JacketStyleEntity jacketStyleToUpdate = retrieveJacketStyleById(updatedJacketStyle.getCustomizationId());
                
                if (jacketStyleToUpdate.getCustomizationId().equals(updatedJacketStyle.getCustomizationId())) {
                    jacketStyleToUpdate.setName(updatedJacketStyle.getName());
                    jacketStyleToUpdate.setDescription(updatedJacketStyle.getDescription());
                    jacketStyleToUpdate.setImage(updatedJacketStyle.getImage());
                    jacketStyleToUpdate.setAdditionalPrice(updatedJacketStyle.getAdditionalPrice());
                } else {
                    throw new UpdateEntityException("Id does not match!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomizationNotFoundException("Jacket Style Id cannot be found");
        }
    }
    
    @Override
    public void deleteJacketStyle(Long jacketStyleId) throws CustomizationNotFoundException {
        JacketStyleEntity jacketStyleToRemove = retrieveJacketStyleById(jacketStyleId);
        
        Query query = em.createQuery("SELECT c FROM CustomizedJacketEntity c WHERE c.jacketStyle.customizationId = :jacketStyleId");
        query.setParameter("jacketStyleId", jacketStyleId);
        
        if (query.getResultList().isEmpty()) {
            em.remove(jacketStyleToRemove);
        } else {
            jacketStyleToRemove.setIsDisabled(Boolean.TRUE);
        }   
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<JacketStyleEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
