/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.SupportTicketSessionBeanLocal;
import entity.CustomerEntity;
import entity.SupportTicketEntity;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.SupportTicketIdExistException;
import util.exception.SupportTicketNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UnsuccessfulTicketException;
import ws.datamodel.CreateSupportTicketReq;
import ws.datamodel.UpdateSupportTicketReq;

/**
 * REST Web Service
 *
 * @author lyntan
 */
@Path("SupportTicket")
public class SupportTicketResource {

    CustomerSessionBeanLocal customerSessionBeanLocal = lookupCustomerSessionBeanLocal();
    
    SupportTicketSessionBeanLocal supportTicketSessionBeanLocal = lookupSupportTicketSessionBeanLocal();

    @Context
    private UriInfo context;

    public SupportTicketResource() {
    }
    
    @Path("retrieveAllSupportTicketsByCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSupportTicketsByCustomer(@QueryParam("email") String email, 
                                        @QueryParam("password") String password)
    {
        try
        {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** SupportTicketResource.retrieveAllSupportTickets(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            List<SupportTicketEntity> supportTicketEntities = supportTicketSessionBeanLocal.retrieveSupportTicketsByCustomerId(customerEntity.getCustomerId());
            
            for(SupportTicketEntity supportTicketEntity:supportTicketEntities)
            {
                supportTicketEntity.setCustomer(null);
            }
            
            GenericEntity<List<SupportTicketEntity>> genericEntity = new GenericEntity<List<SupportTicketEntity>>(supportTicketEntities) {
            };
            
            return Response.status(Status.OK).entity(genericEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveSupportTicket/{ticketId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSupportTicket(@QueryParam("email") String email, 
                                        @QueryParam("password") String password,
                                        @PathParam("ticketId") Long ticketId)
    {
        try
        {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** SupportTicketResource.retrieveSupportTicket(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            SupportTicketEntity supportTicketEntity = supportTicketSessionBeanLocal.retrieveSupportTicketByTicketId(ticketId);
            
            supportTicketEntity.setCustomer(null);
            
            return Response.status(Status.OK).entity(supportTicketEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(SupportTicketNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSupportTicket(CreateSupportTicketReq createSupportTicketReq)
    {
        if(createSupportTicketReq != null)
        {
            try
            {
                SupportTicketEntity ticket = createSupportTicketReq.getSupportTicket();
                ticket.setDateTime(new Date());
                ticket.setIsResolved(Boolean.FALSE);
                createSupportTicketReq.setSupportTicket(ticket);
                
                CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(createSupportTicketReq.getEmail(), createSupportTicketReq.getPassword());
                System.out.println("********** SupportTicketResource.createSupportTicket(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                Long supportTicketId  = supportTicketSessionBeanLocal.createNewSupportTicket(createSupportTicketReq.getSupportTicket(), customerEntity.getCustomerId());                
                
                return Response.status(Response.Status.OK).entity(supportTicketId).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(SupportTicketIdExistException | UnsuccessfulTicketException | UnknownPersistenceException | InputDataValidationException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new support ticket request").build();
        }
    }
    
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSupportTicket(UpdateSupportTicketReq updateSupportTicketReq)
    {
        if(updateSupportTicketReq != null)
        {
            try
            {                
                CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(updateSupportTicketReq.getEmail(), updateSupportTicketReq.getPassword());
                System.out.println("********** SupportTicketResource.updateSupportTicket(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                supportTicketSessionBeanLocal.updateSupportTicketDescription(updateSupportTicketReq.getSupportTicketEntity().getTicketId(), updateSupportTicketReq.getSupportTicketEntity().getDescription());
                
                return Response.status(Response.Status.OK).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(SupportTicketNotFoundException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update support ticket request").build();
        }
    }
    
    
    
    @Path("{supportTicketId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSupportTicket(@QueryParam("email") String email, 
                                        @QueryParam("password") String password,
                                        @PathParam("supportTicketId") Long supportTicketId)
    {
        try
        {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** SupportTicketResource.deleteSupportTicket(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            supportTicketSessionBeanLocal.deleteSupportTicket(supportTicketId);
            
            return Response.status(Status.OK).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(SupportTicketNotFoundException | DeleteEntityException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    

    private SupportTicketSessionBeanLocal lookupSupportTicketSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SupportTicketSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/SupportTicketSessionBean!ejb.session.stateless.SupportTicketSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CustomerSessionBeanLocal lookupCustomerSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/CustomerSessionBean!ejb.session.stateless.CustomerSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}
