/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StoreEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.StoreNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Local
public interface StoreSessionBeanLocal {

    public Long createNewStore(StoreEntity newStoreEntity) throws UnknownPersistenceException, InputDataValidationException;

    public List<StoreEntity> retrieveAllStores();

    public StoreEntity retrieveStoreByStoreId(Long storeId) throws StoreNotFoundException;

    public void updateStore(StoreEntity storeEntity) throws StoreNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deleteStore(Long storeId) throws StoreNotFoundException, DeleteEntityException;
    
}
