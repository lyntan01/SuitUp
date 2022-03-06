/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StandardProductEntity;
import util.exception.StandardProductNotFoundException;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateStandardProductException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Local
public interface StandardProductSessionBeanLocal {

    public List<StandardProductEntity> retrieveAllStandardProducts();

    public Long createNewStandardProduct(StandardProductEntity newStandardProductEntity, Long categoryId, List<Long> tagsId) throws UnknownPersistenceException, InputDataValidationException, CreateStandardProductException;

    public StandardProductEntity retrieveStandardProductByStandardProductId(Long standardProductId) throws StandardProductNotFoundException;

    public void deleteStandardProduct(Long standardProductId) throws StandardProductNotFoundException, DeleteEntityException;

    public void updateStandardProduct(StandardProductEntity standardProductEntity, Long categoryId, List<Long> tagsId) throws StandardProductNotFoundException, UpdateEntityException, InputDataValidationException;
    
}
