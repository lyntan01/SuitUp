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
public class StaffUsernameExistException extends Exception {

    /**
     * Creates a new instance of <code>StaffUsernameExistException</code>
     * without detail message.
     */
    public StaffUsernameExistException() {
    }

    /**
     * Constructs an instance of <code>StaffUsernameExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public StaffUsernameExistException(String msg) {
        super(msg);
    }
}
