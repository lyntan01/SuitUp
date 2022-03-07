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
public class ChangePasswordException extends Exception {

    public ChangePasswordException() {
    }

    public ChangePasswordException(String msg) {
        super(msg);
    }
}
