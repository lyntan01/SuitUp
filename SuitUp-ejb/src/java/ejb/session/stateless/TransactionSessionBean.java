/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.StandardProductEntity;
import entity.TransactionEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import util.exception.AppointmentNotFoundException;
import util.exception.CreateNewTransactionException;
import util.exception.InputDataValidationException;
import util.exception.OrderNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.VoidTransactionException;

/**
 *
 * @author xianhui
 */
@Stateless
public class TransactionSessionBean implements TransactionSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    @EJB
    private AppointmentSessionBean appointmentSessionBeanLocal;
    @EJB
    private OrderSessionBean orderSessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TransactionSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewTransaction(TransactionEntity newTransactionEntity, Long appointmentId, Long orderId) throws AppointmentNotFoundException, OrderNotFoundException, UnknownPersistenceException, InputDataValidationException, CreateNewTransactionException {
        Set<ConstraintViolation<TransactionEntity>> constraintViolations = validator.validate(newTransactionEntity);

        if (constraintViolations.isEmpty()) {
            try {
                if ((orderId != null && appointmentId != null) || (orderId == null && appointmentId == null)) {
                    throw new CreateNewTransactionException("Unable to create transaction because transaction cannot be created due to both Appointment and Order");
                } else if (orderId != null) {
                    OrderEntity order = orderSessionBeanLocal.retrieveOrderByOrderId(orderId); //think this will need catch exception after method created
                    order.setTransaction(newTransactionEntity);
                    newTransactionEntity.setOrder(order);
                } else if (appointmentId != null) {
                    AppointmentEntity appointment = appointmentSessionBeanLocal.retrieveAppointmentByAppointmentId(appointmentId); //think this will need catch exception after method created
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
            } catch (AppointmentNotFoundException ex) {
                throw new AppointmentNotFoundException("Appointment ID " + appointmentId + " does not exist!");
            } catch (OrderNotFoundException ex) {
                throw new OrderNotFoundException("Order ID " + orderId + " does not exist!");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<TransactionEntity> retrieveAllTransactions() {
        Query query = em.createQuery("SELECT t FROM TransactionEntity t");
        List<TransactionEntity> transactionEntities = query.getResultList();
        for (TransactionEntity transactionEntity : transactionEntities) {
            transactionEntity.getAppointment();
            transactionEntity.getOrder();
        }
        return transactionEntities;
    }

    @Override
    public TransactionEntity retrieveTransactionByTransactionId(Long transactionId) throws TransactionNotFoundException {
        TransactionEntity transactionEntity = em.find(TransactionEntity.class, transactionId);

        if (transactionEntity != null) {
            transactionEntity.getAppointment();
            transactionEntity.getOrder();
            return transactionEntity;
        } else {
            throw new TransactionNotFoundException("Transaction ID " + transactionId + " does not exist!");
        }
    }

    //cannot delete transaction, can only void -> note: can unvoid ??
    @Override
    public void voidTransaction(Long transactionId) throws VoidTransactionException {
        try {
            TransactionEntity transactionEntity = retrieveTransactionByTransactionId(transactionId);
            if (transactionEntity.getOrder() != null && transactionEntity.getVoidRefund().equals(Boolean.FALSE)) {
                for (OrderLineItemEntity orderLineItem : transactionEntity.getOrder().getOrderLineItems()) {
                    if (orderLineItem.getProduct() instanceof StandardProductEntity) {
                        StandardProductEntity standardProduct = (StandardProductEntity) orderLineItem.getProduct();
                        standardProduct.setQuantityInStock(standardProduct.getQuantityInStock() + orderLineItem.getQuantity());
                    }
                }
                transactionEntity.setVoidRefund(Boolean.TRUE);
            }
            else if(transactionEntity.getVoidRefund().equals(Boolean.FALSE)) {
                throw new VoidTransactionException("Transaciton was already voided");
            }
        } catch (TransactionNotFoundException ex) {
            throw new VoidTransactionException("Error when voiding transaction: " + ex.getMessage());
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TransactionEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
