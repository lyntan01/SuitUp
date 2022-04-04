/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AppointmentSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmailSessionBeanLocal;
import ejb.session.stateless.TransactionSessionBeanLocal;
import entity.AppointmentEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
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
import util.exception.AppointmentNotFoundException;
import util.exception.CreateNewTransactionException;
import util.exception.InputDataValidationException;
import util.exception.OrderNotFoundException;
import util.exception.UnknownPersistenceException;
import util.generator.RandomStringGenerator;

/**
 *
 * @author keithcharleschan
 */
@Named(value = "appointmentManagementManagedBean")
@ViewScoped
public class AppointmentManagementManagedBean implements Serializable {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private EmailSessionBeanLocal emailSessionBeanLocal;

    @EJB
    private AppointmentSessionBeanLocal appointmentSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @Inject
    private ViewAppointmentManagedBean viewAppointmentManagedBean;

    private List<AppointmentEntity> appointmentEntities;
    private List<AppointmentEntity> filteredAppointmentEntities;

    private AppointmentEntity selectedAppointmentEntityToUpdate;
    private CustomerEntity customerOfAppointmentToPayFor;
    private List<CreditCardEntity> creditCards;
    private CreditCardEntity selectedCreditCard;
    private List<String> censoredCreditCards;
    private String selectedStringCreditCard;
    private Integer selectedCardPosition;

    private String verificationCodeToVerifyAgainst;
    private String providedVerificationCodeByCustomer;

    private double cost;

    public AppointmentManagementManagedBean() {
        appointmentEntities = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        appointmentEntities = appointmentSessionBeanLocal.retrieveAllAppointments();
    }

    public void doUpdateAppointment(ActionEvent event) {
        selectedAppointmentEntityToUpdate = (AppointmentEntity) event.getComponent().getAttributes().get("appointmentEntityToUpdate");
    }

    public void doPaymentForAppointment(ActionEvent event) throws Exception {
        censoredCreditCards = new ArrayList<>();

        selectedAppointmentEntityToUpdate = (AppointmentEntity) event.getComponent().getAttributes().get("appointmentEntityToPayFor");
        Long customerId = selectedAppointmentEntityToUpdate.getCustomer().getCustomerId();
        customerOfAppointmentToPayFor = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        creditCards = customerOfAppointmentToPayFor.getCreditCards();

        for (CreditCardEntity creditCard : creditCards) {
            censoredCreditCards.add("**** **** **** " + creditCard.getCardNumber().substring(12, 16));
        }

    }

    public ViewAppointmentManagedBean getViewAppointmentManagedBean() {
        return viewAppointmentManagedBean;
    }

    public void setViewAppointmentManagedBean(ViewAppointmentManagedBean viewAppointmentManagedBean) {
        this.viewAppointmentManagedBean = viewAppointmentManagedBean;
    }

    public List<AppointmentEntity> getAppointmentEntities() {
        return appointmentEntities;
    }

    public void setAppointmentEntities(List<AppointmentEntity> appointmentEntities) {
        this.appointmentEntities = appointmentEntities;
    }

    public List<AppointmentEntity> getFilteredAppointmentEntities() {
        return filteredAppointmentEntities;
    }

    public void setFilteredAppointmentEntities(List<AppointmentEntity> filteredAppointmentEntities) {
        this.filteredAppointmentEntities = filteredAppointmentEntities;
    }

    public AppointmentEntity getSelectedAppointmentEntityToUpdate() {
        return selectedAppointmentEntityToUpdate;
    }

    public void setSelectedAppointmentEntityToUpdate(AppointmentEntity selectedAppointmentEntityToUpdate) {
        this.selectedAppointmentEntityToUpdate = selectedAppointmentEntityToUpdate;
    }

    public CustomerEntity getCustomerOfAppointmentToPayFor() {
        return customerOfAppointmentToPayFor;
    }

    public void setCustomerOfAppointmentToPayFor(CustomerEntity customerOfAppointmentToPayFor) {
        this.customerOfAppointmentToPayFor = customerOfAppointmentToPayFor;
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

    public String getVerificationCodeToVerifyAgainst() {
        return verificationCodeToVerifyAgainst;
    }

    public void setVerificationCodeToVerifyAgainst(String verificationCodeToVerifyAgainst) {
        this.verificationCodeToVerifyAgainst = verificationCodeToVerifyAgainst;
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
        this.setSelectedCardPosition(censoredCreditCards.indexOf(selectedStringCreditCard));
        this.selectedCreditCard = creditCards.get(selectedCardPosition);

    }

    public Integer getSelectedCardPosition() {
        return selectedCardPosition;
    }

    public void setSelectedCardPosition(Integer selectedCardPosition) {
        this.selectedCardPosition = selectedCardPosition;
    }

    public void generateVerification(ActionEvent event) {
        RandomStringGenerator generator = new RandomStringGenerator(6);
        this.verificationCodeToVerifyAgainst = generator.nextString();
        emailSessionBeanLocal.emailVerificationCodeSync(selectedAppointmentEntityToUpdate, selectedStringCreditCard, verificationCodeToVerifyAgainst, customerOfAppointmentToPayFor, "keithccys@gmail.com");
    }

    public String getProvidedVerificationCodeByCustomer() {
        return providedVerificationCodeByCustomer;
    }

    public void setProvidedVerificationCodeByCustomer(String providedVerificationCodeByCustomer) {
        this.providedVerificationCodeByCustomer = providedVerificationCodeByCustomer;
    }

    public void doCompletePayment() {
        if (verificationCodeToVerifyAgainst.equals(providedVerificationCodeByCustomer)) {
            try {
                System.out.println("Verification successful");
                transactionSessionBeanLocal.createNewTransaction(new TransactionEntity(new BigDecimal("003"), new Date(), selectedAppointmentEntityToUpdate, null), selectedAppointmentEntityToUpdate.getAppointmentId(), null);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Verification successful. Payment completed!", null));
                verificationCodeToVerifyAgainst = "";
                //Add updating of payment status of the appointment to prevent further paying /extra checks
            } catch (AppointmentNotFoundException | CreateNewTransactionException | OrderNotFoundException | InputDataValidationException | UnknownPersistenceException ex) {
                System.out.println(ex.getMessage() + "Error");
            }

        } else {
            System.out.println("Verification failed");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Verification failed. Please provide the correct verification code!", null));
        }
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

   
}
