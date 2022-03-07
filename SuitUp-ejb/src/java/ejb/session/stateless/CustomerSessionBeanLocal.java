/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ChangePasswordException;
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author keithcharleschan
 */
@Local
public interface CustomerSessionBeanLocal {

    public Long createNewCustomer(CustomerEntity newCustomerEntity) throws CustomerEmailExistException, UnknownPersistenceException, InputDataValidationException;

    public List<CustomerEntity> retrieveAllCustomers();

    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException;

    public void updateCustomer(CustomerEntity customerEntity) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException;

    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteEntityException;

    public void customerChangePassword(String email, String oldPassword, String newPassword) throws ChangePasswordException, InvalidLoginCredentialException;
    
}
