/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ColourEntity;
import entity.FabricEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
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
import util.exception.ColourNotFoundException;
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
public class FabricSessionBean implements FabricSessionBeanLocal {
    
    @EJB
    private ColourSessionBeanLocal colourSessionBeanLocal;

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public FabricSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewFabric(FabricEntity newFabricEntity, Long colourId) throws CustomizationIdExistException, ColourNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<FabricEntity>> constraintViolations = validator.validate(newFabricEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                ColourEntity colour = colourSessionBeanLocal.retrieveColourByColourId(colourId);
                newFabricEntity.setColour(colour);
                
                em.persist(newFabricEntity);
                em.flush();
                
                colour.getFabrics().add(newFabricEntity);
                
                return newFabricEntity.getCustomizationId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomizationIdExistException();
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
    public List<FabricEntity> retrieveAllFabrics() {
        Query query = em.createQuery("SELECT c FROM FabricEntity c");
        return query.getResultList();
    }
    
    @Override
    public FabricEntity retrieveFabricById(Long customizationId) throws CustomizationNotFoundException {
        Query query = em.createQuery("SELECT c FROM FabricEntity c WHERE c.customizationId = :customizationId");
        query.setParameter("customizationId", customizationId);
        try {
            return (FabricEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomizationNotFoundException("Fabric ID " + customizationId + " does not exists!");
        }
    }
    
    @Override
    public void updateFabric(FabricEntity updatedFabric) throws CustomizationNotFoundException, UpdateEntityException, InputDataValidationException {
        if (updatedFabric != null && updatedFabric.getCustomizationId() != null) {
            Set<ConstraintViolation<FabricEntity>> constraintViolations = validator.validate(updatedFabric);
            
            if (constraintViolations.isEmpty()) {
                FabricEntity fabricToUpdate = retrieveFabricById(updatedFabric.getCustomizationId());
                
                if (fabricToUpdate.getCustomizationId().equals(updatedFabric.getCustomizationId())) {
                    fabricToUpdate.setName(updatedFabric.getName());
                    fabricToUpdate.setDescription(updatedFabric.getDescription());
                    fabricToUpdate.setImage(updatedFabric.getImage());
                    fabricToUpdate.setAdditionalPrice(updatedFabric.getAdditionalPrice());
                } else {
                    throw new UpdateEntityException("Id does not match!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomizationNotFoundException("Fabric Id cannot be found");
        }
    }
    
    @Override
    public void deleteFabric(Long fabricId) throws CustomizationNotFoundException {
        FabricEntity fabricToRemove = retrieveFabricById(fabricId);
        
        Query query_one = em.createQuery("SELECT c FROM CustomizedPantsEntity c WHERE c.fabric.customizationId = :fabricId");
        query_one.setParameter("fabricId", fabricId);
        
        Query query_two = em.createQuery("SELECT c FROM CustomizedJacketEntity c WHERE c.innerFabric.customizationId = :fabricId");
        query_two.setParameter("fabricId", fabricId);
        
        Query query_three = em.createQuery("SELECT c FROM CustomizedJacketEntity c WHERE c.outerFabric.customizationId = :fabricId");
        query_three.setParameter("fabricId", fabricId);
        
        if (query_one.getResultList().isEmpty() && query_two.getResultList().isEmpty() && query_three.getResultList().isEmpty()) {
            fabricToRemove.getColour().getFabrics().remove(fabricToRemove);
            fabricToRemove.setColour(null);
            
            em.remove(fabricToRemove);
        } else {
            fabricToRemove.setIsDisabled(Boolean.TRUE);
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FabricEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
