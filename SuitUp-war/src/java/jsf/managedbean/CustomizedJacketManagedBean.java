/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.inject.Inject;

/**
 *
 * @author meganyee
 */
@Named(value = "customizedJacketManagedBean")
@ViewScoped
public class CustomizedJacketManagedBean implements Serializable {

    @Inject
    private CreateOrderManagedBean createOrderManagedBean;
    
    public CustomizedJacketManagedBean() {
    }
    
}
