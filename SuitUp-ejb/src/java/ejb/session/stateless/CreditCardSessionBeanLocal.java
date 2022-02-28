/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreditCardNotFoundException;
import util.exception.CreditCardNumberExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Local
public interface CreditCardSessionBeanLocal {

    public Long createNewCreditCard(CreditCardEntity newCreditCardEntity) throws CreditCardNumberExistException, UnknownPersistenceException, InputDataValidationException;

    public List<CreditCardEntity> retrieveAllCreditCards();

    public CreditCardEntity retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException;

    public CreditCardEntity retrieveCreditCardByCreditCardNumber(String ccNumber) throws CreditCardNotFoundException;

    public void updateCreditCard(CreditCardEntity creditCardEntity) throws CreditCardNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deleteCreditCard(Long creditCardId) throws CreditCardNotFoundException;
    
}
