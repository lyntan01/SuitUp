/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AddressNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Local
public interface AddressSessionBeanLocal {

    public Long createNewAddress(AddressEntity newAddressEntity, Long customerId) throws CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<AddressEntity> retrieveAllAddresss();

    public AddressEntity retrieveAddressByAddressId(Long addressId) throws AddressNotFoundException;

    public void updateAddress(AddressEntity addressEntity) throws AddressNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deleteAddress(Long addressId) throws AddressNotFoundException, DeleteEntityException;
    
}
