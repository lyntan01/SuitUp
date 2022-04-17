/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.CustomizedJacketSessionBeanLocal;
import ejb.session.stateless.CustomizedPantsSessionBeanLocal;
import ejb.session.stateless.FabricSessionBeanLocal;
import ejb.session.stateless.JacketMeasurementSessionBeanLocal;
import ejb.session.stateless.JacketStyleSessionBeanLocal;
import ejb.session.stateless.OrderSessionBeanLocal;
import ejb.session.stateless.PocketStyleSessionBeanLocal;
import entity.CustomerEntity;
import entity.CustomizedJacketEntity;
import entity.CustomizedPantsEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.StandardProductEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CustomizationNotFoundException;
import util.exception.CustomizedProductIdExistsException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.JacketMeasurementNotFoundException;
import util.exception.OrderNotFoundException;
import util.exception.PantsMeasurementNotFoundException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateCustomizedJacketReq;
import ws.datamodel.CreateCustomizedPantsReq;

/**
 * REST Web Service
 *
 * @author xianhui
 */
@Path("CustomizedProduct")
public class CustomizedProductResource {

    CustomizedPantsSessionBeanLocal customizedPantsSessionBean = lookupCustomizedPantsSessionBeanLocal();

    OrderSessionBeanLocal orderSessionBean = lookupOrderSessionBeanLocal();

    CustomerSessionBeanLocal customerSessionBean = lookupCustomerSessionBeanLocal();

    CustomizedJacketSessionBeanLocal customizedJacketSessionBean = lookupCustomizedJacketSessionBeanLocal();

    @Context
    private UriInfo context;

    
    public CustomizedProductResource() {
    }

    @Path("retrieveCustomizedJacketByOrder/{orderId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomizedJacketByOrder(@PathParam("orderId") Long orderId, 
                                                    @QueryParam("email") String email, 
                                                    @QueryParam("password") String password)
    {
        try
        {
            CustomerEntity customer = customerSessionBean.customerLogin(email, password);
            System.out.println("********** CustomisedJacketResource.retrieveCustomisedJacketByOrder: Customer " + customer.getFirstName() + " " + customer.getLastName() + " login remotely via web service");
            
            OrderEntity order = orderSessionBean.retrieveOrderByOrderId(orderId);

            List<OrderLineItemEntity> orderLines = order.getOrderLineItems();

            List<CustomizedJacketEntity> customisedJackets = new ArrayList<>();
            
            for (OrderLineItemEntity orderLine : orderLines) {
                if (orderLine.getProduct() instanceof CustomizedJacketEntity) {
                    CustomizedJacketEntity customisedJacket = (CustomizedJacketEntity)orderLine.getProduct();
                    customisedJacket.getOuterFabric().getColour().getFabrics().clear();
                    customisedJacket.getInnerFabric().getColour().getFabrics().clear();
                    customisedJackets.add(customisedJacket);
                }
            }

            GenericEntity<List<CustomizedJacketEntity>> genericEntity = new GenericEntity<List<CustomizedJacketEntity>>(customisedJackets) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
//            return Response.status(Response.Status.OK).entity(customisedJacket).build();
            
        } catch (InvalidLoginCredentialException | OrderNotFoundException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("createCustomizedJacket")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomizedJacket(CreateCustomizedJacketReq createCustomizedJacketReq)
    {
        if(createCustomizedJacketReq != null)
        {
            try
            {
                CustomerEntity customerEntity = customerSessionBean.customerLogin(createCustomizedJacketReq.getEmail(), createCustomizedJacketReq.getPassword());
                System.out.println("********** MeasurementResource.createJacketMeasurement(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                Long customisedJacketId  = customizedJacketSessionBean.createNewCustomizedJacket(createCustomizedJacketReq.getCustomizedJacket(), createCustomizedJacketReq.getPocketStyleId(), createCustomizedJacketReq.getJacketStyleId(), createCustomizedJacketReq.getInnerFabricId(), createCustomizedJacketReq.getOuterFabricId(), createCustomizedJacketReq.getJacketMeasurementId());                
                
                return Response.status(Response.Status.OK).entity(customisedJacketId).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(CustomizedProductIdExistsException | JacketMeasurementNotFoundException | CustomizationNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
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
    
    @Path("retrieveCustomizedPantsByOrder/{orderId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomizedPantsByOrder(@PathParam("orderId") Long orderId, 
                                                    @QueryParam("email") String email, 
                                                    @QueryParam("password") String password)
    {
        try
        {
            CustomerEntity customer = customerSessionBean.customerLogin(email, password);
            System.out.println("********** CustomisedJacketResource.retrieveCustomisedJacketByOrder: Customer " + customer.getFirstName() + " " + customer.getLastName() + " login remotely via web service");
            
            OrderEntity order = orderSessionBean.retrieveOrderByOrderId(orderId);

            List<OrderLineItemEntity> orderLines = order.getOrderLineItems();
            
            List<CustomizedPantsEntity> customisedPants = new ArrayList<>();
            
            for(OrderLineItemEntity orderLine: orderLines) {
                if (orderLine.getProduct() instanceof CustomizedPantsEntity) {
                    CustomizedPantsEntity customisedpants = (CustomizedPantsEntity)orderLine.getProduct();
                    customisedpants.getFabric().getColour().getFabrics().clear();
                    customisedPants.add(customisedpants);
                }
            }

            
            return Response.status(Response.Status.OK).entity(customisedPants).build();
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
    
    @Path("createCustomizedPants")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomizedPants(CreateCustomizedPantsReq createCustomizedPantsReq)
    {
        if(createCustomizedPantsReq != null)
        {
            try
            {
                CustomerEntity customerEntity = customerSessionBean.customerLogin(createCustomizedPantsReq.getEmail(), createCustomizedPantsReq.getPassword());
                System.out.println("********** MeasurementResource.createPantsMeasurement(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                Long customisedPantsId  = customizedPantsSessionBean.createNewCustomizedPants(createCustomizedPantsReq.getNewCustomizedPants(), createCustomizedPantsReq.getFabricId(), createCustomizedPantsReq.getPantsCuttingId(), createCustomizedPantsReq.getPantsMeasurementId());                
                
                return Response.status(Response.Status.OK).entity(customisedPantsId).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            }
            catch(CustomizedProductIdExistsException | PantsMeasurementNotFoundException | CustomizationNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
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

    private CustomizedJacketSessionBeanLocal lookupCustomizedJacketSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomizedJacketSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/CustomizedJacketSessionBean!ejb.session.stateless.CustomizedJacketSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private FabricSessionBeanLocal lookupFabricSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (FabricSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/FabricSessionBean!ejb.session.stateless.FabricSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private JacketStyleSessionBeanLocal lookupJacketStyleSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (JacketStyleSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/JacketStyleSessionBean!ejb.session.stateless.JacketStyleSessionBeanLocal");
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

    private JacketMeasurementSessionBeanLocal lookupJacketMeasurementSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (JacketMeasurementSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/JacketMeasurementSessionBean!ejb.session.stateless.JacketMeasurementSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private PocketStyleSessionBeanLocal lookupPocketStyleSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PocketStyleSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/PocketStyleSessionBean!ejb.session.stateless.PocketStyleSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private OrderSessionBeanLocal lookupOrderSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (OrderSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/OrderSessionBean!ejb.session.stateless.OrderSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CustomizedPantsSessionBeanLocal lookupCustomizedPantsSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomizedPantsSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/CustomizedPantsSessionBean!ejb.session.stateless.CustomizedPantsSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
