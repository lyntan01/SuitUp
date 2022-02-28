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
public class CreditCardNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CreditCardNotFoundException</code>
     * without detail message.
     */
    public CreditCardNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CreditCardNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreditCardNotFoundException(String msg) {
        super(msg);
    }
}
