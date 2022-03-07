/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author keithcharleschan
 */
public class AppointmentNotFoundException extends Exception {

    public AppointmentNotFoundException() {
    }

    public AppointmentNotFoundException(String msg) {
        super(msg);
    }
}
