/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.JacketMeasurementEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.JacketMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Local
public interface JacketMeasurementSessionBeanLocal {

    public List<JacketMeasurementEntity> retrieveAllJacketMeasurements();

    public Long createNewJacketMeasurement(JacketMeasurementEntity newJacketMeasurementEntity, Long customerId) throws UnknownPersistenceException, InputDataValidationException;

    public JacketMeasurementEntity retrieveJacketMeasurementByJacketMeasurementId(Long jacketMeasurementId) throws JacketMeasurementNotFoundException;

    public void updateJacketMeasurement(JacketMeasurementEntity jacketMeasurementEntity) throws InputDataValidationException, JacketMeasurementNotFoundException, UpdateEntityException;

    public void deleteJacketMeasurement(Long jacketMeasurementId) throws JacketMeasurementNotFoundException, DeleteEntityException;
    
}
