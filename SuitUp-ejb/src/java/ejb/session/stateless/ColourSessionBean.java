/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ColourEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
@Stateless
public class ColourSessionBean implements ColourSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ColourSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewColour(ColourEntity newColourEntity) throws ColourIdExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<ColourEntity>> constraintViolations = validator.validate(newColourEntity);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newColourEntity);
                em.flush();
                
                return newColourEntity.getColourId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ColourIdExistException("Colour ID already exists.");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } 
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<ColourEntity> retrieveAllColours() {
        Query query = em.createQuery("SELECT c FROM ColourEntity c");
        List<ColourEntity> colours = query.getResultList();
        
        for (ColourEntity colour: colours) {
            colour.getFabrics().size();
        }
        
        return colours;
    }
    
    @Override
    public ColourEntity retrieveColourByColourId(Long colourId) throws ColourNotFoundException {
        Query query = em.createQuery("SELECT c FROM ColourEntity c where c.colourId = :colourId");
        query.setParameter("colourId", colourId);
        try {
            return (ColourEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ColourNotFoundException("Colour ID " + colourId + " does not exist!");
        }
    }
    
    @Override
    public void updateColour(ColourEntity updatedColourEntity) throws ColourNotFoundException, UpdateEntityException, InputDataValidationException {
        if (updatedColourEntity != null && updatedColourEntity.getColourId() != null) {
            Set<ConstraintViolation<ColourEntity>> constraintViolations = validator.validate(updatedColourEntity);
            
            if (constraintViolations.isEmpty()) {
                ColourEntity colourToUpdate = retrieveColourByColourId(updatedColourEntity.getColourId());
                
                if (colourToUpdate.getColourId().equals(updatedColourEntity.getColourId())) {
                    colourToUpdate.setName(updatedColourEntity.getName());
                    colourToUpdate.setHexCode(updatedColourEntity.getHexCode());
                } else {
                    throw new UpdateEntityException("Id does not match!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ColourNotFoundException("Colour Id cannot be found");
        }
    }
    
    @Override
    public void deleteColour(Long colourId) throws ColourNotFoundException, DeleteEntityException {
        ColourEntity colourEntityToRemove = retrieveColourByColourId(colourId);
        
        if (colourEntityToRemove.getFabrics().size() > 0) {
            throw new DeleteEntityException("Colour cannot be removed as it is used by a fabric");
        }
        
        em.remove(colourEntityToRemove);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ColourEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
