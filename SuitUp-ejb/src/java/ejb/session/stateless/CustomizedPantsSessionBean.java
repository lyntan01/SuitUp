/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomizedPantsEntity;
import entity.FabricEntity;
import entity.PantsCuttingEntity;
import entity.PantsMeasurementEntity;
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
import util.exception.CustomizationNotFoundException;
import util.exception.CustomizedProductIdExistsException;
import util.exception.CustomizedProductNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.PantsMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author meganyee
 */
@Stateless
public class CustomizedPantsSessionBean implements CustomizedPantsSessionBeanLocal {

    @EJB
    private FabricSessionBeanLocal fabricSessionBeanLocal;
    
    @EJB
    private PantsCuttingSessionBeanLocal pantsCuttingSessionBeanLocal;
    
    @EJB
    private PantsMeasurementSessionBeanLocal pantsMeasurementSessionBeanLocal;
    
    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CustomizedPantsSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewCustomizedPants(CustomizedPantsEntity newCustomizedPants, Long fabricId, Long pantsCuttingId, Long pantsMeasurementId) throws CustomizedProductIdExistsException, PantsMeasurementNotFoundException, CustomizationNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<CustomizedPantsEntity>> constraintViolations = validator.validate(newCustomizedPants);
        
        if (constraintViolations.isEmpty()) {
            try {
       
                FabricEntity fabric = fabricSessionBeanLocal.retrieveFabricById(fabricId);
                PantsCuttingEntity pantsCutting = pantsCuttingSessionBeanLocal.retrievePantsCuttingById(pantsCuttingId);
                PantsMeasurementEntity pantsMeasurement = pantsMeasurementSessionBeanLocal.retrievePantsMeasurementByPantsMeasurementId(pantsMeasurementId);
                
                newCustomizedPants.setFabric(fabric);
                newCustomizedPants.setPantsCutting(pantsCutting);
                newCustomizedPants.setPantsMeasurement(pantsMeasurement);
                
                em.persist(newCustomizedPants);
                em.flush();
               
                return newCustomizedPants.getProductId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomizedProductIdExistsException();
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
    public List<CustomizedPantsEntity> retrieveAllCustomizedPants() {
        Query query = em.createQuery("SELECT c FROM CustomizedPantsEntity c");
        return query.getResultList();
    }
    
    @Override
    public CustomizedPantsEntity retrieveCustomizedPantsById(Long productId) throws CustomizedProductNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomizedPantsEntity c WHERE c.productId = :productId");
        query.setParameter("productId", productId);
        try {
            return (CustomizedPantsEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomizedProductNotFoundException("Customized Pants " + productId + " does not exists!");
        }
    }
    
    @Override
    public void updateCustomizedPants(CustomizedPantsEntity updatedPants) throws CustomizedProductNotFoundException, UpdateEntityException, InputDataValidationException {
        if (updatedPants != null && updatedPants.getProductId() != null) {
            Set<ConstraintViolation<CustomizedPantsEntity>> constraintViolations = validator.validate(updatedPants);
            
            if (constraintViolations.isEmpty()) {
                CustomizedPantsEntity pantsToUpdate = retrieveCustomizedPantsById(updatedPants.getProductId());
                
                if (updatedPants.getProductId().equals(pantsToUpdate.getProductId())) {
                    pantsToUpdate.setName(updatedPants.getName());
                    pantsToUpdate.setDescription(updatedPants.getDescription());
                    pantsToUpdate.setImage(updatedPants.getImage());
                    pantsToUpdate.setBasePrice(updatedPants.getBasePrice());
                    pantsToUpdate.setTotalPrice(updatedPants.getTotalPrice());
                    pantsToUpdate.setGender(updatedPants.getGender());
                } else {
                    throw new UpdateEntityException("Id does not match!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomizedProductNotFoundException("Custiomized Pants Id cannot be found");
        }
    } 
    
    @Override
    public void deleteCustomizedPants(Long productId) throws CustomizedProductNotFoundException, DeleteEntityException {
        CustomizedPantsEntity pantsToRemove = retrieveCustomizedPantsById(productId);
        
        Query query = em.createQuery("SELECT o FROM OrderLineEntity o WHERE o.productEntity.productId = :productId");
        query.setParameter("productId", pantsToRemove.getProductId());
        
        if(query.getResultList().isEmpty()) {
            pantsToRemove.setPantsCutting(null);
            pantsToRemove.setFabric(null);
            pantsToRemove.setPantsMeasurement(null);
            
            em.remove(pantsToRemove);
        } else {
            throw new DeleteEntityException("Customized Pants ID " + productId + " is associated with existing order(s) and cannot be deleted!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomizedPantsEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
