/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.JacketMeasurementSessionBeanLocal;
import ejb.session.stateless.PantsMeasurementSessionBeanLocal;
import entity.CustomerEntity;
import entity.JacketMeasurementEntity;
import entity.PantsMeasurementEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.JacketMeasurementNotFoundException;
import util.exception.PantsMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;
import ws.datamodel.CreateJacketMeasurementReq;
import ws.datamodel.CreatePantsMeasurementReq;
import ws.datamodel.UpdateJacketMeasurementReq;
import ws.datamodel.UpdatePantsMeasurementReq;

/**
 * REST Web Service
 *
 * @author xianhui
 */
@Path("Measurement")
public class MeasurementResource {

    CustomerSessionBeanLocal customerSessionBean = lookupCustomerSessionBeanLocal();

    PantsMeasurementSessionBeanLocal pantsMeasurementSessionBean = lookupPantsMeasurementSessionBeanLocal();

    JacketMeasurementSessionBeanLocal jacketMeasurementSessionBean = lookupJacketMeasurementSessionBeanLocal();
    
    

    @Context
    private UriInfo context;

    
    public MeasurementResource() {
    }

    /**
     * Retrieves representation of an instance of ws.rest.MeasurementResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveJacketMeasurementByCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveJacketMeasurementByCustomer(@QueryParam("email") String email, 
                                        @QueryParam("password") String password)
    {
        try
        {
            CustomerEntity customer = customerSessionBean.customerLogin(email, password);
            System.out.println("********** MeasurementResource.retrieveJacketMeasurementByCustomer: Customer " + customer.getFirstName() + " " + customer.getLastName() + " login remotely via web service");

            JacketMeasurementEntity jacketMeasurement = customer.getJacketMeasurement();

            
            return Response.status(Response.Status.OK).entity(jacketMeasurement).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrievePantsMeasurementByCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePantsMeasurementByCustomer(@QueryParam("email") String email, 
                                        @QueryParam("password") String password)
    {
        try
        {
            CustomerEntity customer = customerSessionBean.customerLogin(email, password);
            System.out.println("********** MeasurementResource.retrievePantsMeasurementByCustomer: Customer " + customer.getFirstName() + " " + customer.getLastName() + " login remotely via web service");

            PantsMeasurementEntity pantsMeasurement = customer.getPantsMeasurement();

            
            return Response.status(Response.Status.OK).entity(pantsMeasurement).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    
    @Path("createJacketMeasurement")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createJacketMeasurement(CreateJacketMeasurementReq createJacketMeasurementReq)
    {
        if(createJacketMeasurementReq != null)
        {
            try
            {
                CustomerEntity customerEntity = customerSessionBean.customerLogin(createJacketMeasurementReq.getEmail(), createJacketMeasurementReq.getPassword());
                System.out.println("********** MeasurementResource.createJacketMeasurement(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                Long jacketMeasurementId  = jacketMeasurementSessionBean.createNewJacketMeasurement(createJacketMeasurementReq.getJacketMeasurement(), customerEntity.getCustomerId());                
                
                JacketMeasurementEntity jacketMeasurement = jacketMeasurementSessionBean.retrieveJacketMeasurementByJacketMeasurementId(jacketMeasurementId);
                
                return Response.status(Response.Status.OK).entity(jacketMeasurement).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(CustomerNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new jacket measurement request").build();
        }
    }
    
    @Path("createPantsMeasurement")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPantsMeasurement(CreatePantsMeasurementReq createPantsMeasurementReq)
    {
        if(createPantsMeasurementReq != null)
        {
            try
            {
                CustomerEntity customerEntity = customerSessionBean.customerLogin(createPantsMeasurementReq.getEmail(), createPantsMeasurementReq.getPassword());
                System.out.println("********** MeasurementResource.createPantsMeasurement(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                Long pantsMeasurementId  = pantsMeasurementSessionBean.createNewPantsMeasurement(createPantsMeasurementReq.getPantsMeasurement(), customerEntity.getCustomerId());                
               
                PantsMeasurementEntity pants = pantsMeasurementSessionBean.retrievePantsMeasurementByPantsMeasurementId(pantsMeasurementId);
                
                return Response.status(Response.Status.OK).entity(pants).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(CustomerNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new pants measurement request").build();
        }
    }
    
    
    @Path("updateJacketMeasurement")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJacketMeasurement(UpdateJacketMeasurementReq updateJacketMeasurementReq)
    {
        if(updateJacketMeasurementReq != null)
        {
            try
            {                
                CustomerEntity customerEntity = customerSessionBean.customerLogin(updateJacketMeasurementReq.getEmail(), updateJacketMeasurementReq.getPassword());
                System.out.println("********** MeasurementResource.updateJacketMeasuremen(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                jacketMeasurementSessionBean.updateJacketMeasurement(updateJacketMeasurementReq.getJacketMeasurement());
                
                return Response.status(Response.Status.OK).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(InputDataValidationException | JacketMeasurementNotFoundException | UpdateEntityException | UnknownPersistenceException | CustomerNotFoundException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update jacket measurement request").build();
        }
    }
    
    @Path("updatePantsMeasurement")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePantsMeasurement(UpdatePantsMeasurementReq updatePantsMeasurementReq)
    {
        if(updatePantsMeasurementReq != null)
        {
            try
            {                
                CustomerEntity customerEntity = customerSessionBean.customerLogin(updatePantsMeasurementReq.getEmail(), updatePantsMeasurementReq.getPassword());
                System.out.println("********** MeasurementResource.updatePantsMeasuremen(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                pantsMeasurementSessionBean.updatePantsMeasurement(updatePantsMeasurementReq.getPantsMeasurement());
                
                return Response.status(Response.Status.OK).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(InputDataValidationException | PantsMeasurementNotFoundException | UpdateEntityException | UnknownPersistenceException | CustomerNotFoundException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update pants measurement request").build();
        }
    }

    private JacketMeasurementSessionBeanLocal lookupJacketMeasurementSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (JacketMeasurementSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/JacketMeasurementSessionBean!ejb.session.stateless.JacketMeasurementSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private PantsMeasurementSessionBeanLocal lookupPantsMeasurementSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PantsMeasurementSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/PantsMeasurementSessionBean!ejb.session.stateless.PantsMeasurementSessionBeanLocal");
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
