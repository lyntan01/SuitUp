/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.JacketMeasurementEntity;

/**
 *
 * @author xianhui
 */
public class CreateJacketMeasurementReq {

    private String email;
    private String password;
    private JacketMeasurementEntity jacketMeasurement;

    public CreateJacketMeasurementReq() {
    }
    

    public CreateJacketMeasurementReq(String email, String password, JacketMeasurementEntity jacketMeasurement) {
        this.email = email;
        this.password = password;
        this.jacketMeasurement = jacketMeasurement;

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

    public JacketMeasurementEntity getJacketMeasurement() {
        return jacketMeasurement;
    }

    public void setJacketMeasurement(JacketMeasurementEntity jacketMeasurement) {
        this.jacketMeasurement = jacketMeasurement;
    }
}
