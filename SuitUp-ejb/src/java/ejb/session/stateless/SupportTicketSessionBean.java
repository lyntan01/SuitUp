/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.SupportTicketEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
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
@Stateless
public class SupportTicketSessionBean implements SupportTicketSessionBeanLocal {
    
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public SupportTicketSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewSupportTicket(SupportTicketEntity newSupportTicketEntity, Long customerId) throws SupportTicketIdExistException, UnsuccessfulTicketException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<SupportTicketEntity>> constraintViolations = validator.validate(newSupportTicketEntity);
        
        if (constraintViolations.isEmpty()) {

            try {
                CustomerEntity customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                newSupportTicketEntity.setCustomer(customer);

                em.persist(newSupportTicketEntity);
                em.flush();
                
                customer.getSupportTickets().add(newSupportTicketEntity);

                return newSupportTicketEntity.getTicketId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new SupportTicketIdExistException("Support Ticket ID already exists.");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } catch (CustomerNotFoundException ex) {
                throw new UnsuccessfulTicketException("Customer ID " + customerId + " not found.");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<SupportTicketEntity> retrieveAllTickets() {
        Query query = em.createQuery("SELECT t FROM SupportTicketEntity t");
        return query.getResultList();
    }
    
    @Override
    public SupportTicketEntity retrieveSupportTicketByTicketId(Long ticketId) throws SupportTicketNotFoundException {
        Query query = em.createQuery("SELECT t FROM SupportTicketEntity t WHERE t.ticketId = :ticketId");
        query.setParameter("ticketId", ticketId);
        SupportTicketEntity ticket = (SupportTicketEntity) query.getSingleResult();
        
        if (ticket != null) {
            return ticket;
        } else {
            throw new SupportTicketNotFoundException("Support Ticket " + ticketId + " does not exist!");
        }
    }
    
    @Override
    public List<SupportTicketEntity> retrieveSupportTicketsByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT t FROM SupportTicketEntity t WHERE t.customer.customerId = :customerId");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    @Override
    public void updateSupportTicketDescription(Long ticketId, String newDescription) throws SupportTicketNotFoundException {
        SupportTicketEntity ticket = retrieveSupportTicketByTicketId(ticketId);
        ticket.setDescription(newDescription);
    }
    
    @Override
    public void addTicketReply(Long ticketId, String staffReply) throws SupportTicketNotFoundException, UpdateEntityException {
        SupportTicketEntity ticket = retrieveSupportTicketByTicketId(ticketId);
        
        if (ticket.getIsResolved()) {
            throw new UpdateEntityException("Ticket has been already resolved");
        } else {
            ticket.setStaffReply(staffReply);
            ticket.setIsResolved(Boolean.TRUE);
        }
    }
    
    @Override
    public void deleteSupportTicket(Long ticketId) throws SupportTicketNotFoundException, DeleteEntityException {
        SupportTicketEntity ticketToRemove = retrieveSupportTicketByTicketId(ticketId);
        
        if (ticketToRemove.getIsResolved()) {
            throw new DeleteEntityException("Support Ticket ID " + ticketId + " has already been resolved.");
        } else {
            ticketToRemove.getCustomer().getSupportTickets().remove(ticketToRemove);
            em.remove(ticketToRemove);
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<SupportTicketEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
    
