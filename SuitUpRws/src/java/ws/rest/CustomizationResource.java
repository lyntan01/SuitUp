/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.ColourSessionBeanLocal;
import ejb.session.stateless.FabricSessionBeanLocal;
import ejb.session.stateless.JacketStyleSessionBeanLocal;
import ejb.session.stateless.PantsCuttingSessionBeanLocal;
import ejb.session.stateless.PocketStyleSessionBeanLocal;
import entity.ColourEntity;
import entity.FabricEntity;
import entity.JacketStyleEntity;
import entity.PantsCuttingEntity;
import entity.PocketStyleEntity;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.ColourNotFoundException;
import util.exception.CustomizationNotFoundException;

/**
 * REST Web Service
 *
 * @author xianhui
 */
@Path("Customization")
public class CustomizationResource {

    PantsCuttingSessionBeanLocal pantsCuttingSessionBean = lookupPantsCuttingSessionBeanLocal();

    JacketStyleSessionBeanLocal jacketStyleSessionBean = lookupJacketStyleSessionBeanLocal();

    FabricSessionBeanLocal fabricSessionBean = lookupFabricSessionBeanLocal();

    PocketStyleSessionBeanLocal pocketStyleSessionBean = lookupPocketStyleSessionBeanLocal();

    ColourSessionBeanLocal colourSessionBean = lookupColourSessionBeanLocal();
    
    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CustomizationResource
     */
    public CustomizationResource() {
    }
    
//    <-----------------------PANTS CUTTING -------------------->
    @Path("retrieveAllPantsCutting")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPantsCutting() {
        try {
            List<PantsCuttingEntity> pantsCuttings = pantsCuttingSessionBean.retrieveAllPantsCutting();

            GenericEntity<List<PantsCuttingEntity>> genericEntity = new GenericEntity<List<PantsCuttingEntity>>(pantsCuttings) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrievePantsCutting/{customizationId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveProduct(@PathParam("customizationId") Long customizationId)
    {
        try
        {
            PantsCuttingEntity pantsCuttingEntity = pantsCuttingSessionBean.retrievePantsCuttingById(customizationId);
            
            return Response.status(Response.Status.OK).entity(pantsCuttingEntity).build();

        }
        catch (CustomizationNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
//    <-----------------------JACKET STYLE-------------------->
    @Path("retrieveAllJacketStyle")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllJacketStyle() {
        try {
            List<JacketStyleEntity> jacketStyles = jacketStyleSessionBean.retrieveAllJacketStyles();

            GenericEntity<List<JacketStyleEntity>> genericEntity = new GenericEntity<List<JacketStyleEntity>>(jacketStyles) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveJacketStyle/{customizationId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveJacketStyle(@PathParam("customizationId") Long customizationId)
    {
        try
        {
            JacketStyleEntity jacketStyleEntity = jacketStyleSessionBean.retrieveJacketStyleById(customizationId);
            return Response.status(Response.Status.OK).entity(jacketStyleEntity).build();

        }
        catch (CustomizationNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
//    <-----------------------POCKET STYLE -------------------->
    @Path("retrieveAllPocketStyle")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPocketStyle() {
        try {
            List<PocketStyleEntity> pocketStyles = pocketStyleSessionBean.retrieveAllPocketStyles();

            GenericEntity<List<PocketStyleEntity>> genericEntity = new GenericEntity<List<PocketStyleEntity>>(pocketStyles) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrievePocketStyle/{customizationId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePocketStyle(@PathParam("customizationId") Long customizationId)
    {
        try
        {
            PocketStyleEntity pocketStyleEntity = pocketStyleSessionBean.retrievePocketStyleById(customizationId);
            return Response.status(Response.Status.OK).entity(pocketStyleEntity).build();

        }
        catch (CustomizationNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    //    <-----------------------COLOUR-------------------->
    @Path("retrieveAllColour")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllColour() {
        try {
            List<ColourEntity> colours = colourSessionBean.retrieveAllColours();
            
            for(ColourEntity colour: colours) {
                for(FabricEntity fabric: colour.getFabrics()) {
                    fabric.setColour(null);
                }
            }

            GenericEntity<List<ColourEntity>> genericEntity = new GenericEntity<List<ColourEntity>>(colours) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveColour/{colourId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveColour(@PathParam("colourId") Long colourId) {
        try {
            ColourEntity colourEntity = colourSessionBean.retrieveColourByColourId(colourId);
            for (FabricEntity fabric : colourEntity.getFabrics()) {
                fabric.setColour(null);
            }
            return Response.status(Response.Status.OK).entity(colourEntity).build();

        } catch (ColourNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
//    <-----------------------FABRIC-------------------->
    @Path("retrieveAllFabric")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFabric() {
        try {
            List<FabricEntity> fabrics = fabricSessionBean.retrieveAllFabrics();
            
            for(FabricEntity fabric: fabrics) {
                fabric.getColour().getFabrics().clear();
            }

            GenericEntity<List<FabricEntity>> genericEntity = new GenericEntity<List<FabricEntity>>(fabrics) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveFabric/{customizationId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFabric(@PathParam("customizationId") Long customizationId)
    {
        try
        {
            FabricEntity fabricEntity = fabricSessionBean.retrieveFabricById(customizationId);
            fabricEntity.getColour().getFabrics().clear();
            return Response.status(Response.Status.OK).entity(fabricEntity).build();

        }
        catch (CustomizationNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    

    private ColourSessionBeanLocal lookupColourSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ColourSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/ColourSessionBean!ejb.session.stateless.ColourSessionBeanLocal");
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

    private PantsCuttingSessionBeanLocal lookupPantsCuttingSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PantsCuttingSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/PantsCuttingSessionBean!ejb.session.stateless.PantsCuttingSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
}
