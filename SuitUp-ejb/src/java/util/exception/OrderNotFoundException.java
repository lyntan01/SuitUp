/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author keithcharleschan
 */
public class OrderNotFoundException extends Exception {

    public OrderNotFoundException() {
    }

    public OrderNotFoundException(String msg) {
        super(msg);
    }
}
