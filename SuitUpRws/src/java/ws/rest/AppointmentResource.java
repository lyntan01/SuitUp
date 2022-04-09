/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.AppointmentSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.AppointmentEntity;
import entity.CustomerEntity;
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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.AppointmentNotFoundException;
import util.exception.CreateNewAppointmentException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StoreNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;
import ws.datamodel.CreateAppointmentReq;
import ws.datamodel.UpdateAppointmentReq;

/**
 * REST Web Service
 *
 * @author lyntan
 */
@Path("Appointment")
public class AppointmentResource {

    AppointmentSessionBeanLocal appointmentSessionBeanLocal = lookupAppointmentSessionBeanLocal();

    CustomerSessionBeanLocal customerSessionBeanLocal = lookupCustomerSessionBeanLocal();

    @Context
    private UriInfo context;

    public AppointmentResource() {
    }
    
    @Path("retrieveAllAppointmentsByCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllAppointmentsByCustomer(@QueryParam("email") String email, 
                                        @QueryParam("password") String password)
    {
        try
        {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** AppointmentResource.retrieveAllAppointments(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            List<AppointmentEntity> appointmentEntities = customerEntity.getAppointments();
            
            for (AppointmentEntity appointment : appointmentEntities) {
                appointment.getStore().getAppointments().clear();
                appointment.setCustomer(null);
                appointment.setTransaction(null);
            }
            
            GenericEntity<List<AppointmentEntity>> genericEntity = new GenericEntity<List<AppointmentEntity>>(appointmentEntities) {
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
    
    @Path("retrieveAppointment/{appointmentId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAppointment(@QueryParam("email") String email, 
                                        @QueryParam("password") String password,
                                        @PathParam("appointmentId") Long appointmentId)
    {
        try
        {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** AppointmentResource.retrieveAppointment(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            AppointmentEntity appointmentEntity = appointmentSessionBeanLocal.retrieveAppointmentByAppointmentId(appointmentId);
            
            appointmentEntity.getStore().getAppointments().clear();
            appointmentEntity.setCustomer(null);
            appointmentEntity.setTransaction(null);
                        
            return Response.status(Status.OK).entity(appointmentEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(AppointmentNotFoundException ex)
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
    public Response createAppointment(CreateAppointmentReq createAppointmentReq)
    {
        if(createAppointmentReq != null)
        {
            try
            {
                CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(createAppointmentReq.getEmail(), createAppointmentReq.getPassword());
                System.out.println("********** AppointmentResource.createAppointment(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                Long appointmentId  = appointmentSessionBeanLocal.createNewAppointment(createAppointmentReq.getAppointment(), createAppointmentReq.getStoreId(), customerEntity.getCustomerId());                
                
                return Response.status(Response.Status.OK).entity(appointmentId).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(CreateNewAppointmentException | StoreNotFoundException | CustomerNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new appointment request").build();
        }
    }
    
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAppointment(UpdateAppointmentReq updateAppointmentReq)
    {
        if(updateAppointmentReq != null)
        {
            try
            {                
                CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(updateAppointmentReq.getEmail(), updateAppointmentReq.getPassword());
                System.out.println("********** AppointmentResource.updateAppointment(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                appointmentSessionBeanLocal.updateAppointment(updateAppointmentReq.getAppointment(), updateAppointmentReq.getStoreId());
                
                return Response.status(Response.Status.OK).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(AppointmentNotFoundException | StoreNotFoundException | UpdateEntityException | InputDataValidationException ex)
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update appointment request").build();
        }
    }
    
    
    
    @Path("{appointmentId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAppointment(@QueryParam("email") String email, 
                                        @QueryParam("password") String password,
                                        @PathParam("appointmentId") Long appointmentId)
    {
        try
        {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** AppointmentResource.deleteAppointment(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            appointmentSessionBeanLocal.deleteAppointment(appointmentId);
            
            return Response.status(Status.OK).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(AppointmentNotFoundException | DeleteEntityException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
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

    private AppointmentSessionBeanLocal lookupAppointmentSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (AppointmentSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/AppointmentSessionBean!ejb.session.stateless.AppointmentSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}
