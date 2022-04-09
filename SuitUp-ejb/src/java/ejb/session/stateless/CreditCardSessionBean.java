/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.CustomerEntity;
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
import util.exception.CreditCardNotFoundException;
import util.exception.CreditCardNumberExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;
    
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CreditCardSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewCreditCard(CreditCardEntity newCreditCardEntity, Long customerId) throws CreditCardNumberExistException, CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<CreditCardEntity>>constraintViolations = validator.validate(newCreditCardEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                CustomerEntity customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                
                em.persist(newCreditCardEntity);
                em.flush();
                
                customerEntity.getCreditCards().add(newCreditCardEntity);

                return newCreditCardEntity.getCreditCardId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreditCardNumberExistException("Credit card number already exists.");
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
    public List<CreditCardEntity> retrieveAllCreditCards()
    {
        Query query = em.createQuery("SELECT m FROM CreditCardEntity m");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public CreditCardEntity retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException
    {
        CreditCardEntity creditCardEntity = em.find(CreditCardEntity.class, creditCardId);
        
        if(creditCardEntity != null)
        {
            return creditCardEntity;
        }
        else
        {
            throw new CreditCardNotFoundException("Credit Card ID " + creditCardId + " does not exist!");
        }
    }
    
    
    
    @Override
    public CreditCardEntity retrieveCreditCardByCreditCardNumber(String ccNumber) throws CreditCardNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.cardNumber = :inCardNumber");
        query.setParameter("inCardNumber", ccNumber);
        
        try
        {
            return (CreditCardEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CreditCardNotFoundException("Credit Card Number " + ccNumber + " does not exist!");
        }
    }
    
    @Override
    public void updateCreditCard(CreditCardEntity creditCardEntity) throws CreditCardNotFoundException, UpdateEntityException, InputDataValidationException
    {
        if(creditCardEntity != null && creditCardEntity.getCreditCardId() != null)
        {
            Set<ConstraintViolation<CreditCardEntity>>constraintViolations = validator.validate(creditCardEntity);
        
            if(constraintViolations.isEmpty())
            {
                CreditCardEntity creditCardEntityToUpdate = retrieveCreditCardByCreditCardId(creditCardEntity.getCreditCardId());

                if(creditCardEntityToUpdate.getCardNumber().equals(creditCardEntity.getCardNumber()))
                {
                    creditCardEntityToUpdate.setHolderName(creditCardEntity.getHolderName());
                    creditCardEntityToUpdate.setExpiryDate(creditCardEntity.getExpiryDate());  
                    creditCardEntityToUpdate.setCvv(creditCardEntity.getCvv());
                    // Number deliberately NOT updated to demonstrate that client is not allowed to update account credential through this business method
                }
                else
                {
                    throw new UpdateEntityException("Credit card number of credit card record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new CreditCardNotFoundException("Credit Card ID not provided for credit card to be updated");
        }
    }
    
    
    @Override
    public void deleteCreditCard(Long creditCardId) throws CreditCardNotFoundException
    {
        CreditCardEntity creditCardEntityToRemove = retrieveCreditCardByCreditCardId(creditCardId);
        
        Query query = em.createNamedQuery("findCustomerByCreditCardId").setParameter("inCardId", creditCardId);
        CustomerEntity customerEntity = (CustomerEntity) query.getSingleResult();
        customerEntity.getCreditCards().remove(creditCardEntityToRemove);
        
        em.remove(creditCardEntityToRemove);

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CreditCardEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
