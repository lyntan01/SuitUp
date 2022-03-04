/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
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
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateStaffException;
import util.security.CryptographicHelper;

/**
 *
 * @author lyntan
 */
@Stateless
public class StaffSessionBean implements StaffSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StaffSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewStaff(StaffEntity newStaffEntity) throws StaffUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<StaffEntity>> constraintViolations = validator.validate(newStaffEntity);

        if (constraintViolations.isEmpty()) {
            try {
                entityManager.persist(newStaffEntity);
                entityManager.flush();

                return newStaffEntity.getStaffId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new StaffUsernameExistException();
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
    public List<StaffEntity> retrieveAllStaffs() {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s");

        return query.getResultList();
    }

    @Override
    public StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException {
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, staffId);

        if (staffEntity != null) {
            return staffEntity;
        } else {
            throw new StaffNotFoundException("Staff ID " + staffId + " does not exist!");
        }
    }

    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (StaffEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
    }

    // Updated in v4.5 to include password hashing
    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            StaffEntity staffEntity = retrieveStaffByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + staffEntity.getSalt()));

            if (staffEntity.getPassword().equals(passwordHash)) {
                return staffEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (StaffNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    // Updated in v4.1 to update selective attributes instead of merging the entire state passed in from the client
    // Also check for existing staff before proceeding with the update
    // Updated in v4.2 with bean validation
    @Override
    public void updateStaff(StaffEntity staffEntity) throws StaffNotFoundException, UpdateStaffException, InputDataValidationException {
        if (staffEntity != null && staffEntity.getStaffId() != null) {
            Set<ConstraintViolation<StaffEntity>> constraintViolations = validator.validate(staffEntity);

            if (constraintViolations.isEmpty()) {
                StaffEntity staffEntityToUpdate = retrieveStaffByStaffId(staffEntity.getStaffId());

                if (staffEntityToUpdate.getUsername().equals(staffEntity.getUsername())) {
                    staffEntityToUpdate.setFirstName(staffEntity.getFirstName());
                    staffEntityToUpdate.setLastName(staffEntity.getLastName());
                    staffEntityToUpdate.setAccessRightEnum(staffEntity.getAccessRightEnum());
                    // Username and password are deliberately NOT updated to demonstrate that client is not allowed to update account credential through this business method
                } else {
                    throw new UpdateStaffException("Username of staff record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new StaffNotFoundException("Staff ID not provided for staff to be updated");
        }
    }

    @Override
    public void deleteStaff(Long staffId) throws StaffNotFoundException {
        StaffEntity staffEntityToRemove = retrieveStaffByStaffId(staffId);

        // Disassociate staff and store
        staffEntityToRemove.getStore().getStaff().remove(staffEntityToRemove);
        staffEntityToRemove.setStore(null);

        entityManager.remove(staffEntityToRemove);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<StaffEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
