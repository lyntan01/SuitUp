/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomizedJacketEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomizationNotFoundException;
import util.exception.CustomizedProductIdExistsException;
import util.exception.CustomizedProductNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.JacketMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author meganyee
 */
@Local
public interface CustomizedJacketSessionBeanLocal {

    public Long createNewFabric(CustomizedJacketEntity newCustomizedJacket, Long pocketStyleId, Long jacketStyleId, Long innerFabricId, Long outerFabricId, Long jacketMeasurementId) throws CustomizedProductIdExistsException, JacketMeasurementNotFoundException, CustomizationNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<CustomizedJacketEntity> retrieveAllCustomizedJackets();

    public CustomizedJacketEntity retrieveCustomizedJacketById(Long productId) throws CustomizedProductNotFoundException;

    public void updateCustomizedJacket(CustomizedJacketEntity updatedJacket) throws CustomizedProductNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deleteCustomizedJacket(Long productId) throws CustomizedProductNotFoundException, DeleteEntityException;
    
}
