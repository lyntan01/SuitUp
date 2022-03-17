/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.JacketStyleEntity;
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
public interface JacketStyleSessionBeanLocal {
    public Long createNewJacketStyle(JacketStyleEntity newJacketStyleEntity) throws CustomizationIdExistException, UnknownPersistenceException, InputDataValidationException;
    
    public List<JacketStyleEntity> retrieveAllJacketStyles();
    
    public JacketStyleEntity retrieveJacketStyleById(Long customizationId) throws CustomizationNotFoundException;
    
    public void updateJacketStyle(JacketStyleEntity updatedJacketStyle) throws CustomizationNotFoundException, UpdateEntityException, InputDataValidationException;
    
    public void deleteJacketStyle(Long jacketStyleId) throws CustomizationNotFoundException;
   
}
