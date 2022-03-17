/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ColourEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ColourIdExistException;
import util.exception.ColourNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author meganyee
 */
@Local
public interface ColourSessionBeanLocal {
    public Long createNewColour(ColourEntity newColourEntity) throws ColourIdExistException, UnknownPersistenceException, InputDataValidationException;
        
    public List<ColourEntity> retrieveAllColours();
    
    public ColourEntity retrieveColourByColourId(Long colourId) throws ColourNotFoundException;
    
    public void updateColour(ColourEntity updatedColourEntity) throws ColourNotFoundException, UpdateEntityException, InputDataValidationException;
    
    public void deleteColour(Long colourId) throws ColourNotFoundException, DeleteEntityException;
}
