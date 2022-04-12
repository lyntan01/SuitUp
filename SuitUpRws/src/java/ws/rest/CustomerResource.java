/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.CustomerEntity;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.ChangePasswordException;
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateCustomerException;
import ws.datamodel.CreateCustomerReq;
import ws.datamodel.CustomerChangePasswordReq;
import ws.datamodel.UpdateProfileReq;

/**
 * REST Web Service
 *
 * @author keithcharleschan
 */
@Path("Customer")
public class CustomerResource {

    CustomerSessionBeanLocal customerSessionBeanLocal = lookupCustomerSessionBeanLocal();

    @Context
    private UriInfo context;

    public CustomerResource() {
    }

    @Path("customerLogin")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerLogin(@QueryParam("email") String email,
            @QueryParam("password") String password) {
        try {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** CustomerResource.customerLogin(): Customer " + customerEntity.getEmail() + " login remotely via web service");

            customerEntity.setPassword(null);
            customerEntity.setSalt(null);
            customerEntity.getOrders().clear();
//            customerEntity.getAddresses().clear();
            customerEntity.getAppointments().clear();
            customerEntity.getSupportTickets().clear();

//            customerEntity.setJacketMeasurement(null);
//            customerEntity.setPantsMeasurement(null);

            return Response.status(Response.Status.OK).entity(customerEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("customerRegister")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(CreateCustomerReq createCustomerReq) {
        if (createCustomerReq != null) {
            try {

                CustomerEntity customerEntity = createCustomerReq.getNewCustomer();

                Long customerId = customerSessionBeanLocal.createNewCustomer(customerEntity);

                return Response.status(Response.Status.OK).entity(customerId).build();
            } catch (CustomerEmailExistException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new customer request").build();
        }
    }

    @Path("retrieveCustomerByCustomerId/{customerId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerByCustomerId(@PathParam("customerId") Long customerId) {
        try {
            CustomerEntity customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);

            customerEntity.setPassword(null);
            customerEntity.setSalt(null);
            customerEntity.getOrders().clear();
            //customerEntity.getAddresses().clear();
            customerEntity.getAppointments().clear();
            customerEntity.getSupportTickets().clear();
            //customerEntity.setJacketMeasurement(null);
            //customerEntity.setPantsMeasurement(null);

            return Response.status(Response.Status.OK).entity(customerEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("changePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerChangePassword(CustomerChangePasswordReq customerChangePasswordReq) {
        if (customerChangePasswordReq != null) {
            try {
                customerSessionBeanLocal.customerChangePassword(customerChangePasswordReq.getEmail(), customerChangePasswordReq.getOldPassword(), customerChangePasswordReq.getNewPassword());

                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (ChangePasswordException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid password change request").build();
        }
    }

    @Path("updateProfile")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(UpdateProfileReq updateProfileReq) {
        if (updateProfileReq != null) {
            try {
                CustomerEntity customer = updateProfileReq.getCurrentCustomer();
                customerSessionBeanLocal.updateCustomer(customer);
        
                return Response.status(Response.Status.OK).build();
            } catch (CustomerNotFoundException | UpdateCustomerException | InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid user profile update request").build();
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
