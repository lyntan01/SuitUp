/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ManufacturingIssueEntity;
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
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.ManufacturingIssueNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Stateless
public class ManufacturingIssueSessionBean implements ManufacturingIssueSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ManufacturingIssueSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewManufacturingIssue(ManufacturingIssueEntity newManufacturingIssueEntity) throws UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<ManufacturingIssueEntity>> constraintViolations = validator.validate(newManufacturingIssueEntity);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newManufacturingIssueEntity);
                em.flush();

                return newManufacturingIssueEntity.getManufacturingIssueId();
            } catch (PersistenceException ex) {

                throw new UnknownPersistenceException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<ManufacturingIssueEntity> retrieveAllManufacturingIssues() {
        Query query = em.createQuery("SELECT m FROM ManufacturingIssueEntity m");

        return query.getResultList();
    }

    @Override
    public ManufacturingIssueEntity retrieveManufacturingIssueByManufacturingIssueId(Long manufacturingIssueId) throws ManufacturingIssueNotFoundException {
        ManufacturingIssueEntity manufacturingIssueEntity = em.find(ManufacturingIssueEntity.class, manufacturingIssueId);

        if (manufacturingIssueEntity != null) {
            return manufacturingIssueEntity;
        } else {
            throw new ManufacturingIssueNotFoundException("Manufacturing Issue ID " + manufacturingIssueId + " does not exist!");
        }
    }

    @Override
    public void updateManufacturingIssue(ManufacturingIssueEntity manufacturingIssueEntity) throws ManufacturingIssueNotFoundException, UpdateEntityException, InputDataValidationException {
        if (manufacturingIssueEntity != null && manufacturingIssueEntity.getManufacturingIssueId() != null) {
            Set<ConstraintViolation<ManufacturingIssueEntity>> constraintViolations = validator.validate(manufacturingIssueEntity);

            if (constraintViolations.isEmpty()) {
                ManufacturingIssueEntity manufacturingIssueEntityToUpdate = retrieveManufacturingIssueByManufacturingIssueId(manufacturingIssueEntity.getManufacturingIssueId());

                if (manufacturingIssueEntityToUpdate.getManufacturingIssueId().equals(manufacturingIssueEntity.getManufacturingIssueId())) {
                    manufacturingIssueEntityToUpdate.setName(manufacturingIssueEntity.getName());
                    manufacturingIssueEntityToUpdate.setDescription(manufacturingIssueEntity.getDescription());
                } else {
                    throw new UpdateEntityException("ID of manufacturing issue record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ManufacturingIssueNotFoundException("Manufacturing Issue ID not provided for manufacturingIssue to be updated");
        }
    }

    @Override
    public void deleteManufacturingIssue(Long manufacturingIssueId) throws ManufacturingIssueNotFoundException, DeleteEntityException {
        ManufacturingIssueEntity manufacturingIssueEntityToRemove = retrieveManufacturingIssueByManufacturingIssueId(manufacturingIssueId);
        
        // Disassocation
        manufacturingIssueEntityToRemove.getOrder().getManufacturingIssues().remove(manufacturingIssueEntityToRemove);
        manufacturingIssueEntityToRemove.setOrder(null);
        Query query = em.createQuery("SELECT s FROM StaffEntity s, IN (s.manufacturingIssues) m WHERE m.manufacturingIssueId = :issueId")
                        .setParameter("issueId", manufacturingIssueId);
        StaffEntity staffEntity = (StaffEntity) query.getSingleResult();
        staffEntity.getManufacturingIssues().remove(manufacturingIssueEntityToRemove);
        
        em.remove(manufacturingIssueEntityToRemove);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ManufacturingIssueEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
