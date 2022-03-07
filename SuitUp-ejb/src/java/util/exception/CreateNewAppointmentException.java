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
public class CreateNewAppointmentException extends Exception {

    public CreateNewAppointmentException() {
    }

    public CreateNewAppointmentException(String msg) {
        super(msg);
    }
}
