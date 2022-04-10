/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.PantsMeasurementEntity;

/**
 *
 * @author xianhui
 */
public class UpdatePantsMeasurementReq {
    private String email;
    private String password;
    private PantsMeasurementEntity pantsMeasurement;

    public UpdatePantsMeasurementReq(String email, String password, PantsMeasurementEntity pantsMeasurement) {
        this.email = email;
        this.password = password;
        this.pantsMeasurement = pantsMeasurement;

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

    public PantsMeasurementEntity getPantsMeasurement() {
        return pantsMeasurement;
    }

    public void setPantsMeasurement(PantsMeasurementEntity pantsMeasurement) {
        this.pantsMeasurement = pantsMeasurement;
    }
}
