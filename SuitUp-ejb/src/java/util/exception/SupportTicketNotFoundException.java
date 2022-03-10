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
public class SupportTicketNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>SupportTicketNotFoundException</code>
     * without detail message.
     */
    public SupportTicketNotFoundException() {
    }

    /**
     * Constructs an instance of <code>SupportTicketNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SupportTicketNotFoundException(String msg) {
        super(msg);
    }
}
