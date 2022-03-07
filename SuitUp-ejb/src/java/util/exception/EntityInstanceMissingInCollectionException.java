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
public class EntityInstanceMissingInCollectionException extends Exception {

    public EntityInstanceMissingInCollectionException() {
    }

    public EntityInstanceMissingInCollectionException(String msg) {
        super(msg);
    }
}
