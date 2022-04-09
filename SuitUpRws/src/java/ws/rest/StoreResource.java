/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.StoreSessionBeanLocal;
import entity.StoreEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author lyntan
 */
@Path("Store")
public class StoreResource {

    StoreSessionBeanLocal storeSessionBeanLocal = lookupStoreSessionBeanLocal();

    @Context
    private UriInfo context;

    public StoreResource() {
    }

    @Path("retrieveAllStores")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllStores() {
        try {
            List<StoreEntity> storeEntities = storeSessionBeanLocal.retrieveAllStores();
            
            // Disassociate bidirectional relationships
            for (StoreEntity store : storeEntities) {
                store.getAppointments().clear();
                store.getStaff().clear();
            }

            GenericEntity<List<StoreEntity>> genericEntity = new GenericEntity<List<StoreEntity>>(storeEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private StoreSessionBeanLocal lookupStoreSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StoreSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/StoreSessionBean!ejb.session.stateless.StoreSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
