/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.enumeration;

/**
 *
 * @author lyntan
 */
public enum AccessRightEnum {
    TAILOR("Tailor"),
    CASHIER("Cashier"),
    MANAGER("Manager");
    
    private final String name;

    AccessRightEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
}
