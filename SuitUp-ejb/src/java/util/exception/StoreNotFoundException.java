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
public class StoreNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>StoreNotFoundException</code> without
     * detail message.
     */
    public StoreNotFoundException() {
    }

    /**
     * Constructs an instance of <code>StoreNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public StoreNotFoundException(String msg) {
        super(msg);
    }
}
