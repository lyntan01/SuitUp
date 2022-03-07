/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransactionEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AppointmentNotFoundException;
import util.exception.CreateNewTransactionException;
import util.exception.InputDataValidationException;
import util.exception.OrderNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;
import util.exception.UpdateTransactionException;
import util.exception.VoidTransactionException;

/**
 *
 * @author xianhui
 */
@Local
public interface TransactionSessionBeanLocal {

    public Long createNewTransaction(TransactionEntity newTransactionEntity, Long appointmentId, Long orderId) throws AppointmentNotFoundException, OrderNotFoundException, UnknownPersistenceException, InputDataValidationException, CreateNewTransactionException;

    public List<TransactionEntity> retrieveAllTransactions();

    public TransactionEntity retrieveTransactionByTransactionId(Long transactionId) throws TransactionNotFoundException;

    public void voidTransaction(Long transactionId) throws VoidTransactionException;
    
}
