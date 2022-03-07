/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.JacketMeasurementEntity;
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
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.JacketMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Stateless
public class JacketMeasurementSessionBean implements JacketMeasurementSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    
   private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public JacketMeasurementSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public Long createNewJacketMeasurement(JacketMeasurementEntity newJacketMeasurementEntity, Long customerId) throws UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<JacketMeasurementEntity>>constraintViolations = validator.validate(newJacketMeasurementEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                CustomerEntity customer = customerSessionBean.retreiveCustomerByCustomerId(customerId); //think this will need catch exception after method created
                customer.setJacketMeasurement(newJacketMeasurementEntity);
                em.persist(newJacketMeasurementEntity);
                em.flush();

                return newJacketMeasurementEntity.getJacketMeasurementId();
                
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    

    @Override
    public List<JacketMeasurementEntity> retrieveAllJacketMeasurements()
    {
        Query query = em.createQuery("SELECT j FROM JacketMeasurementEntity j");
        List<JacketMeasurementEntity> jacketMeasurementEntities = query.getResultList();
        return jacketMeasurementEntities;
    }
    
    
    @Override
    public JacketMeasurementEntity retrieveJacketMeasurementByJacketMeasurementId(Long jacketMeasurementId) throws JacketMeasurementNotFoundException
    {
        JacketMeasurementEntity jacketMeasurementEntity = em.find(JacketMeasurementEntity.class, jacketMeasurementId);
        
        if(jacketMeasurementEntity != null)
        {
            return jacketMeasurementEntity;
        }
        else
        {
            throw new JacketMeasurementNotFoundException("JacketMeasurement ID " + jacketMeasurementId + " does not exist!");
        }               
    }
    

    @Override
    public void updateJacketMeasurement(JacketMeasurementEntity jacketMeasurementEntity) throws InputDataValidationException, JacketMeasurementNotFoundException, UpdateEntityException
    {
        Set<ConstraintViolation<JacketMeasurementEntity>>constraintViolations = validator.validate(jacketMeasurementEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(jacketMeasurementEntity.getJacketMeasurementId()!= null)
            {
                JacketMeasurementEntity jacketMeasurementEntityToUpdate = retrieveJacketMeasurementByJacketMeasurementId(jacketMeasurementEntity.getJacketMeasurementId());
                jacketMeasurementEntityToUpdate.setNeck(jacketMeasurementEntity.getNeck());
                jacketMeasurementEntityToUpdate.setFrontLength(jacketMeasurementEntity.getFrontLength());
                jacketMeasurementEntityToUpdate.setChestGirth(jacketMeasurementEntity.getChestGirth());
                jacketMeasurementEntityToUpdate.setFrontChestWidth(jacketMeasurementEntity.getFrontChestWidth());
                jacketMeasurementEntityToUpdate.setUpperWaistGrith(jacketMeasurementEntity.getUpperWaistGrith());
                jacketMeasurementEntityToUpdate.setHipGirth(jacketMeasurementEntity.getHipGirth());
                jacketMeasurementEntityToUpdate.setArmhole(jacketMeasurementEntity.getArmhole());
                jacketMeasurementEntityToUpdate.setShoulderWidth(jacketMeasurementEntity.getShoulderWidth());
                jacketMeasurementEntityToUpdate.setSleeveLength(jacketMeasurementEntity.getSleeveLength());
                jacketMeasurementEntityToUpdate.setBackwidth(jacketMeasurementEntity.getBackwidth());
                jacketMeasurementEntityToUpdate.setBicepGirth(jacketMeasurementEntity.getBicepGirth());
                jacketMeasurementEntityToUpdate.setForearmGirth(jacketMeasurementEntity.getForearmGirth());
                jacketMeasurementEntityToUpdate.setWristGirth(jacketMeasurementEntity.getWristGirth());
            }
            else
            {
                throw new JacketMeasurementNotFoundException("JacketMeasurement ID not provided for jacketMeasurement to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    @Override
    public void deleteJacketMeasurement(Long jacketMeasurementId) throws JacketMeasurementNotFoundException, DeleteEntityException {
        JacketMeasurementEntity jacketMeasurementEntityToRemove = retrieveJacketMeasurementByJacketMeasurementId(jacketMeasurementId);
        Query query = em.createQuery("SELECT c FROM CustomizedJacketEntity c WHERE c.jacketMeasurement.jacketMeasurementId =:jacketId");
        query.setParameter("jacketId", jacketMeasurementId);
        if (query.getResultList().isEmpty()) {
            Query secondQuery = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.jacketMeasurement.jacketMeasurementId =:id");
            secondQuery.setParameter("id", jacketMeasurementId);
            CustomerEntity customer = (CustomerEntity) secondQuery.getSingleResult();
            customer.setJacketMeasurement(null);
            em.remove(jacketMeasurementEntityToRemove);
        }
        else {
            throw new DeleteEntityException("Unable to Delete Jacket Measurement because there is/are Customized Jacket using the measurements");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<JacketMeasurementEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    
}
