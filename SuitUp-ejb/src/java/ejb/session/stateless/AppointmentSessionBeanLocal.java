/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AppointmentNotFoundException;
import util.exception.CreateNewAppointmentException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.StoreNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author keithcharleschan
 */
@Local
public interface AppointmentSessionBeanLocal {

    public Long createNewAppointment(AppointmentEntity newAppointmentEntity, Long storeId, Long customerId) throws CreateNewAppointmentException, StoreNotFoundException, CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<AppointmentEntity> retrieveAllAppointments();

    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;

    public void updateAppointment(AppointmentEntity appointmentEntity, Long storeId) throws AppointmentNotFoundException, StoreNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deleteAppointment(Long appointmentId) throws AppointmentNotFoundException, DeleteEntityException;

}
