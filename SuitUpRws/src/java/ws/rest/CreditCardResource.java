/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CreditCardSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.StaffEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import util.exception.CreditCardNotFoundException;
import util.exception.CreditCardNumberExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateEntityException;
import ws.datamodel.CreateCreditCardReq;
import ws.datamodel.UpdateCreditCardReq;

/**
 * REST Web Service
 *
 * @author keithcharleschan
 */
@Path("CreditCard")
public class CreditCardResource {

    CustomerSessionBeanLocal customerSessionBeanLocal = lookupCustomerSessionBeanLocal();

    CreditCardSessionBeanLocal creditCardSessionBeanLocal = lookupCreditCardSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CreditCardResource
     */
    public CreditCardResource() {
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCreditCard(CreateCreditCardReq createCreditCardReq) {
        if (createCreditCardReq != null) {
            try {
                LocalDateTime date = Instant.ofEpochMilli(createCreditCardReq.getExpiryDate()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                Date expiryDate = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());

                createCreditCardReq.getNewCreditCard().setExpiryDate(expiryDate);

                CustomerEntity customer = customerSessionBeanLocal.customerLogin(createCreditCardReq.getEmail(), createCreditCardReq.getPassword());
                Long customerId = customer.getCustomerId();
                Long creditCardId = creditCardSessionBeanLocal.createNewCreditCard(createCreditCardReq.getNewCreditCard(), customerId);

                return Response.status(Response.Status.OK).entity(creditCardId).build();
            } catch (CreditCardNumberExistException | CustomerNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new credit card request").build();
        }
    }

    @Path("{creditCardId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@QueryParam("email") String email,
            @QueryParam("password") String password,
            @PathParam("creditCardId") Long creditCardId) {

        try {
            CustomerEntity customer = customerSessionBeanLocal.customerLogin(email, password);
            creditCardSessionBeanLocal.deleteCreditCard(creditCardId);

            return Response.status(Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (CreditCardNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

    @Path("retrieveAllCreditCards")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCreditCards(@QueryParam("email") String email,
            @QueryParam("password") String password) {
        try {

            CustomerEntity customer = customerSessionBeanLocal.customerLogin(email, password);
            List<CreditCardEntity> creditCards = customer.getCreditCards();

            GenericEntity<List<CreditCardEntity>> genericEntity = new GenericEntity<List<CreditCardEntity>>(creditCards) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("{creditCardId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardById(@QueryParam("email") String email,
            @QueryParam("password") String password,
            @PathParam("creditCardId") Long creditCardId) {
        try {
            CustomerEntity customer = customerSessionBeanLocal.customerLogin(email, password);
            CreditCardEntity creditCard = creditCardSessionBeanLocal.retrieveCreditCardByCreditCardId(creditCardId);

            return Response.status(Status.OK).entity(creditCard).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (CreditCardNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCard(UpdateCreditCardReq updateCreditCardReq)
    {
        if(updateCreditCardReq != null)
        {
            try
            {                
                 CustomerEntity customer = customerSessionBeanLocal.customerLogin(updateCreditCardReq.getEmail(), updateCreditCardReq.getPassword());
                
                creditCardSessionBeanLocal.updateCreditCard(updateCreditCardReq.getCreditCardEntity());
                
                return Response.status(Response.Status.OK).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(UpdateEntityException ex)
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update product request").build();
        }
    }

    private CreditCardSessionBeanLocal lookupCreditCardSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CreditCardSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/CreditCardSessionBean!ejb.session.stateless.CreditCardSessionBeanLocal");
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
