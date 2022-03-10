/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author meganyee
 */
public class UnsuccessfulTicketException extends Exception {

    /**
     * Creates a new instance of <code>UnsuccessfulTicketException</code>
     * without detail message.
     */
    public UnsuccessfulTicketException() {
    }

    /**
     * Constructs an instance of <code>UnsuccessfulTicketException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UnsuccessfulTicketException(String msg) {
        super(msg);
    }
}
