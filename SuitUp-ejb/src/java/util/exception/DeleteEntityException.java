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
public class DeleteEntityException extends Exception {

    /**
     * Creates a new instance of <code>DeleteEntityException</code> without
     * detail message.
     */
    public DeleteEntityException() {
    }

    /**
     * Constructs an instance of <code>DeleteEntityException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteEntityException(String msg) {
        super(msg);
    }
}
