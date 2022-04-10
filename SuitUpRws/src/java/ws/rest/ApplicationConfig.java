/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author lyntan
 */
@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.rest.AddressResource.class);
        resources.add(ws.rest.AppointmentResource.class);
        resources.add(ws.rest.CorsFilter.class);
        resources.add(ws.rest.CreditCardResource.class);
        resources.add(ws.rest.CustomerResource.class);
        resources.add(ws.rest.CustomizationResource.class);
        resources.add(ws.rest.CustomizedProductResource.class);
        resources.add(ws.rest.MeasurementResource.class);
        resources.add(ws.rest.OrderResource.class);
        resources.add(ws.rest.PromotionResource.class);
        resources.add(ws.rest.StandardProductResource.class);
        resources.add(ws.rest.StoreResource.class);
        resources.add(ws.rest.SupportTicketResource.class);     
    }
    
}
