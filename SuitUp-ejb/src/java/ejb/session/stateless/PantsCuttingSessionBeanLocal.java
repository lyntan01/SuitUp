/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PantsCuttingEntity;
import java.util.List;
import javax.ejb.Local;
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
public interface PantsCuttingSessionBeanLocal {

    public Long createNewPantsCutting(PantsCuttingEntity newPantsCuttingEntity) throws CustomizationIdExistException, UnknownPersistenceException, InputDataValidationException;

    public List<PantsCuttingEntity> retrieveAllPantsCutting();

    public PantsCuttingEntity retrievePantsCuttingById(Long customizationId) throws CustomizationNotFoundException;

    public void updatePantsCutting(PantsCuttingEntity updatedPantsCutting) throws CustomizationNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deletePantsCutting(Long pantsCuttingId) throws CustomizationNotFoundException;
    
}
