/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.enumeration;

/**
 *
 * @author keithcharleschan
 */
public enum OrderStatusEnum {
    UNPAID("Unpaid"),
    PAID("Paid"),
    PROCESSING("Processing"),
    READY_FOR_PICKUP("Ready For Pickup"),
    PICKED_UP("Picked Up"),
    IN_DELIVERY("In Delivery"),
    DELIVERED("Delivered"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");
    
    private final String name;

    OrderStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
