/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import javafx.event.ActionEvent;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author lyntan
 */
@Named(value = "viewMyProfileManagedBean")
@RequestScoped
public class ViewMyProfileManagedBean {

    public ViewMyProfileManagedBean() {
    }
    
    
}
