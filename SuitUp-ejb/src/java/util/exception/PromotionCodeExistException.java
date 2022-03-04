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
public class PromotionCodeExistException extends Exception {

    /**
     * Creates a new instance of <code>PromotionCodeExistException</code>
     * without detail message.
     */
    public PromotionCodeExistException() {
    }

    /**
     * Constructs an instance of <code>PromotionCodeExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public PromotionCodeExistException(String msg) {
        super(msg);
    }
}
