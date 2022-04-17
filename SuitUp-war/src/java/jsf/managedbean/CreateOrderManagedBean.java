/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AddressSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.CustomizedJacketSessionBeanLocal;
import ejb.session.stateless.CustomizedPantsSessionBeanLocal;
import ejb.session.stateless.OrderSessionBeanLocal;
import ejb.session.stateless.PromotionSessionBeanLocal;
import entity.AddressEntity;
import entity.CustomerEntity;
import entity.CustomizedJacketEntity;
import entity.CustomizedPantsEntity;
import entity.CustomizedProductEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.ProductEntity;
import entity.PromotionEntity;
import entity.StandardProductEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.enumeration.CollectionMethodEnum;
import util.enumeration.OrderStatusEnum;
import util.exception.AddressNotFoundException;
import util.exception.CreateNewOrderException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomizedProductNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.PromotionNotFoundException;
import util.generator.RandomStringGenerator;

/**
 *
 * @author meganyee
 */
@Named(value = "createOrderManagedBean")
@ViewScoped
public class CreateOrderManagedBean implements Serializable {

    @EJB
    private AddressSessionBeanLocal addressSessionBeanLocal;

    @EJB
    private PromotionSessionBeanLocal promotionSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private CustomizedPantsSessionBeanLocal customizedPantsSessionBeanLocal;

    @EJB
    private CustomizedJacketSessionBeanLocal customizedJacketSessionBeanLocal;

    @EJB
    private OrderSessionBeanLocal orderSessionBeanLocal;

    private OrderEntity newOrder;
    private List<OrderLineItemEntity> orderLineItemsEntities;
    private Integer totalLineItem;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private String collectionMethod;
    private Boolean expressOrder;
//    private AddressEntity deliveryAddress;
    private Long selectedDeliveryAddress;

    private String email;
    private CustomerEntity currentCustomer;

    private String promotionCode;

    public CreateOrderManagedBean() {
        initialiseState();
    }

    private void initialiseState() {
        orderLineItemsEntities = new ArrayList<>();
        newOrder = new OrderEntity();
        totalLineItem = 0;
        totalQuantity = 0;
        totalAmount = new BigDecimal("0.00");
        email = null;
        currentCustomer = null;
        collectionMethod = null;
        expressOrder = null;
        selectedDeliveryAddress = -1L;
        RandomStringGenerator generator = new RandomStringGenerator(5);
        String orderSerialNumber = generator.generateSerial();
    }

    public void createNewOrder(ActionEvent event) {
        newOrder.setCollectionMethodEnum(CollectionMethodEnum.valueOf(collectionMethod));
        newOrder.setCustomer(currentCustomer);
        newOrder.setOrderLineItems(orderLineItemsEntities);
        newOrder.setExpressOrder(expressOrder);

        newOrder.setOrderStatusEnum(OrderStatusEnum.UNPAID);
        newOrder.setTotalLineItem(orderLineItemsEntities.size());
        newOrder.setOrderDateTime(new Date());
        RandomStringGenerator generator = new RandomStringGenerator(5);
        newOrder.setSerialNumber(generator.generateSerial());
        
        double total = 0;
        
//        BigDecimal totalForOrder = BigDecimal.ZERO;
        int totalQuantity = 0;
        for(OrderLineItemEntity orderLine : orderLineItemsEntities) {
            System.out.println("---ITERATING THRU orderLine--" + orderLine.getSubTotal());
            double val = orderLine.getSubTotal().doubleValue();
            total += val;
            System.out.println(total);
//            BigDecimal val = orderLine.getSubTotal();
//            System.out.println("val" + val);
//            totalForOrder.add(val);
            totalQuantity += orderLine.getQuantity();
        }
        
        BigDecimal totalForOrder = BigDecimal.valueOf(total);
        
        newOrder.setTotalAmount(totalForOrder);
        System.out.println("totalForOrder : " + totalForOrder);
        System.out.println(" newOrder.getTotalAmount: " + newOrder.getTotalAmount());
        newOrder.setTotalQuantity(totalQuantity);
        

        if (selectedDeliveryAddress != -1L) { //CHANGEEEE
            try {
                AddressEntity address = addressSessionBeanLocal.retrieveAddressByAddressId(selectedDeliveryAddress);
                newOrder.setDeliveryAddress(address);
            } catch (AddressNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
            }
        }

        System.out.println(newOrder.getOrderLineItems());

        System.out.println("");

        try {
//            System.out.println(newOrder);
            orderSessionBeanLocal.createNewOfflineOrder(currentCustomer.getCustomerId(), selectedDeliveryAddress, newOrder);
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order created succesfully!", null));
            System.out.println("********** CREATED AN ORDER");
        } catch (AddressNotFoundException | CreateNewOrderException | CustomerNotFoundException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }

        // call promotions
    }

