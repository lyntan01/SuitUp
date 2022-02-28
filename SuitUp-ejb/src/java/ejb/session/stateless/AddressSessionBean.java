/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.CustomerEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AddressNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Stateless
public class AddressSessionBean implements AddressSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AddressSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewAddress(AddressEntity newAddressEntity) throws UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<AddressEntity>> constraintViolations = validator.validate(newAddressEntity);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newAddressEntity);
                em.flush();

                return newAddressEntity.getAddressId();
            } catch (PersistenceException ex) {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<AddressEntity> retrieveAllAddresss() {
        Query query = em.createQuery("SELECT a FROM AddressEntity a");

        return query.getResultList();
    }

    @Override
    public AddressEntity retrieveAddressByAddressId(Long addressId) throws AddressNotFoundException {
        AddressEntity addressEntity = em.find(AddressEntity.class, addressId);

        if (addressEntity != null) {
            return addressEntity;
        } else {
            throw new AddressNotFoundException("Address ID " + addressId + " does not exist!");
        }
    }

    @Override
    public void updateAddress(AddressEntity addressEntity) throws AddressNotFoundException, UpdateEntityException, InputDataValidationException {
        if (addressEntity != null && addressEntity.getAddressId() != null) {
            Set<ConstraintViolation<AddressEntity>> constraintViolations = validator.validate(addressEntity);

            if (constraintViolations.isEmpty()) {
                AddressEntity addressEntityToUpdate = retrieveAddressByAddressId(addressEntity.getAddressId());

                if (addressEntityToUpdate.getAddressId().equals(addressEntity.getAddressId())) {
                    addressEntityToUpdate.setName(addressEntity.getName());
                    addressEntityToUpdate.setPhoneNumber(addressEntity.getPhoneNumber());
                    addressEntityToUpdate.setAddressLineOne(addressEntity.getAddressLineOne());
                    addressEntityToUpdate.setAddressLineTwo(addressEntity.getAddressLineTwo());
                    addressEntityToUpdate.setPostalCode(addressEntity.getPostalCode());
                } else {
                    throw new UpdateEntityException("Address ID of address record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new AddressNotFoundException("Address ID not provided for address to be updated");
        }
    }

    @Override
    public void deleteAddress(Long addressId) throws AddressNotFoundException, DeleteEntityException {
        AddressEntity addressEntityToRemove = retrieveAddressByAddressId(addressId);
        boolean isDeliveryAddress = em.createQuery("SELECT o FROM OrderEntity o WHERE o.deliveryAddress.addressId = :inAddressId")
                                      .setParameter("inAddressId", addressId)
                                      .getResultList()
                                      .size() > 0;
        boolean isFactoryAddress = em.createQuery("SELECT m FROM ManufacturerEntity m WHERE m.factoryAddress.addressId = :inAddressId")
                                     .setParameter("inAddressId", addressId)
                                     .getResultList()
                                     .size() > 0;
        boolean isStoreAddress = em.createQuery("SELECT s FROM StoreEntity s WHERE s.address.addressId = :inAddressId")
                                    .setParameter("inAddressId", addressId)
                                    .getResultList()
                                    .size() > 0;

        if(!isDeliveryAddress && !isFactoryAddress && !isStoreAddress) // Address is only a customer address not linked to any order yet
        {
            // Disassociation
            CustomerEntity customerEntity = (CustomerEntity) em.createNamedQuery("findCustomerByAddressId")
                                                               .setParameter("inAddressId", addressId)
                                                               .getSingleResult();
            customerEntity.getAddresses().remove(addressEntityToRemove);
            em.remove(addressEntityToRemove);
        }
        else if (isDeliveryAddress)
        {
            // Prevent deleting addresss linked to existing order(s)
            throw new DeleteEntityException("Address ID " + addressId + " is associated with existing order(s) and cannot be deleted!");
        }
        else if (isFactoryAddress)
        {
            // Prevent deleting addresss linked to existing factory(s)
            throw new DeleteEntityException("Address ID " + addressId + " is associated with existing manufacturer(s) and cannot be deleted!");
        }
        else if (isStoreAddress)
        {
            // Prevent deleting addresss linked to existing store(s)
            throw new DeleteEntityException("Address ID " + addressId + " is associated with existing store(s) and cannot be deleted!");
        }
        
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AddressEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
