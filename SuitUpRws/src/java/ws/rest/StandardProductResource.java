/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.StandardProductSessionBeanLocal;
import ejb.session.stateless.TagSessionBeanLocal;
import entity.CategoryEntity;
import entity.StandardProductEntity;
import entity.TagEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CategoryNotFoundException;
import util.exception.StandardProductNotFoundException;
import util.exception.TagNotFoundException;

/**
 * REST Web Service
 *
 * @author xianhui
 */
@Path("StandardProduct")
public class StandardProductResource {

    TagSessionBeanLocal tagSessionBean = lookupTagSessionBeanLocal();

    CategorySessionBeanLocal categorySessionBean = lookupCategorySessionBeanLocal();

    StandardProductSessionBeanLocal standardProductSessionBean = lookupStandardProductSessionBeanLocal();
    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of StandardProductResource
     */
    public StandardProductResource() {
    }

    @Path("retrieveAllStandardProducts")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllStandardProduct() {
        try {
            List<StandardProductEntity> standardProducts = standardProductSessionBean.retrieveAllStandardProducts();
            
            // Disassociate bidirectional relationships
            for(StandardProductEntity standardProduct : standardProducts) {
                standardProduct.getCategory().getStandardProducts().clear();
                List<TagEntity> tags = standardProduct.getTags();
                for(TagEntity tag: tags) {
                    tag.getStandardProducts().clear();
                }
                
            }

            GenericEntity<List<StandardProductEntity>> genericEntity = new GenericEntity<List<StandardProductEntity>>(standardProducts) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveStandardProduct/{productId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveStandardProduct(@PathParam("productId") Long productId)
    {
        try
        {
            StandardProductEntity standardProductEntity = standardProductSessionBean.retrieveStandardProductByStandardProductId(productId);
            standardProductEntity.getCategory().getStandardProducts().clear();

            for (TagEntity tagEntity : standardProductEntity.getTags())
            {
                tagEntity.getStandardProducts().clear();
            }
            
            return Response.status(Response.Status.OK).entity(standardProductEntity).build();

        }
        catch (StandardProductNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories() {
        try {
            List<CategoryEntity> categories = categorySessionBean.retrieveAllCategories();
      
            // Disassociate bidirectional relationships
            for (CategoryEntity category : categories) {
                List<StandardProductEntity> standardProducts = category.getStandardProducts();
                for (StandardProductEntity standardProduct : standardProducts) {
                    standardProduct.setCategory(null);
                    standardProduct.getTags().clear();
                }

            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categories) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveStandardProductsByCategory/{categoryId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveStadardProductsByCategory(@PathParam("categoryId") Long categoryId)
    {
        try
        {
            CategoryEntity categoryEntity = categorySessionBean.retrieveCategoryByCategoryId(categoryId);
            List<StandardProductEntity> standardProductEntities = categoryEntity.getStandardProducts();

            for (StandardProductEntity standardProduct : standardProductEntities)
            {
                standardProduct.setCategory(null);
                List<TagEntity> tags = standardProduct.getTags();
                for(TagEntity tag: tags) {
                    tag.getStandardProducts().clear();
                }
            }
            
            GenericEntity<List<StandardProductEntity>> genericEntity = new GenericEntity<List<StandardProductEntity>>(standardProductEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();

        }
        catch (CategoryNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllTags")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTags() {
        try {
            List<TagEntity> tags = tagSessionBean.retrieveAllTags();
            
            // Disassociate bidirectional relationships
            for(TagEntity tag : tags) {
                List<StandardProductEntity> standardProducts = tag.getStandardProducts();
                
                for(StandardProductEntity standardProduct: standardProducts) {
                    standardProduct.getTags().clear();
                    standardProduct.getCategory().getStandardProducts().clear();
                }
                
            }

            GenericEntity<List<TagEntity>> genericEntity = new GenericEntity<List<TagEntity>>(tags) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveStandardProductsByTag/{TagId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveStadardProductsByTag(@PathParam("TagId") Long tagId)
    {
        try
        {
            TagEntity tagEntity = tagSessionBean.retrieveTagByTagId(tagId);
            List<StandardProductEntity> standardProductEntities = tagEntity.getStandardProducts();

            for (StandardProductEntity standardProduct : standardProductEntities)
            {
                standardProduct.setCategory(null);
                standardProduct.getTags().clear();
            }
            
            GenericEntity<List<StandardProductEntity>> genericEntity = new GenericEntity<List<StandardProductEntity>>(standardProductEntities) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();

        }
        catch (TagNotFoundException ex)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch (Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private StandardProductSessionBeanLocal lookupStandardProductSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StandardProductSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/StandardProductSessionBean!ejb.session.stateless.StandardProductSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CategorySessionBeanLocal lookupCategorySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CategorySessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/CategorySessionBean!ejb.session.stateless.CategorySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private TagSessionBeanLocal lookupTagSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TagSessionBeanLocal) c.lookup("java:global/SuitUp/SuitUp-ejb/TagSessionBean!ejb.session.stateless.TagSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
