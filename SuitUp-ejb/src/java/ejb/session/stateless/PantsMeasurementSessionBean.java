/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.PantsMeasurementEntity;
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
import util.exception.PantsMeasurementNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Stateless
public class PantsMeasurementSessionBean implements PantsMeasurementSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PantsMeasurementSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public Long createNewPantsMeasurement(PantsMeasurementEntity newPantsMeasurementEntity, Long customerId) throws UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<PantsMeasurementEntity>>constraintViolations = validator.validate(newPantsMeasurementEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                CustomerEntity customer = customerSessionBean.retreiveCustomerByCustomerId(customerId); //think this will need catch exception after method created
                customer.setPantsMeasurement(newPantsMeasurementEntity);
                em.persist(newPantsMeasurementEntity);
                em.flush();

                return newPantsMeasurementEntity.getPantsMeasurementId();
                
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    

    @Override
    public List<PantsMeasurementEntity> retrieveAllPantsMeasurements()
    {
        Query query = em.createQuery("SELECT m FROM PantsMeasurementEntity m");
        List<PantsMeasurementEntity> pantsMeasurementEntities = query.getResultList();
        return pantsMeasurementEntities;
    }
    
    
    @Override
    public PantsMeasurementEntity retrievePantsMeasurementByPantsMeasurementId(Long pantsMeasurementId) throws PantsMeasurementNotFoundException
    {
        PantsMeasurementEntity pantsMeasurementEntity = em.find(PantsMeasurementEntity.class, pantsMeasurementId);
        
        if(pantsMeasurementEntity != null)
        {
            return pantsMeasurementEntity;
        }
        else
        {
            throw new PantsMeasurementNotFoundException("PantsMeasurement ID " + pantsMeasurementId + " does not exist!");
        }               
    }
    

    @Override
    public void updatePantsMeasurement(PantsMeasurementEntity pantsMeasurementEntity) throws InputDataValidationException, PantsMeasurementNotFoundException, UpdateEntityException
    {
        Set<ConstraintViolation<PantsMeasurementEntity>>constraintViolations = validator.validate(pantsMeasurementEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(pantsMeasurementEntity.getPantsMeasurementId()!= null)
            {
                PantsMeasurementEntity pantsMeasurementEntityToUpdate = retrievePantsMeasurementByPantsMeasurementId(pantsMeasurementEntity.getPantsMeasurementId());
                pantsMeasurementEntityToUpdate.setLegsLength(pantsMeasurementEntity.getLegsLength());
                pantsMeasurementEntityToUpdate.setLowerWaistGirth(pantsMeasurementEntity.getLowerWaistGirth());
                pantsMeasurementEntityToUpdate.setHipGirth(pantsMeasurementEntity.getHipGirth());
                pantsMeasurementEntityToUpdate.setCrotch(pantsMeasurementEntity.getCrotch());
                pantsMeasurementEntityToUpdate.setThighGirth(pantsMeasurementEntity.getThighGirth());
                pantsMeasurementEntityToUpdate.setKneeGirth(pantsMeasurementEntity.getKneeGirth());
                pantsMeasurementEntityToUpdate.setCalfGirth(pantsMeasurementEntity.getCalfGirth());
                pantsMeasurementEntityToUpdate.setPantsOpeningWidth(pantsMeasurementEntity.getPantsOpeningWidth());
            }
            else
            {
                throw new PantsMeasurementNotFoundException("PantsMeasurement ID not provided for pantsMeasurement to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    @Override
    public void deletePantsMeasurement(Long pantsMeasurementId) throws PantsMeasurementNotFoundException, DeleteEntityException {
        PantsMeasurementEntity pantsMeasurementEntityToRemove = retrievePantsMeasurementByPantsMeasurementId(pantsMeasurementId);
        Query query = em.createQuery("SELECT c FROM CustomizedPantsEntity c WHERE c.pantsMeasurement.pantsMeasurementId =:pantsId");
        query.setParameter("pantsId", pantsMeasurementId);
        if (query.getResultList().isEmpty()) {
            Query secondQuery = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.pantsMeasurement.pantsMeasurementId =:id");
            secondQuery.setParameter("id", pantsMeasurementId);
            CustomerEntity customer = (CustomerEntity) secondQuery.getSingleResult();
            customer.setPantsMeasurement(null);
            em.remove(pantsMeasurementEntityToRemove);
        }
        else {
            throw new DeleteEntityException("Unable to Delete Pants Measurement because there is/are Customized Pants using the measurements");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PantsMeasurementEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
