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
public class CustomizationNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CustomizationNotFoundException</code>
     * without detail message.
     */
    public CustomizationNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CustomizationNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomizationNotFoundException(String msg) {
        super(msg);
    }
}
