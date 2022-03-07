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
public class EntityInstanceExistsInCollectionException extends Exception {

    public EntityInstanceExistsInCollectionException() {
    }

    public EntityInstanceExistsInCollectionException(String msg) {
        super(msg);
    }
}
