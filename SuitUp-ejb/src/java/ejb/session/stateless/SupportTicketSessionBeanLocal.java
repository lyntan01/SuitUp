/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SupportTicketEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.SupportTicketIdExistException;
import util.exception.SupportTicketNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UnsuccessfulTicketException;
import util.exception.UpdateEntityException;

/**
 *
 * @author meganyee
 */
@Local
public interface SupportTicketSessionBeanLocal {
    
    public Long createNewSupportTicket(SupportTicketEntity newSupportTicketEntity, Long customerId) throws SupportTicketIdExistException, UnsuccessfulTicketException, UnknownPersistenceException, InputDataValidationException;
    
    public List<SupportTicketEntity> retrieveAllTickets();
    
    public SupportTicketEntity retrieveSupportTicketByTicketId(Long ticketId) throws SupportTicketNotFoundException;
    
    public List<SupportTicketEntity> retrieveSupportTicketsByCustomerId(Long customerId);
    
    public void updateSupportTicketDescription(Long ticketId, String newDescription) throws SupportTicketNotFoundException;
    
    public void addTicketReply(Long ticketId, String staffReply) throws SupportTicketNotFoundException, UpdateEntityException;
    
    public void deleteSupportTicket(Long ticketId) throws SupportTicketNotFoundException, DeleteEntityException;
    
}
