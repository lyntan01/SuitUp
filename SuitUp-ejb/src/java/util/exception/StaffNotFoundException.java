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
public class StaffNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>StaffNotFoundException</code> without
     * detail message.
     */
    public StaffNotFoundException() {
    }

    /**
     * Constructs an instance of <code>StaffNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public StaffNotFoundException(String msg) {
        super(msg);
    }
}
