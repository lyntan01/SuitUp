/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OrderSessionBeanLocal;
import ejb.session.stateless.TransactionSessionBeanLocal;
import entity.CustomerEntity;
import entity.CustomizedJacketEntity;
import entity.CustomizedPantsEntity;
import entity.CustomizedProductEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.ProductEntity;
import entity.StandardProductEntity;
import entity.TagEntity;
import entity.TransactionEntity;
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
import util.enumeration.OrderStatusEnum;
import util.exception.AddressNotFoundException;
import util.exception.CancelOrderException;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OrderNotFoundException;
import util.exception.PromotionCodeExpiredException;
import util.exception.PromotionFullyRedeemedException;
import util.exception.PromotionMinimumAmountNotHitException;
import util.exception.PromotionNotFoundException;
import util.generator.RandomStringGenerator;
import ws.datamodel.ApplyPromotionCodeReq;
import ws.datamodel.CreateOrderReq;

/**
 * REST Web Service
 *
 * @author lyntan
 */
@Path("Order")
public class OrderResource {

    TransactionSessionBeanLocal transactionSessionBeanLocal = lookupTransactionSessionBeanLocal();

    OrderSessionBeanLocal orderSessionBeanLocal = lookupOrderSessionBeanLocal();

    CustomerSessionBeanLocal customerSessionBeanLocal = lookupCustomerSessionBeanLocal();

    @Context
    private UriInfo context;

    public OrderResource() {
    }

