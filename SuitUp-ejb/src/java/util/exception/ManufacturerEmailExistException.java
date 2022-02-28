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
public class ManufacturerEmailExistException extends Exception {

    /**
     * Creates a new instance of <code>ManufacturerEmailExistException</code>
     * without detail message.
     */
    public ManufacturerEmailExistException() {
    }

    /**
     * Constructs an instance of <code>ManufacturerEmailExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ManufacturerEmailExistException(String msg) {
        super(msg);
    }
}
