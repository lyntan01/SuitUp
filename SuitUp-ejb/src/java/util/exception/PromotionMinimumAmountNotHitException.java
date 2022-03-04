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
public class PromotionMinimumAmountNotHitException extends Exception {

    /**
     * Creates a new instance of
     * <code>PromotionMinimumAmountNotHitException</code> without detail
     * message.
     */
    public PromotionMinimumAmountNotHitException() {
    }

    /**
     * Constructs an instance of
     * <code>PromotionMinimumAmountNotHitException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public PromotionMinimumAmountNotHitException(String msg) {
        super(msg);
    }
}
