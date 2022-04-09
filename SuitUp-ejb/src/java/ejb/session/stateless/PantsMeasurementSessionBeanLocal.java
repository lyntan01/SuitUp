/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PantsMeasurementEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.PantsMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Local
public interface PantsMeasurementSessionBeanLocal {

    public Long createNewPantsMeasurement(PantsMeasurementEntity newPantsMeasurementEntity, Long customerId) throws UnknownPersistenceException, InputDataValidationException, CustomerNotFoundException;

    public List<PantsMeasurementEntity> retrieveAllPantsMeasurements();

    public PantsMeasurementEntity retrievePantsMeasurementByPantsMeasurementId(Long pantsMeasurementId) throws PantsMeasurementNotFoundException;

    public void updatePantsMeasurement(PantsMeasurementEntity pantsMeasurementEntity) throws InputDataValidationException, PantsMeasurementNotFoundException, UpdateEntityException, UnknownPersistenceException, CustomerNotFoundException;

    public void deletePantsMeasurement(Long pantsMeasurementId) throws PantsMeasurementNotFoundException, DeleteEntityException;
    
}
