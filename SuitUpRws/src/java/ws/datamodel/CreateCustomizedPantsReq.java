/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.CustomizedPantsEntity;

/**
 *
 * @author xianhui
 */
public class CreateCustomizedPantsReq {
    
    private String email;
    private String password;
    private CustomizedPantsEntity newCustomizedPants; 
    private Long fabricId; 
    private Long pantsCuttingId; 
    private Long pantsMeasurementId;
        
    public CreateCustomizedPantsReq(String email, String password, CustomizedPantsEntity newCustomizedPants, Long fabricId, Long pantsCuttingId, Long pantsMeasurementId) {
        this.email = email;
        this.password = password;
        this.newCustomizedPants = newCustomizedPants; 
        this.fabricId = fabricId;
        this.pantsCuttingId = pantsCuttingId;  
        this.pantsMeasurementId = pantsMeasurementId;

    }

    public CreateCustomizedPantsReq() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomizedPantsEntity getNewCustomizedPants() {
        return newCustomizedPants;
    }

    public void setNewCustomizedPants(CustomizedPantsEntity newCustomizedPants) {
        this.newCustomizedPants = newCustomizedPants;
    }

    public Long getFabricId() {
        return fabricId;
    }

    public void setFabricId(Long fabricId) {
        this.fabricId = fabricId;
    }

    public Long getPantsCuttingId() {
        return pantsCuttingId;
    }

    public void setPantsCuttingId(Long pantsCuttingId) {
        this.pantsCuttingId = pantsCuttingId;
    }

    public Long getPantsMeasurementId() {
        return pantsMeasurementId;
    }

    public void setPantsMeasurementId(Long pantsMeasurementId) {
        this.pantsMeasurementId = pantsMeasurementId;
    }
}
