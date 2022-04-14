/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.PromotionSessionBeanLocal;
import entity.PromotionEntity;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.PromotionCodeExpiredException;
import util.exception.PromotionFullyRedeemedException;
import util.exception.PromotionMinimumAmountNotHitException;
import util.exception.PromotionNotFoundException;

/**
 * REST Web Service
 *
 * @author lyntan
 */
@Path("Promotion")
public class PromotionResource {

    PromotionSessionBeanLocal promotionSessionBeanLocal = lookupPromotionSessionBeanLocal();

    @Context
    private UriInfo context;

    public PromotionResource() {
    }
    
    @Path("retrievePromotionByPromotionCode")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePromotionByPromotionCode(@QueryParam("promotionCode") String promotionCode)
    {
        try
        {
            PromotionEntity promotionEntity = promotionSessionBeanLocal.retrievePromotionByPromotionCode(promotionCode);

            promotionEntity.getOrders().clear();
                        
            return Response.status(Response.Status.OK).entity(promotionEntity).build();
        }
        catch (PromotionNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("applyPromotion")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response applyPromotion(@QueryParam("promotionCode") String promotionCode, 
                                    @QueryParam("totalAmount") BigDecimal totalAmount)
    {
        try
        {
            BigDecimal discountedAmount = promotionSessionBeanLocal.getDiscountedAmount(promotionCode, totalAmount);
            return Response.status(Response.Status.OK).entity(discountedAmount).build();
        }
        catch (PromotionNotFoundException | PromotionCodeExpiredException | PromotionFullyRedeemedException | PromotionMinimumAmountNotHitException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private PromotionSessionBeanLocal lookupPromotionSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PromotionSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/PromotionSessionBean!ejb.session.stateless.PromotionSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}
