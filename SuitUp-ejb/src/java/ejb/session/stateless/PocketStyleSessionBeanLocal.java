/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PocketStyleEntity;
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
public interface PocketStyleSessionBeanLocal {
    
    public Long createNewPocketStyle(PocketStyleEntity newPocketStyleEntity) throws CustomizationIdExistException, UnknownPersistenceException, InputDataValidationException;
    
    public List<PocketStyleEntity> retrieveAllPocketStyles();
    
    public PocketStyleEntity retrievePocketStyleById(Long customizationId) throws CustomizationNotFoundException;
    
    public void updatePocketStyle(PocketStyleEntity updatedPocketStyle) throws CustomizationNotFoundException, UpdateEntityException, InputDataValidationException;
    
    public void deletePocketStyle(Long pocketStyleId) throws CustomizationNotFoundException;

}
