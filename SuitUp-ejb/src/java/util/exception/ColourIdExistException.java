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
public class ColourIdExistException extends Exception {

    /**
     * Creates a new instance of <code>ColourIdExistException</code> without
     * detail message.
     */
    public ColourIdExistException() {
    }

    /**
     * Constructs an instance of <code>ColourIdExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ColourIdExistException(String msg) {
        super(msg);
    }
}
