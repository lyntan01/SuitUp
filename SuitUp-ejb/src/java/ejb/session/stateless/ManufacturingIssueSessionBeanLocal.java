/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ManufacturingIssueEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.ManufacturingIssueNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Local
public interface ManufacturingIssueSessionBeanLocal {

    public Long createNewManufacturingIssue(ManufacturingIssueEntity newManufacturingIssueEntity) throws UnknownPersistenceException, InputDataValidationException;

    public List<ManufacturingIssueEntity> retrieveAllManufacturingIssues();

    public ManufacturingIssueEntity retrieveManufacturingIssueByManufacturingIssueId(Long manufacturingIssueId) throws ManufacturingIssueNotFoundException;

    public void updateManufacturingIssue(ManufacturingIssueEntity manufacturingIssueEntity) throws ManufacturingIssueNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deleteManufacturingIssue(Long manufacturingIssueId) throws ManufacturingIssueNotFoundException, DeleteEntityException;
    
}
