/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomizedPantsEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomizationNotFoundException;
import util.exception.CustomizedProductIdExistsException;
import util.exception.CustomizedProductNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.PantsMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author meganyee
 */
@Local
public interface CustomizedPantsSessionBeanLocal {

    public Long createNewCustomizedPants(CustomizedPantsEntity newCustomizedPants, Long fabricId, Long pantsCuttingId, Long pantsMeasurementId) throws CustomizedProductIdExistsException, PantsMeasurementNotFoundException, CustomizationNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<CustomizedPantsEntity> retrieveAllCustomizedPants();

    public CustomizedPantsEntity retrieveCustomizedPantsById(Long productId) throws CustomizedProductNotFoundException;

    public void updateCustomizedPants(CustomizedPantsEntity updatedPants) throws CustomizedProductNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deleteCustomizedPants(Long productId) throws CustomizedProductNotFoundException, DeleteEntityException;

    public Long createNewCustomizedPants(CustomizedPantsEntity newCustomizedPants) throws CustomizedProductIdExistsException, PantsMeasurementNotFoundException, CustomizationNotFoundException, UnknownPersistenceException, InputDataValidationException;
    
}
