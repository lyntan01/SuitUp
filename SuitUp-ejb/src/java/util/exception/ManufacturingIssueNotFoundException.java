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
public class ManufacturingIssueNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>ManufacturingIssueNotFoundException</code> without detail message.
     */
    public ManufacturingIssueNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>ManufacturingIssueNotFoundException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ManufacturingIssueNotFoundException(String msg) {
        super(msg);
    }
}
