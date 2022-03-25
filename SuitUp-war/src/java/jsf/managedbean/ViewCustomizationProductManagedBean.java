/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ColourEntity;
import entity.FabricEntity;
import entity.JacketStyleEntity;
import entity.PantsCuttingEntity;
import entity.PocketStyleEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author xianhui
 */
@Named(value = "viewCustomizationProductManagedBean")
@ViewScoped
public class ViewCustomizationProductManagedBean implements Serializable {

    private ColourEntity colourToView;
    private JacketStyleEntity jacketStyleToView;
    private PocketStyleEntity pocketStyleToView;
    private FabricEntity fabricToView;
    private PantsCuttingEntity pantsCuttingToView;

    public ViewCustomizationProductManagedBean() {
        colourToView = new ColourEntity();
        jacketStyleToView = new JacketStyleEntity();
        pocketStyleToView = new PocketStyleEntity();
        fabricToView = new FabricEntity();
        pantsCuttingToView = new PantsCuttingEntity();
    }
    
    @PostConstruct
    public void postConstruct() 
    {
    }

    public ColourEntity getColourToView() {
        return colourToView;
    }

    public void setColourToView(ColourEntity colourToView) {
        this.colourToView = colourToView;
    }

    public JacketStyleEntity getJacketStyleToView() {
        return jacketStyleToView;
    }

    public void setJacketStyleToView(JacketStyleEntity jacketStyleToView) {
        this.jacketStyleToView = jacketStyleToView;
    }

    public PocketStyleEntity getPocketStyleToView() {
        return pocketStyleToView;
    }

    public void setPocketStyleToView(PocketStyleEntity pocketStyleToView) {
        this.pocketStyleToView = pocketStyleToView;
    }

    public FabricEntity getFabricToView() {
        return fabricToView;
    }

    public void setFabricToView(FabricEntity fabricToView) {
        this.fabricToView = fabricToView;
    }

    public PantsCuttingEntity getPantsCuttingToView() {
        return pantsCuttingToView;
    }

    public void setPantsCuttingToView(PantsCuttingEntity pantsCuttingToView) {
        this.pantsCuttingToView = pantsCuttingToView;
    }

}
