/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.AppointmentEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
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
import util.exception.ChangePasswordException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerEmailExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;
import util.security.CryptographicHelper;

/**
 *
 * @author keithcharleschan
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewCustomer(CustomerEntity newCustomerEntity) throws CustomerEmailExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<CustomerEntity>> constraintViolations = validator.validate(newCustomerEntity);

        if (constraintViolations.isEmpty()) {
            try {
                entityManager.persist(newCustomerEntity);
                entityManager.flush();

                return newCustomerEntity.getCustomerId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CustomerEmailExistException();
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
    public List<CustomerEntity> retrieveAllCustomers() {
        Query query = entityManager.createQuery("SELECT c FROM CustomerEntity c");

        return query.getResultList();
    }

    @Override
    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = entityManager.find(CustomerEntity.class, customerId);

        if (customerEntity != null) {
            return customerEntity;
        } else {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
        }
    }

    @Override
    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = entityManager.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (CustomerEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Email " + email + " does not exist!");
        }
    }

    // Updated in v4.5 to include password hashing
    @Override
    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            CustomerEntity customerEntity = retrieveCustomerByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customerEntity.getSalt()));

            if (customerEntity.getPassword().equals(passwordHash)) {
                return customerEntity;
            } else {
                throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
        }
    }

    // Updated in v4.1 to update selective attributes instead of merging the entire state passed in from the client
    // Also check for existing customer before proceeding with the update
    // Updated in v4.2 with bean validation
    @Override
    public void updateCustomer(CustomerEntity customerEntity) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException {
        if (customerEntity != null && customerEntity.getCustomerId() != null) {
            Set<ConstraintViolation<CustomerEntity>> constraintViolations = validator.validate(customerEntity);

            if (constraintViolations.isEmpty()) {
                CustomerEntity customerEntityToUpdate = retrieveCustomerByCustomerId(customerEntity.getCustomerId());

                if (customerEntityToUpdate.getEmail().equals(customerEntity.getEmail())) {
                    customerEntityToUpdate.setFirstName(customerEntity.getFirstName());
                    customerEntityToUpdate.setLastName(customerEntity.getLastName());
                    //not sure where the updating of address /credit card / support tickets comes in
                    //pass in the Id from createAddress in AddressSessionBean and add it to the customer's existing list? (same for credit card)
                } else {
                    throw new UpdateCustomerException("Email of customer record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
        }
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntityToRemove = retrieveCustomerByCustomerId(customerId);

        //linked to quite a number of things not sure if its correct
        
  
        
        for (CreditCardEntity creditCard : customerEntityToRemove.getCreditCards()) {
            entityManager.remove(creditCard);
        }

        for (AddressEntity address : customerEntityToRemove.getAddresses()) {
            entityManager.remove(address);
        }

        for (AppointmentEntity appointment : customerEntityToRemove.getAppointments()) {
            entityManager.remove(appointment);
        }

        for (OrderEntity orderEntity : customerEntityToRemove.getOrders()) {
            
//                  if got transaction throw exception

            for (OrderLineItemEntity orderLineItem : orderEntity.getOrderLineItems()) {
                entityManager.remove(orderLineItem);
            }

            orderEntity.getOrderLineItems().clear();
            entityManager.remove(orderEntity);
        }

        customerEntityToRemove.getCreditCards().clear();
        customerEntityToRemove.getAddresses().clear();
        customerEntityToRemove.getAppointments().clear();
        customerEntityToRemove.getOrders().clear();

        entityManager.remove(customerEntityToRemove);
    }

    @Override
    public void customerChangePassword(String email, String oldPassword, String newPassword) throws ChangePasswordException, InvalidLoginCredentialException {

        try {
            CustomerEntity customerEntity = customerLogin(email, oldPassword);

            if (newPassword == null || newPassword.isEmpty()) {
                throw new ChangePasswordException("Please provide a password.");
            } else if (newPassword == oldPassword) {
                throw new ChangePasswordException("Please provide a different password from your previous password.");
            } else {
                customerEntity.setPassword(newPassword);
            }

        } catch (InvalidLoginCredentialException ex) {
            throw new InvalidLoginCredentialException("Invalid credentials!");
        }

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
