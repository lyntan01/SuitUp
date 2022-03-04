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
public class PromotionCodeExpiredException extends Exception {

    /**
     * Creates a new instance of <code>PromotionCodeExpiredException</code>
     * without detail message.
     */
    public PromotionCodeExpiredException() {
    }

    /**
     * Constructs an instance of <code>PromotionCodeExpiredException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public PromotionCodeExpiredException(String msg) {
        super(msg);
    }
}