    @Path("retrieveOrdersByCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrdersByCustomer(@QueryParam("email") String email,
            @QueryParam("password") String password) {
        try {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** OrderResource.retrieveOrdersByCustomer(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            List<OrderEntity> orderEntities = orderSessionBeanLocal.retrieveOrderbyCustomerId(customerEntity.getCustomerId());

            for (OrderEntity orderEntity : orderEntities) {
                if (orderEntity.getPromotion() != null) {
                    orderEntity.getPromotion().getOrders().clear();
                }

                for (OrderLineItemEntity orderLineItemEntity : orderEntity.getOrderLineItems()) {

                    ProductEntity productEntity = orderLineItemEntity.getProduct();

                    if (productEntity instanceof StandardProductEntity) {

                        StandardProductEntity standardProduct = (StandardProductEntity) productEntity;
                        standardProduct.getCategory().getStandardProducts().clear();
                        List<TagEntity> tags = standardProduct.getTags();
                        for (TagEntity tag : tags) {
                            tag.getStandardProducts().clear();
                        }

                    } else if (productEntity instanceof CustomizedProductEntity) {

                        CustomizedProductEntity customizedProduct = (CustomizedProductEntity) productEntity;

                        if (customizedProduct instanceof CustomizedJacketEntity) {
                            CustomizedJacketEntity customizedJacket = (CustomizedJacketEntity) customizedProduct;
                            customizedJacket.getInnerFabric().getColour().getFabrics().clear();
                            customizedJacket.getOuterFabric().getColour().getFabrics().clear();
                        } else if (customizedProduct instanceof CustomizedPantsEntity) {
                            CustomizedPantsEntity customizedPants = (CustomizedPantsEntity) customizedProduct;
                            customizedPants.getFabric().getColour().getFabrics().clear();
                        }

                    }
                }

                orderEntity.getCustomer().getOrders().clear();
                orderEntity.getCustomer().getSupportTickets().clear();
                orderEntity.getCustomer().getAppointments().clear();
                
                if (orderEntity.getTransaction() != null) {
                    orderEntity.getTransaction().setAppointment(null);
                    orderEntity.getTransaction().setOrder(null);
                }  
            }

            GenericEntity<List<OrderEntity>> genericEntity = new GenericEntity<List<OrderEntity>>(orderEntities) {
            };
            System.out.println("***********" + genericEntity);

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveOrder/{orderId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrder(@QueryParam("email") String email,
            @QueryParam("password") String password,
            @PathParam("orderId") Long orderId) {
        try {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** OrderResource.retrieveOrder(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            OrderEntity orderEntity = orderSessionBeanLocal.retrieveOrderByOrderId(orderId);

            if (orderEntity.getPromotion() != null) {
                orderEntity.getPromotion().getOrders().clear();
            }

            for (OrderLineItemEntity orderLineItemEntity : orderEntity.getOrderLineItems()) {

                ProductEntity productEntity = orderLineItemEntity.getProduct();

                if (productEntity instanceof StandardProductEntity) {

                    StandardProductEntity standardProduct = (StandardProductEntity) productEntity;
                    standardProduct.getCategory().getStandardProducts().clear();
                    List<TagEntity> tags = standardProduct.getTags();
                    for (TagEntity tag : tags) {
                        tag.getStandardProducts().clear();
                    }

                } else if (productEntity instanceof CustomizedProductEntity) {

                    CustomizedProductEntity customizedProduct = (CustomizedProductEntity) productEntity;

                    if (customizedProduct instanceof CustomizedJacketEntity) {
                        CustomizedJacketEntity customizedJacket = (CustomizedJacketEntity) customizedProduct;
                        customizedJacket.getInnerFabric().getColour().getFabrics().clear();
                        customizedJacket.getOuterFabric().getColour().getFabrics().clear();
                    } else if (customizedProduct instanceof CustomizedPantsEntity) {
                        CustomizedPantsEntity customizedPants = (CustomizedPantsEntity) customizedProduct;
                        customizedPants.getFabric().getColour().getFabrics().clear();
                    }

                }
            }

            orderEntity.getCustomer().getOrders().clear();
            orderEntity.getCustomer().getSupportTickets().clear();
            orderEntity.getCustomer().getAppointments().clear();

            if (orderEntity.getTransaction() != null) {
                orderEntity.getTransaction().setAppointment(null);
                orderEntity.getTransaction().setOrder(null);
            }

            return Response.status(Response.Status.OK).entity(orderEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (OrderNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    // Order is created first before promotion is applied
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(CreateOrderReq createOrderReq) {
        if (createOrderReq != null) {
            try {
                CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(createOrderReq.getEmail(), createOrderReq.getPassword());
                System.out.println("********** OrderResource.createOrder(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                OrderEntity newOrder = createOrderReq.getOrder();
                newOrder.setOrderDateTime(new Date());
                RandomStringGenerator generator = new RandomStringGenerator(5);
                String orderSerialNumber = generator.generateSerial();
                newOrder.setSerialNumber(orderSerialNumber);
                newOrder.setOrderStatusEnum(OrderStatusEnum.PROCESSING);

                OrderEntity orderEntity = orderSessionBeanLocal.createNewOrder(customerEntity.getCustomerId(), createOrderReq.getAddressId(), newOrder);

                if (orderEntity.getPromotion() != null) {
                    orderEntity.getPromotion().getOrders().clear();
                }

                for (OrderLineItemEntity orderLineItemEntity : orderEntity.getOrderLineItems()) {

                    ProductEntity productEntity = orderLineItemEntity.getProduct();

                    if (productEntity instanceof StandardProductEntity) {

                        StandardProductEntity standardProduct = (StandardProductEntity) productEntity;
                        standardProduct.getCategory().getStandardProducts().clear();
                        List<TagEntity> tags = standardProduct.getTags();
                        for (TagEntity tag : tags) {
                            tag.getStandardProducts().clear();
                        }

                    } else if (productEntity instanceof CustomizedProductEntity) {

                        CustomizedProductEntity customizedProduct = (CustomizedProductEntity) productEntity;

                        if (customizedProduct instanceof CustomizedJacketEntity) {
                            CustomizedJacketEntity customizedJacket = (CustomizedJacketEntity) customizedProduct;
                            customizedJacket.getInnerFabric().getColour().getFabrics().clear();
                            customizedJacket.getOuterFabric().getColour().getFabrics().clear();
                        } else if (customizedProduct instanceof CustomizedPantsEntity) {
                            CustomizedPantsEntity customizedPants = (CustomizedPantsEntity) customizedProduct;
                            customizedPants.getFabric().getColour().getFabrics().clear();
                        }

                    }
                }

                orderEntity.getCustomer().getOrders().clear();
                orderEntity.getCustomer().getSupportTickets().clear();
                orderEntity.getCustomer().getAppointments().clear();

                if (orderEntity.getTransaction() != null) {
                    orderEntity.getTransaction().setAppointment(null);
                    orderEntity.getTransaction().setOrder(null);
                }

                return Response.status(Response.Status.OK).entity(orderEntity.getOrderId()).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (CustomerNotFoundException | CreateNewOrderException | InputDataValidationException | AddressNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                ex.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new order request").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response applyPromotionCode(ApplyPromotionCodeReq applyPromotionCodeReq) {
        if (applyPromotionCodeReq != null) {
            try {
                CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(applyPromotionCodeReq.getEmail(), applyPromotionCodeReq.getPassword());
                System.out.println("********** OrderResource.applyPromotionCode(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

                orderSessionBeanLocal.applyPromotionCode(applyPromotionCodeReq.getOrder().getOrderId(), applyPromotionCodeReq.getPromotionCode());

                // Create transaction for order, as last step of order creation
                transactionSessionBeanLocal.createNewTransaction(new TransactionEntity(applyPromotionCodeReq.getOrder().getTotalAmount(), new Date(), null, applyPromotionCodeReq.getOrder()), null, applyPromotionCodeReq.getOrder().getOrderId());

                return Response.status(Response.Status.OK).entity(applyPromotionCodeReq.getOrder().getOrderId()).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (OrderNotFoundException | PromotionNotFoundException | PromotionCodeExpiredException | PromotionMinimumAmountNotHitException | PromotionFullyRedeemedException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update order request").build();
        }
    }

    @Path("{orderId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelOrder(@QueryParam("email") String email,
            @QueryParam("password") String password,
            @PathParam("orderId") Long orderId) {
        try {
            CustomerEntity customerEntity = customerSessionBeanLocal.customerLogin(email, password);
            System.out.println("********** OrderResource.cancelOrder(): Customer " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " login remotely via web service");

            orderSessionBeanLocal.updateOrderToBeCancelled(orderId);

            return Response.status(Response.Status.OK).entity("Order cancelled successfully.").build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (OrderNotFoundException | CancelOrderException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
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

    private OrderSessionBeanLocal lookupOrderSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (OrderSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/OrderSessionBean!ejb.session.stateless.OrderSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private TransactionSessionBeanLocal lookupTransactionSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TransactionSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/TransactionSessionBean!ejb.session.stateless.TransactionSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
