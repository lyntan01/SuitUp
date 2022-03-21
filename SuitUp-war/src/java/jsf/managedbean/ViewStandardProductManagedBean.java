/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.StandardProductEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author xianhui
 */
@Named(value = "viewStandardProductManagedBean")
@ViewScoped
public class ViewStandardProductManagedBean implements Serializable {

    private StandardProductEntity standardProductToView;
    
    public ViewStandardProductManagedBean() {
        standardProductToView = new StandardProductEntity();
    }
    
    @PostConstruct
    public void postConstruct()
    {        
    }

    public StandardProductEntity getStandardProductToView() {
        return standardProductToView;
    }

    public void setStandardProductToView(StandardProductEntity standardProductToView) {
        this.standardProductToView = standardProductToView;
    }
    
}
