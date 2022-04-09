/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmailSessionBeanLocal;
import ejb.session.stateless.OrderSessionBeanLocal;
import ejb.session.stateless.TransactionSessionBeanLocal;
import entity.AppointmentEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.TransactionEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;
import util.enumeration.OrderStatusEnum;
import util.exception.CreateNewTransactionException;
import util.exception.InputDataValidationException;
import util.exception.OrderNotFoundException;
import util.exception.UnknownPersistenceException;
import util.generator.RandomStringGenerator;

/**
 *
 * @author keithcharleschan
 */
@Named(value = "orderManagementManagedBean")
@ViewScoped
public class OrderManagementManagedBean implements Serializable {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

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

    private OrderEntity orderEntityToPayFor;
    private CustomerEntity customerOfOrderToPayFor;
    private List<CreditCardEntity> creditCards;
    private CreditCardEntity selectedCreditCard;
    private List<String> censoredCreditCards;
    private String selectedStringCreditCard;
    private Integer selectedCardPosition;

    private String verificationCodeToVerifyAgainst;
    private String providedVerificationCodeByCustomer;

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

    public void doPaymentForOrder(ActionEvent event) throws Exception {
        censoredCreditCards = new ArrayList<>();

        orderEntityToPayFor = (OrderEntity) event.getComponent().getAttributes().get("orderEntityToPayFor");
        Long customerId = orderEntityToPayFor.getCustomer().getCustomerId();
        customerOfOrderToPayFor = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        creditCards = customerOfOrderToPayFor.getCreditCards();

        for (CreditCardEntity creditCard : creditCards) {
            censoredCreditCards.add("**** **** **** " + creditCard.getCardNumber().substring(12, 16));
        }

    }

    public OrderEntity getOrderEntityToPayFor() {
        return orderEntityToPayFor;
    }

    public void setOrderEntityToPayFor(OrderEntity orderEntityToPayFor) {
        this.orderEntityToPayFor = orderEntityToPayFor;
    }

    public CustomerEntity getCustomerOfOrderToPayFor() {
        return customerOfOrderToPayFor;
    }

    public void setCustomerOfOrderToPayFor(CustomerEntity customerOfOrderToPayFor) {
        this.customerOfOrderToPayFor = customerOfOrderToPayFor;
    }

    public List<CreditCardEntity> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardEntity> creditCards) {
        this.creditCards = creditCards;
    }

    public CreditCardEntity getSelectedCreditCard() {
        return selectedCreditCard;
    }

    public void setSelectedCreditCard(CreditCardEntity selectedCreditCard) {
        this.selectedCreditCard = selectedCreditCard;
    }

    public List<String> getCensoredCreditCards() {
        return censoredCreditCards;
    }

    public void setCensoredCreditCards(List<String> censoredCreditCards) {
        this.censoredCreditCards = censoredCreditCards;
    }

    public String getSelectedStringCreditCard() {
        return selectedStringCreditCard;
    }

    public void setSelectedStringCreditCard(String selectedStringCreditCard) {
        this.selectedStringCreditCard = selectedStringCreditCard;
    }

    public Integer getSelectedCardPosition() {
        return selectedCardPosition;
    }

    public void setSelectedCardPosition(Integer selectedCardPosition) {
        this.selectedCardPosition = selectedCardPosition;
    }

    public String getVerificationCodeToVerifyAgainst() {
        return verificationCodeToVerifyAgainst;
    }

    public void setVerificationCodeToVerifyAgainst(String verificationCodeToVerifyAgainst) {
        this.verificationCodeToVerifyAgainst = verificationCodeToVerifyAgainst;
    }

    public void generateVerification(ActionEvent event) {
        RandomStringGenerator generator = new RandomStringGenerator(6);
        this.verificationCodeToVerifyAgainst = generator.nextString();
        emailSessionBeanLocal.emailVerificationCodeSync(orderEntityToPayFor, selectedStringCreditCard, verificationCodeToVerifyAgainst, customerOfOrderToPayFor, "keithccys@gmail.com");
    }

    public String getProvidedVerificationCodeByCustomer() {
        return providedVerificationCodeByCustomer;
    }

    public void setProvidedVerificationCodeByCustomer(String providedVerificationCodeByCustomer) {
        this.providedVerificationCodeByCustomer = providedVerificationCodeByCustomer;
    }

    public void doCompletePayment(ActionEvent event) {

        if (verificationCodeToVerifyAgainst.equals(providedVerificationCodeByCustomer)) {

            if (orderEntityToPayFor.getTransaction() != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Order has been paid for. Please close this window.", null));
            } else {
                try {
                    System.out.println("Verification successful");
                    transactionSessionBeanLocal.createNewTransaction(new TransactionEntity(orderEntityToPayFor.getTotalAmount(), new Date(), null, orderEntityToPayFor), null, orderEntityToPayFor.getOrderId());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Verification successful. Payment completed! Please close this window.", null));
                    //Need update the order status to paid and boolenan variable.
                    verificationCodeToVerifyAgainst = "";
                    //Add updating of payment status of the appointment to prevent further paying /extra checks
                } catch (AppointmentNotFoundException | CreateNewTransactionException | OrderNotFoundException | InputDataValidationException | UnknownPersistenceException ex) {
                    System.out.println(ex.getMessage() + "Error");
                }

            }

        } else {
            System.out.println("Verification failed");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Verification failed. Please provide the correct verification code!", null));
        }
    }

}
