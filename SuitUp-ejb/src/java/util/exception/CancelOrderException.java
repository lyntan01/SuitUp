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
public class CancelOrderException extends Exception {

    /**
     * Creates a new instance of <code>CancelOrderException</code> without
     * detail message.
     */
    public CancelOrderException() {
    }

    /**
     * Constructs an instance of <code>CancelOrderException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CancelOrderException(String msg) {
        super(msg);
    }
}