    public void retrieveCustomer(ActionEvent event) {
        try {
            currentCustomer = customerSessionBeanLocal.retrieveCustomerByEmail(email);
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving customer: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public BigDecimal retrievePricePerItem(ProductEntity product) {
        if (product instanceof StandardProductEntity) {
            return ((StandardProductEntity) product).getUnitPrice();
        } else if (product instanceof CustomizedProductEntity) {
            return ((CustomizedProductEntity) product).getTotalPrice();
        }

        return null;
    }

    public BigDecimal retrieveTotalQuantity() {
        try {
            PromotionEntity promotion = promotionSessionBeanLocal.retrievePromotionByPromotionCode(promotionCode);

        } catch (PromotionNotFoundException ex) {

            return totalAmount;
        }

        return null;
    }

    public void incrementQuantity(ActionEvent event) {
        ProductEntity product = (ProductEntity) event.getComponent().getAttributes().get("productToIncrement");
        System.out.println(product);
        this.addItem(product, 1);
    }

    public void decrementQuantity(ActionEvent event) {
        ProductEntity product = (ProductEntity) event.getComponent().getAttributes().get("productToDecrement");
        this.removeItem(product, -1);
    }

    public void addItem(ProductEntity product, int quantity) {

        System.out.println(product);

        boolean hasItem = false;

        BigDecimal unitPrice = new BigDecimal("0.00");

        if (product instanceof StandardProductEntity) {
            unitPrice = ((StandardProductEntity) product).getUnitPrice();
        } else if (product instanceof CustomizedProductEntity) {
            unitPrice = ((CustomizedProductEntity) product).getTotalPrice();
        }

        if (orderLineItemsEntities.size() > 0) {
            for (OrderLineItemEntity orderItem : orderLineItemsEntities) {
                if (product instanceof StandardProductEntity) {
                    if (orderItem.getProduct().getProductId().equals(product.getProductId())) {
                        hasItem = true;

                        orderItem.setQuantity(orderItem.getQuantity() + quantity);
                        orderItem.setSubTotal(unitPrice.multiply(new BigDecimal(orderItem.getQuantity())));
                        System.out.println("orderItem.setSubTotal" + orderItem.getSubTotal());
                        
                        totalAmount = totalAmount.add(unitPrice.multiply(new BigDecimal(quantity)));
                        totalQuantity += quantity;
                    }
                } else if (product instanceof CustomizedProductEntity) {
                    if (orderItem.getProduct().getName().equals(product.getName())) {
                        hasItem = true;

                        orderItem.setQuantity(orderItem.getQuantity() + quantity);
                        orderItem.setSubTotal(unitPrice.multiply(new BigDecimal(orderItem.getQuantity())));
                        System.out.println("orderItem.setSubTotal" + orderItem.getSubTotal());

                        totalAmount = totalAmount.add(unitPrice.multiply(new BigDecimal(quantity)));
                        totalQuantity += quantity;
                    }
                }
            }
        }

        if (!hasItem) {
            BigDecimal subTotal = unitPrice.multiply(new BigDecimal(quantity));
            totalLineItem++;
            totalQuantity += quantity;
            totalAmount = totalAmount.add(subTotal);

            System.out.println("subTotal--" + subTotal);
            orderLineItemsEntities.add(new OrderLineItemEntity(quantity, subTotal, product));

        }
    }

    public void removeItem(ProductEntity product, int quantity) {

        boolean hasItem = false;

        for (int i = 0; i < orderLineItemsEntities.size(); i++) {

            OrderLineItemEntity orderItem = orderLineItemsEntities.get(i);

            if (orderItem.getProduct().getProductId().equals(product.getProductId())) {

                BigDecimal originalQty = BigDecimal.valueOf(orderItem.getQuantity());
                BigDecimal unitPrice = new BigDecimal("0.00");

                if (product instanceof StandardProductEntity) {
                    unitPrice = ((StandardProductEntity) product).getUnitPrice();
                } else if (product instanceof CustomizedProductEntity) {
                    unitPrice = ((CustomizedProductEntity) product).getTotalPrice();
                }

                if (orderItem.getQuantity() - quantity == 0) {

                    orderItem.setQuantity(0);
                    orderItem.setSubTotal(new BigDecimal("0.00"));

                    orderLineItemsEntities.remove(orderItem);
                    totalAmount = totalAmount.subtract(unitPrice.multiply(originalQty));
                    totalQuantity -= quantity;
                    totalLineItem--;

                    if (product instanceof CustomizedJacketEntity) {
                        CustomizedJacketEntity jacketToDelete = (CustomizedJacketEntity) product;
                        try {
                            customizedJacketSessionBeanLocal.deleteCustomizedJacket(jacketToDelete.getProductId());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, jacketToDelete.getProductId() + " deleted successfully", null));
                        } catch (CustomizedProductNotFoundException | DeleteEntityException ex) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting customized jacket: " + ex.getMessage(), null));
                        } catch (Exception ex) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
                        }
                    } else if (product instanceof CustomizedPantsEntity) {
                        CustomizedPantsEntity pantsToDelete = (CustomizedPantsEntity) product;
                        try {
                            customizedPantsSessionBeanLocal.deleteCustomizedPants(pantsToDelete.getProductId());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, pantsToDelete.getProductId() + " deleted successfully", null));
                        } catch (CustomizedProductNotFoundException | DeleteEntityException ex) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting customized pants: " + ex.getMessage(), null));
                        } catch (Exception ex) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
                        }
                    }

                } else {

                    orderItem.setQuantity(orderItem.getQuantity() + quantity);
                    orderItem.setSubTotal(unitPrice.multiply(new BigDecimal(orderItem.getQuantity())));
                    totalAmount = totalAmount.subtract(unitPrice.multiply(BigDecimal.valueOf(quantity)));
                    totalQuantity -= quantity;

                }
            }
        }
    }

    public List<OrderLineItemEntity> getOrderLineItemEntities() {
        return orderLineItemsEntities;
    }

    public void setOrderLineItemEntities(List<OrderLineItemEntity> orderLineItemsEntities) {
        for (OrderLineItemEntity orderItem : orderLineItemsEntities) {
            if (orderItem.getQuantity() == 0) {
                orderLineItemsEntities.remove(orderItem);
            }
        }

        this.orderLineItemsEntities = orderLineItemsEntities;
    }

    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public Boolean getExpressOrder() {
        return expressOrder;
    }

    public void setExpressOrder(Boolean expressOrder) {
        this.expressOrder = expressOrder;
    }
//    
//    public AddressEntity getDeliveryAddress() {
//        return deliveryAddress;
//    }
//
//    public void setDeliveryAddress(AddressEntity deliveryAddress) {
//        this.deliveryAddress = deliveryAddress;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public OrderEntity getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(OrderEntity newOrder) {
        this.newOrder = newOrder;
    }

    public Long getSelectedDeliveryAddress() {
        return selectedDeliveryAddress;
    }

    public void setSelectedDeliveryAddress(Long selectedDeliveryAddress) {
        this.selectedDeliveryAddress = selectedDeliveryAddress;
    }

}
