/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.CustomizedJacketEntity;
import static entity.CustomizedJacketEntity_.jacketMeasurement;

/**
 *
 * @author xianhui
 */
public class CreateCustomizedJacketReq {
    private String email;
    private String password;
    private CustomizedJacketEntity customizedJacket;
    private Long pocketStyleId;
    private Long jacketStyleId; 
    private Long innerFabricId;
    private Long outerFabricId; 
    private Long jacketMeasurementId;

    public CreateCustomizedJacketReq(String email, String password, CustomizedJacketEntity customizedJacket,Long pocketStyleId, Long jacketStyleId, Long innerFabricId, Long outerFabricId, Long jacketMeasurementId) {
        this.email = email;
        this.password = password;
        this.customizedJacket = customizedJacket;
        this.pocketStyleId = pocketStyleId;
        this.jacketStyleId = jacketStyleId;
        this.innerFabricId = innerFabricId;
        this.outerFabricId = outerFabricId;
        this.jacketMeasurementId = jacketMeasurementId;
        

    }

    public CreateCustomizedJacketReq() {
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

    public CustomizedJacketEntity getCustomizedJacket() {
        return customizedJacket;
    }

    public void setCustomizedJacket(CustomizedJacketEntity customizedJacket) {
        this.customizedJacket = customizedJacket;
    }

    public Long getPocketStyleId() {
        return pocketStyleId;
    }

    public void setPocketStyleId(Long pocketStyleId) {
        this.pocketStyleId = pocketStyleId;
    }

    public Long getJacketStyleId() {
        return jacketStyleId;
    }

    public void setJacketStyleId(Long jacketStyleId) {
        this.jacketStyleId = jacketStyleId;
    }

    public Long getInnerFabricId() {
        return innerFabricId;
    }

    public void setInnerFabricId(Long innerFabricId) {
        this.innerFabricId = innerFabricId;
    }

    public Long getOuterFabricId() {
        return outerFabricId;
    }

    public void setOuterFabricId(Long outerFabricId) {
        this.outerFabricId = outerFabricId;
    }

    public Long getJacketMeasurementId() {
        return jacketMeasurementId;
    }

    public void setJacketMeasurementId(Long jacketMeasurementId) {
        this.jacketMeasurementId = jacketMeasurementId;
    }

}
