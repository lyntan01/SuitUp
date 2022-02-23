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
public class UpdateStaffException extends Exception {

    /**
     * Creates a new instance of <code>UpdateStaffException</code> without
     * detail message.
     */
    public UpdateStaffException() {
    }

    /**
     * Constructs an instance of <code>UpdateStaffException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateStaffException(String msg) {
        super(msg);
    }
}
