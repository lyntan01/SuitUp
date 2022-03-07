/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.StoreEntity;
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
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.AppointmentNotFoundException;
import util.exception.CreateNewAppointmentException;
import util.exception.CustomerNotFoundException;
import util.exception.StoreNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author keithcharleschan
 */
@Stateless
public class AppointmentSessionBean implements AppointmentSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private StoreSessionBean storeSessionBeanLocal;

    @EJB
    private TransactionSessionBean transactionSessionBeanLocal;

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AppointmentSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewAppointment(AppointmentEntity newAppointmentEntity, Long storeId, Long customerId) throws CreateNewAppointmentException, StoreNotFoundException, CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<AppointmentEntity>> constraintViolations = validator.validate(newAppointmentEntity);

        if (constraintViolations.isEmpty()) {
            try {

                if (storeId == null) {
                    throw new CreateNewAppointmentException("The appointment must be associated with a store");
                }

                StoreEntity storeEntity = storeSessionBeanLocal.retrieveStoreByStoreId(storeId);
                storeEntity.getAppointments().add(newAppointmentEntity);

                if (customerId == null) {
                    throw new CreateNewAppointmentException("The appointment must be associated with a customer");
                }

                CustomerEntity customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                customerEntity.getAppointments().add(newAppointmentEntity);

                newAppointmentEntity.setStore(storeEntity);
                newAppointmentEntity.setCustomer(customerEntity);

                em.persist(newAppointmentEntity);
                em.flush();

                return newAppointmentEntity.getAppointmentId();
            } catch (StoreNotFoundException ex) {
                throw new StoreNotFoundException("Store ID " + storeId + " does not exist!");
            } catch (CustomerNotFoundException ex) {
                throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<AppointmentEntity> retrieveAllAppointments() {
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a");

        return query.getResultList();
    }

    @Override
    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException {
        AppointmentEntity appointmentEntity = em.find(AppointmentEntity.class, appointmentId);

        if (appointmentEntity != null) {
            return appointmentEntity;
        } else {
            throw new AppointmentNotFoundException("Appointment ID " + appointmentId + " does not exist!");
        }
    }

    @Override
    public void updateAppointment(AppointmentEntity appointmentEntity) throws AppointmentNotFoundException, UpdateEntityException, InputDataValidationException {
        if (appointmentEntity != null && appointmentEntity.getAppointmentId() != null) {
            Set<ConstraintViolation<AppointmentEntity>> constraintViolations = validator.validate(appointmentEntity);

            if (constraintViolations.isEmpty()) {
                AppointmentEntity appointmentEntityToUpdate = retrieveAppointmentByAppointmentId(appointmentEntity.getAppointmentId());

                if (appointmentEntityToUpdate.getAppointmentId().equals(appointmentEntity.getAppointmentId())) {
                    appointmentEntityToUpdate.setAppointmentDateTime(appointmentEntity.getAppointmentDateTime());
                    appointmentEntityToUpdate.setAppointmentTypeEnum(appointmentEntity.getAppointmentTypeEnum());
                } else {
                    throw new UpdateEntityException("Appointment ID of appointment record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new AppointmentNotFoundException("Appointment ID not provided for appointment to be updated");
        }
    }

    @Override
    public void deleteAppointment(Long appointmentId) throws AppointmentNotFoundException, DeleteEntityException {
        AppointmentEntity appointmentEntityToRemove = retrieveAppointmentByAppointmentId(appointmentId);

        if (appointmentEntityToRemove.getTransaction() == null) {
            appointmentEntityToRemove.getCustomer().getAppointments().remove(appointmentEntityToRemove);
            appointmentEntityToRemove.getStore().getAppointments().remove(appointmentEntityToRemove);
            appointmentEntityToRemove.setCustomer(null);
            appointmentEntityToRemove.setStore(null);
        } else {
            throw new DeleteEntityException("You can't delete an appointment that has a transaction associated with it!");
        }

        em.remove(appointmentEntityToRemove);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AppointmentEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
