/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FabricEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ColourNotFoundException;
import util.exception.CustomizationIdExistException;
import util.exception.CustomizationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author meganyee
 */
@Local
public interface FabricSessionBeanLocal {

    public Long createNewFabric(FabricEntity newFabricEntity, Long colourId) throws CustomizationIdExistException, ColourNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<FabricEntity> retrieveAllFabrics();

    public FabricEntity retrieveFabricById(Long customizationId) throws CustomizationNotFoundException;

    public void updateFabric(FabricEntity updatedFabric) throws CustomizationNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deleteFabric(Long fabricId) throws CustomizationNotFoundException;
    
}
