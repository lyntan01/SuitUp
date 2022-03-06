/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.ProductEntity;
import entity.StandardProductEntity;
import entity.TransactionEntity;
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
import util.exception.CreateNewTransactionException;
import util.exception.InputDataValidationException;
import util.exception.TransactionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;
import util.exception.UpdateTransactionException;

/**
 *
 * @author xianhui
 */
@Stateless
public class TransactionSessionBean implements TransactionSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    @EJB
    private AppointmentSessionBeanLocal appointmentSessionBean;
    @EJB
    private OrderSessionBeanLocal orderSessionBean;
    
   private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public TransactionSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public Long createNewTransaction(TransactionEntity newTransactionEntity, Long appointmentId, Long orderId) throws UnknownPersistenceException, InputDataValidationException, CreateNewTransactionException
    {
        Set<ConstraintViolation<TransactionEntity>>constraintViolations = validator.validate(newTransactionEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                if((orderId!=null && appointmentId != null) || (orderId==null && appointmentId == null)) {
                    throw new CreateNewTransactionException("Unable to create transaction because transaction cannot be created due to both Appointment and Order");
                } else if (orderId != null) {
                    OrderEntity order = orderSessionBean.retreiveOrderByOrderId(orderId); //think this will need catch exception after method created
                    order.setTransaction(newTransactionEntity);
                    newTransactionEntity.setOrder(order);
                } else if (appointmentId != null) {
                    AppointmentEntity appointment = appointmentSessionBean.retreiveAppointmentByAppointmentId(appointmentId); //think this will need catch exception after method created
                    appointment.setTransaction(newTransactionEntity);
                    newTransactionEntity.setAppointment(appointment);
                } else {
                    throw new CreateNewTransactionException("Unable to create transaction because transaction is not linked to either Appointment or Order");
                }

                em.persist(newTransactionEntity);
                em.flush();

                return newTransactionEntity.getTransactionId();
                
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    

    @Override
    public List<TransactionEntity> retrieveAllTransactions()
    {
        Query query = em.createQuery("SELECT t FROM TransactionEntity t");
        List<TransactionEntity> transactionEntities = query.getResultList();
        for(TransactionEntity transactionEntity: transactionEntities) {
            transactionEntity.getAppointment();
            transactionEntity.getOrder();
        }
        return transactionEntities;
    }
    
    
    @Override
    public TransactionEntity retrieveTransactionByTransactionId(Long transactionId) throws TransactionNotFoundException
    {
        TransactionEntity transactionEntity = em.find(TransactionEntity.class, transactionId);
        
        if(transactionEntity != null)
        {
            transactionEntity.getAppointment();
            transactionEntity.getOrder();
            return transactionEntity;
        }
        else
        {
            throw new TransactionNotFoundException("Transaction ID " + transactionId + " does not exist!");
        }               
    }
    

    @Override
    public void updateTransaction(TransactionEntity transactionEntity) throws InputDataValidationException, TransactionNotFoundException, UpdateEntityException, UpdateTransactionException
    {
        Set<ConstraintViolation<TransactionEntity>>constraintViolations = validator.validate(transactionEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(transactionEntity.getTransactionId()!= null)
            {
                TransactionEntity transactionEntityToUpdate = retrieveTransactionByTransactionId(transactionEntity.getTransactionId());
                transactionEntityToUpdate.setTotalAmount(transactionEntity.getTotalAmount());
                transactionEntityToUpdate.setPaymentDate(transactionEntity.getPaymentDate());
                if(transactionEntityToUpdate.getVoidRefund().equals(false) && transactionEntity.getVoidRefund().equals(true)) {
                    voidTransaction(transactionEntityToUpdate.getTransactionId());
                }
                transactionEntityToUpdate.setVoidRefund(transactionEntity.getVoidRefund());
                if(!transactionEntityToUpdate.getAppointment().getAppointmentId().equals(transactionEntity.getAppointment().getAppointmentId()) && !transactionEntityToUpdate.getOrder().getOrderId().equals(transactionEntity.getOrder().getOrderId())) {
                    throw new UpdateTransactionException("Cannot update the initial Order/Appointment associated with Transaction");
                }
            }
            else
            {
                throw new TransactionNotFoundException("Transaction ID not provided for transaction to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    //cannot delete transaction, can only void 
    @Override
    public void voidTransaction(Long transactionId) throws TransactionNotFoundException {
        TransactionEntity transactionEntity = retrieveTransactionByTransactionId(transactionId);
        if(transactionEntity.getOrder()!=null) {
            for(OrderLineItemEntity orderLineItem: transactionEntity.getOrder().getOrderLineItems()) {
                if(orderLineItem.getProduct() instanceof StandardProductEntity) {
                    StandardProductEntity standardProduct = (StandardProductEntity) orderLineItem.getProduct();
                    standardProduct.setQuantityInStock(standardProduct.getQuantityInStock() + orderLineItem.getQuantity());   
                }
            }
        }
        transactionEntity.setVoidRefund(Boolean.TRUE);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TransactionEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    
}
