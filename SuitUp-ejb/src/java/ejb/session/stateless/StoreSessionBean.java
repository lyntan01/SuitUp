/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import entity.StoreEntity;
import java.util.List;
import java.util.Set;
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
import util.exception.StoreNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Stateless
public class StoreSessionBean implements StoreSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StoreSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewStore(StoreEntity newStoreEntity) throws UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<StoreEntity>> constraintViolations = validator.validate(newStoreEntity);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newStoreEntity);
                em.flush();

                return newStoreEntity.getStoreId();
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<StoreEntity> retrieveAllStores() {
        Query query = em.createQuery("SELECT m FROM StoreEntity m");

        return query.getResultList();
    }

    @Override
    public StoreEntity retrieveStoreByStoreId(Long storeId) throws StoreNotFoundException {
        StoreEntity storeEntity = em.find(StoreEntity.class, storeId);

        if (storeEntity != null) {
            return storeEntity;
        } else {
            throw new StoreNotFoundException("Store ID " + storeId + " does not exist!");
        }
    }

    @Override
    public void updateStore(StoreEntity storeEntity) throws StoreNotFoundException, UpdateEntityException, InputDataValidationException {
        if (storeEntity != null && storeEntity.getStoreId() != null) {
            Set<ConstraintViolation<StoreEntity>> constraintViolations = validator.validate(storeEntity);

            if (constraintViolations.isEmpty()) {
                StoreEntity storeEntityToUpdate = retrieveStoreByStoreId(storeEntity.getStoreId());

                if (storeEntityToUpdate.getStoreId().equals(storeEntity.getStoreId())) {
                    storeEntityToUpdate.setStoreName(storeEntity.getStoreName());
                    storeEntityToUpdate.setStoreDescription(storeEntity.getStoreDescription());
                    storeEntityToUpdate.setOpeningHour(storeEntity.getOpeningHour());
                    storeEntityToUpdate.setClosingHour(storeEntity.getClosingHour());
                    storeEntityToUpdate.setContactNumber(storeEntity.getContactNumber());
                } else {
                    throw new UpdateEntityException("Store ID of store record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new StoreNotFoundException("Store ID not provided for store to be updated");
        }
    }

    @Override
    public void deleteStore(Long storeId) throws StoreNotFoundException, DeleteEntityException {
        StoreEntity storeEntityToRemove = retrieveStoreByStoreId(storeId);
        
        if(storeEntityToRemove.getAppointments().isEmpty() && storeEntityToRemove.getStaff().isEmpty())
        {
            // Disassociation
            for (StaffEntity staff : storeEntityToRemove.getStaff()) {
                staff.setStore(null);
            }
            storeEntityToRemove.getStaff().clear();
            storeEntityToRemove.setAddress(null);
            em.remove(storeEntityToRemove);
        }
        else
        {
            // Prevent deleting stores with existing appointment(s) and/or staff
            throw new DeleteEntityException("Store ID " + storeId + " is associated with existing appointment(s) and/or staff and cannot be deleted!");
        }
        
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<StoreEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
