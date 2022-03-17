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
public class ColourNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ColourNotFoundException</code> without
     * detail message.
     */
    public ColourNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ColourNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ColourNotFoundException(String msg) {
        super(msg);
    }
}
