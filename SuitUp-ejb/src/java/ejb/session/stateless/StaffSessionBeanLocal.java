/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateStaffException;

/**
 *
 * @author lyntan
 */
@Local
public interface StaffSessionBeanLocal {

    public Long createNewStaff(StaffEntity newStaffEntity) throws StaffUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<StaffEntity> retrieveAllStaffs();

    public StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException;

    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;

    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;

    public void updateStaff(StaffEntity staffEntity) throws StaffNotFoundException, UpdateStaffException, InputDataValidationException;

    public void deleteStaff(Long staffId) throws StaffNotFoundException, DeleteStaffException;
    
}
