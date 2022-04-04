/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EmailSessionBeanLocal;
import ejb.session.stateless.OrderSessionBeanLocal;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;
import util.enumeration.OrderStatusEnum;
import util.exception.OrderNotFoundException;

/**
 *
 * @author keithcharleschan
 */
@Named(value = "orderManagementManagedBean")
@ViewScoped
public class OrderManagementManagedBean implements Serializable {

    @EJB
    private EmailSessionBeanLocal emailSessionBeanLocal;

    @EJB
    private OrderSessionBeanLocal orderSessionBeanLocal;

    @Inject
    private ViewOrderManagedBean viewOrderManagedBean;

    private List<OrderEntity> orderEntities;
    private List<OrderEntity> filteredOrderEntities;

    private OrderEntity selectedOrderEntityToUpdate;
    private List<OrderLineItemEntity> filteredOrderLineItemEntities;

    private String orderStatusEnumStringUpdate;

    public OrderManagementManagedBean() {
        orderEntities = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        orderEntities = orderSessionBeanLocal.retrieveAllOrders();
    }

    public void updateOrderStatusChange(SelectEvent event) {
        orderStatusEnumStringUpdate = String.valueOf(event.getObject());

        try {
            selectedOrderEntityToUpdate.setOrderStatusEnum(OrderStatusEnum.valueOf(orderStatusEnumStringUpdate));
            orderSessionBeanLocal.updateOrder(selectedOrderEntityToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order Status Updated Successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void doUpdateOrder(ActionEvent event) {
        selectedOrderEntityToUpdate = (OrderEntity) event.getComponent().getAttributes().get("orderEntityToUpdate");
        orderStatusEnumStringUpdate = selectedOrderEntityToUpdate.getOrderStatusEnum().getName();
        filteredOrderLineItemEntities = new ArrayList<>(selectedOrderEntityToUpdate.getOrderLineItems());
    }

    public ViewOrderManagedBean getViewOrderManagedBean() {
        return viewOrderManagedBean;
    }

    public void setViewOrderManagedBean(ViewOrderManagedBean viewOrderManagedBean) {
        this.viewOrderManagedBean = viewOrderManagedBean;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public List<OrderEntity> getFilteredOrderEntities() {
        return filteredOrderEntities;
    }

    public void setFilteredOrderEntities(List<OrderEntity> filteredOrderEntities) {
        this.filteredOrderEntities = filteredOrderEntities;
    }

    public OrderEntity getSelectedOrderEntityToUpdate() {
        return selectedOrderEntityToUpdate;
    }

    public void setSelectedOrderEntityToUpdate(OrderEntity selectedOrderEntityToUpdate) {
        this.selectedOrderEntityToUpdate = selectedOrderEntityToUpdate;
    }

    public List<OrderLineItemEntity> getFilteredOrderLineItemEntities() {
        return filteredOrderLineItemEntities;
    }

    public void setFilteredOrderLineItemEntities(List<OrderLineItemEntity> filteredOrderLineItemEntities) {
        this.filteredOrderLineItemEntities = filteredOrderLineItemEntities;
    }

    public String getOrderStatusEnumStringUpdate() {
        return orderStatusEnumStringUpdate;
    }

    public void setOrderStatusEnumStringUpdate(String orderStatusEnumStringUpdate) {
        this.orderStatusEnumStringUpdate = orderStatusEnumStringUpdate;
    }

    public void sendEmail(ActionEvent event) {
        emailSessionBeanLocal.emailUpdatedOrderStatusAsync(selectedOrderEntityToUpdate, selectedOrderEntityToUpdate.getCustomer(), "keithccys@gmail.com");
    }

}
