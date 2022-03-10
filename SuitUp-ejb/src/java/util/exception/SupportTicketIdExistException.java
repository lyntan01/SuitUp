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
public class SupportTicketIdExistException extends Exception {

    /**
     * Creates a new instance of <code>SupportTicketExistException</code>
     * without detail message.
     */
    public SupportTicketIdExistException() {
    }

    /**
     * Constructs an instance of <code>SupportTicketExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public SupportTicketIdExistException(String msg) {
        super(msg);
    }
}
