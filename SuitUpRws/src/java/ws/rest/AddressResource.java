/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.AddressSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.AddressEntity;
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
import util.exception.AddressNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;
import ws.datamodel.CreateAddressReq;
import ws.datamodel.UpdateAddressReq;

/**
 * REST Web Service
 *
 * @author lyntan
 */
@Path("Address")
public class AddressResource {

    CustomerSessionBeanLocal customerSessionBeanLocal = lookupCustomerSessionBeanLocal();

    AddressSessionBeanLocal addressSessionBeanLocal = lookupAddressSessionBeanLocal();

    @Context
    private UriInfo context;

    
    public AddressResource() {
    }
    
    @Path("retrieveAllAddressesByCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllAddressesByCustomer(@QueryParam("email") String email, 
                                        @QueryParam("password") String password)
    {
        try
        {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** AddressResource.retrieveAllAddresses(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            List<AddressEntity> addressEntities = customerEntity.getAddresses();
            
            GenericEntity<List<AddressEntity>> genericEntity = new GenericEntity<List<AddressEntity>>(addressEntities) {
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
    
    @Path("retrieveAddress/{addressId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAddress(@QueryParam("email") String email, 
                                        @QueryParam("password") String password,
                                        @PathParam("addressId") Long addressId)
    {
        try
        {
            if (!addressSessionBeanLocal.isStoreAddress(addressId)) {
                CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
                System.out.println("********** AddressResource.retrieveAddress(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");
            }
            
            AddressEntity addressEntity = addressSessionBeanLocal.retrieveAddressByAddressId(addressId);
                        
            return Response.status(Status.OK).entity(addressEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity("You must be logged in to view this address. " + ex.getMessage()).build();
        }
        catch(AddressNotFoundException ex)
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
    public Response createAddress(CreateAddressReq createAddressReq)
    {
        if(createAddressReq != null)
        {
            try
            {
                CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(createAddressReq.getEmail(), createAddressReq.getPassword());
                System.out.println("********** AddressResource.createAddress(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                Long addressId  = addressSessionBeanLocal.createNewCustomerAddress(createAddressReq.getAddress(), customerEntity.getCustomerId());                
                
                return Response.status(Response.Status.OK).entity(addressId).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(CustomerNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new address request").build();
        }
    }
    
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAddress(UpdateAddressReq updateAddressReq)
    {
        if(updateAddressReq != null)
        {
            try
            {                
                System.out.println("test");
                System.out.println(updateAddressReq.getEmail());
                System.out.println(updateAddressReq.getPassword());
                CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(updateAddressReq.getEmail(), updateAddressReq.getPassword());
                System.out.println("********** AddressResource.updateAddress(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                addressSessionBeanLocal.updateAddress(updateAddressReq.getAddressEntity());
                
                return Response.status(Response.Status.OK).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(AddressNotFoundException | UpdateEntityException | InputDataValidationException ex)
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update address request").build();
        }
    }
    
    
    
    @Path("{addressId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAddress(@QueryParam("email") String email, 
                                        @QueryParam("password") String password,
                                        @PathParam("addressId") Long addressId)
    {
        try
        {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** AddressResource.deleteAddress(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            addressSessionBeanLocal.deleteAddress(addressId);
            
            return Response.status(Status.OK).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(AddressNotFoundException | DeleteEntityException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private AddressSessionBeanLocal lookupAddressSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (AddressSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/AddressSessionBean!ejb.session.stateless.AddressSessionBeanLocal");
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
