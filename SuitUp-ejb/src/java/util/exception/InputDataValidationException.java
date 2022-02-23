/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author lyntan
 */
public class InputDataValidationException extends Exception {

    /**
     * Creates a new instance of <code>InputDataValidationException</code>
     * without detail message.
     */
    public InputDataValidationException() {
    }

    /**
     * Constructs an instance of <code>InputDataValidationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InputDataValidationException(String msg) {
        super(msg);
    }
}
