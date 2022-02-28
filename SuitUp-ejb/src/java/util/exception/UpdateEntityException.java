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
public class UpdateEntityException extends Exception {

    /**
     * Creates a new instance of <code>UpdateEntityException</code> without
     * detail message.
     */
    public UpdateEntityException() {
    }

    /**
     * Constructs an instance of <code>UpdateEntityException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateEntityException(String msg) {
        super(msg);
    }
}
