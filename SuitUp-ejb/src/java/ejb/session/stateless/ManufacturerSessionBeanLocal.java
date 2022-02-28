/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ManufacturerEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.ManufacturerEmailExistException;
import util.exception.ManufacturerNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Local
public interface ManufacturerSessionBeanLocal {

    public Long createNewManufacturer(ManufacturerEntity newManufacturerEntity) throws ManufacturerEmailExistException, UnknownPersistenceException, InputDataValidationException;

    public List<ManufacturerEntity> retrieveAllManufacturers();

    public ManufacturerEntity retrieveManufacturerByManufacturerId(Long manufacturerId) throws ManufacturerNotFoundException;

    public ManufacturerEntity retrieveManufacturerByEmail(String email) throws ManufacturerNotFoundException;

    public void updateManufacturer(ManufacturerEntity manufacturerEntity) throws ManufacturerNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deleteManufacturer(Long manufacturerId) throws ManufacturerNotFoundException, DeleteEntityException;
    
}
