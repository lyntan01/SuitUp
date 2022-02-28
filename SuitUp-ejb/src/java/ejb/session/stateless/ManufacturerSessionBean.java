/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ManufacturerEntity;
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
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.ManufacturerEmailExistException;
import util.exception.ManufacturerNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Stateless
public class ManufacturerSessionBean implements ManufacturerSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ManufacturerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewManufacturer(ManufacturerEntity newManufacturerEntity) throws ManufacturerEmailExistException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<ManufacturerEntity>>constraintViolations = validator.validate(newManufacturerEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newManufacturerEntity);
                em.flush();

                return newManufacturerEntity.getManufacturerId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new ManufacturerEmailExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<ManufacturerEntity> retrieveAllManufacturers()
    {
        Query query = em.createQuery("SELECT m FROM ManufacturerEntity m");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public ManufacturerEntity retrieveManufacturerByManufacturerId(Long manufacturerId) throws ManufacturerNotFoundException
    {
        ManufacturerEntity manufacturerEntity = em.find(ManufacturerEntity.class, manufacturerId);
        
        if(manufacturerEntity != null)
        {
            return manufacturerEntity;
        }
        else
        {
            throw new ManufacturerNotFoundException("Manufacturer ID " + manufacturerId + " does not exist!");
        }
    }
    
    
    
    @Override
    public ManufacturerEntity retrieveManufacturerByEmail(String email) throws ManufacturerNotFoundException
    {
        Query query = em.createQuery("SELECT m FROM ManufacturerEntity m WHERE m.email = :inEmail");
        query.setParameter("inEmail", email);
        
        try
        {
            return (ManufacturerEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new ManufacturerNotFoundException("Manufacturer Email " + email + " does not exist!");
        }
    }
    
    @Override
    public void updateManufacturer(ManufacturerEntity manufacturerEntity) throws ManufacturerNotFoundException, UpdateEntityException, InputDataValidationException
    {
        if(manufacturerEntity != null && manufacturerEntity.getManufacturerId() != null)
        {
            Set<ConstraintViolation<ManufacturerEntity>>constraintViolations = validator.validate(manufacturerEntity);
        
            if(constraintViolations.isEmpty())
            {
                ManufacturerEntity manufacturerEntityToUpdate = retrieveManufacturerByManufacturerId(manufacturerEntity.getManufacturerId());

                if(manufacturerEntityToUpdate.getEmail().equals(manufacturerEntity.getEmail()))
                {
                    manufacturerEntityToUpdate.setName(manufacturerEntity.getName());
                    manufacturerEntityToUpdate.setDescription(manufacturerEntity.getDescription());               
                    // Email deliberately NOT updated to demonstrate that client is not allowed to update account credential through this business method
                }
                else
                {
                    throw new UpdateEntityException("Username of manufacturer record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new ManufacturerNotFoundException("Manufacturer ID not provided for manufacturer to be updated");
        }
    }
    
    
    @Override
    public void deleteManufacturer(Long manufacturerId) throws ManufacturerNotFoundException, DeleteEntityException
    {
        ManufacturerEntity manufacturerEntityToRemove = retrieveManufacturerByManufacturerId(manufacturerId);
        manufacturerEntityToRemove.setFactoryAddress(null);
        em.remove(manufacturerEntityToRemove);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ManufacturerEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
