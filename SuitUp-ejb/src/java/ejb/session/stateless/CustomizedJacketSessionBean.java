/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomizedJacketEntity;
import entity.FabricEntity;
import entity.JacketMeasurementEntity;
import entity.JacketStyleEntity;
import entity.PocketStyleEntity;
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
import util.exception.JacketMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author meganyee
 */
@Stateless
public class CustomizedJacketSessionBean implements CustomizedJacketSessionBeanLocal {
    
    @EJB
    private PocketStyleSessionBeanLocal pocketStyleSessionBeanLocal;
    
    @EJB
    private JacketStyleSessionBeanLocal jacketStyleSessionBeanLocal;
    
    @EJB
    private FabricSessionBeanLocal fabricSessionBeanLocal;
    
    @EJB
    private JacketMeasurementSessionBeanLocal jacketMeasurementSessionBeanLocal;

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CustomizedJacketSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewCustomizedJacket(CustomizedJacketEntity newCustomizedJacket, Long pocketStyleId, Long jacketStyleId, Long innerFabricId, Long outerFabricId, Long jacketMeasurementId) throws CustomizedProductIdExistsException, JacketMeasurementNotFoundException, CustomizationNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<CustomizedJacketEntity>> constraintViolations = validator.validate(newCustomizedJacket);
        
        if (constraintViolations.isEmpty()) {
            try {
                PocketStyleEntity pocketStyle = pocketStyleSessionBeanLocal.retrievePocketStyleById(pocketStyleId);
                JacketStyleEntity jacketStyle = jacketStyleSessionBeanLocal.retrieveJacketStyleById(jacketStyleId);
                FabricEntity innerFabric = fabricSessionBeanLocal.retrieveFabricById(innerFabricId);
                FabricEntity outerFabric = fabricSessionBeanLocal.retrieveFabricById(outerFabricId);
                JacketMeasurementEntity jacketMeasurement = jacketMeasurementSessionBeanLocal.retrieveJacketMeasurementByJacketMeasurementId(jacketMeasurementId);
                
                newCustomizedJacket.setPocketStyle(pocketStyle);
                newCustomizedJacket.setJacketStyle(jacketStyle);
                newCustomizedJacket.setInnerFabric(innerFabric);
                newCustomizedJacket.setOuterFabric(outerFabric);
                newCustomizedJacket.setJacketMeasurement(jacketMeasurement);
                
                em.persist(newCustomizedJacket);
                em.flush();
               
                return newCustomizedJacket.getProductId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomizedProductIdExistsException("Customized Product ID already exists.");
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
    public List<CustomizedJacketEntity> retrieveAllCustomizedJackets() {
        Query query = em.createQuery("SELECT c FROM CustomizedJacketEntity c");
        return query.getResultList();
    }
    
    @Override
    public CustomizedJacketEntity retrieveCustomizedJacketById(Long productId) throws CustomizedProductNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomizedJacketEntity c WHERE c.productId = :productId");
        query.setParameter("productId", productId);
        try {
            return (CustomizedJacketEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomizedProductNotFoundException("Customized Jacket " + productId + " does not exists!");
        }
    }
    
    @Override
    public void updateCustomizedJacket(CustomizedJacketEntity updatedJacket) throws CustomizedProductNotFoundException, UpdateEntityException, InputDataValidationException {
        if (updatedJacket != null && updatedJacket.getProductId() != null) {
            Set<ConstraintViolation<CustomizedJacketEntity>> constraintViolations = validator.validate(updatedJacket);
            
            if (constraintViolations.isEmpty()) {
                CustomizedJacketEntity jacketToUpdate = retrieveCustomizedJacketById(updatedJacket.getProductId());
                
                if (jacketToUpdate.getProductId().equals(updatedJacket.getProductId())) {
                    jacketToUpdate.setName(updatedJacket.getName());
                    jacketToUpdate.setDescription(updatedJacket.getDescription());
                    jacketToUpdate.setImage(updatedJacket.getImage());
                    jacketToUpdate.setBasePrice(updatedJacket.getBasePrice());
                    jacketToUpdate.setTotalPrice(updatedJacket.getTotalPrice());
                    jacketToUpdate.setGender(updatedJacket.getGender());
                } else {
                    throw new UpdateEntityException("Id does not match!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomizedProductNotFoundException("Custiomized Jacket Id cannot be found");
        }
    } 
    
    @Override
    public void deleteCustomizedJacket(Long productId) throws CustomizedProductNotFoundException, DeleteEntityException {
        CustomizedJacketEntity jacketToRemove = retrieveCustomizedJacketById(productId);
        
        Query query = em.createQuery("SELECT o FROM OrderLineEntity o WHERE o.productEntity.productId = :productId");
        query.setParameter("productId", jacketToRemove.getProductId());
        
        if(query.getResultList().isEmpty()) {
            jacketToRemove.setPocketStyle(null);
            jacketToRemove.setJacketStyle(null);
            jacketToRemove.setInnerFabric(null);
            jacketToRemove.setOuterFabric(null);
            jacketToRemove.setJacketMeasurement(null);
            
            em.remove(jacketToRemove);
        } else {
            throw new DeleteEntityException("Customized Jacket ID " + productId + " is associated with existing order(s) and cannot be deleted!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomizedJacketEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
