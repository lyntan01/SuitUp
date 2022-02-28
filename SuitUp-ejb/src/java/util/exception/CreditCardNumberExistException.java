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
public class CreditCardNumberExistException extends Exception {

    /**
     * Creates a new instance of <code>CreditCardNumberExistException</code>
     * without detail message.
     */
    public CreditCardNumberExistException() {
    }

    /**
     * Constructs an instance of <code>CreditCardNumberExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreditCardNumberExistException(String msg) {
        super(msg);
    }
}
