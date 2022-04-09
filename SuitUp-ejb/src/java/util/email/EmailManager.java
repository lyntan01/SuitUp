/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.email;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author keithcharleschan
 */
public class EmailManager {

    private final String emailServerName = "smtp.gmail.com";
    private final String mailer = "JavaMailer";
    private String smtpAuthUser;
    private String smtpAuthPassword;

    public EmailManager() {
    }

    public EmailManager(String smtpAuthUser, String smtpAuthPassword) {
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPassword = smtpAuthPassword;
    }

    public Boolean emailCheckoutNotification(OrderEntity orderEntity, String fromEmailAddress, String toEmailAddress) {
        String emailBody = "";

        emailBody += "You have completed the checkout successfully for Order ID: " + orderEntity.getOrderId() + "\n\n";

//        emailBody += "You have completed the checkout successfully for Sale Transaction ID: " + saleTransactionEntity.getSaleTransactionId() + "\n\n";
//        emailBody += "S/N     SKU Code     Product Name     Quantity     Unit Price     Sub-Total\n\n";
//
//        int count = 1;
//        for (OrderLineItemEntity orderLineItem : orderEntity.getOrderLineItems()) {
//            emailBody += count
//                    + "     " + orderLineItem.getProduct().get()
//                    + "     " + saleTransactionLineItemEntity.getProductEntity().getName()
//                    + "     " + saleTransactionLineItemEntity.getQuantity()
//                    + "     " + NumberFormat.getCurrencyInstance().format(saleTransactionLineItemEntity.getUnitPrice())
//                    + "     " + NumberFormat.getCurrencyInstance().format(saleTransactionLineItemEntity.getSubTotal()) + "\n";
//
//            count++;
//        }
//
//
//        emailBody += "\nTotal Line Item: " + orderEntity.getTotalOrderItem() + ", Total Quantity: " + orderEntity.getTotalQuantity() + ", Total Amount: " + NumberFormat.getCurrencyInstance().format(orderEntity.getTotalAmount()) + "\n";
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Receipt for Order #" + orderEntity.getSerialNumber());
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);

                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                Transport.send(msg);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    //Email Used to Send Verification Code for Appointment
    public Boolean emailVerificationCode(AppointmentEntity appointmentEntity, String selectedCreditCard, String verificationCode, CustomerEntity customerEntity, String fromEmailAddress, String toEmailAddress) {
        String emailBody = "Dear " + customerEntity.getFirstName() + "," + "\n";
        emailBody += " \n";
        emailBody += "You are currently paying for " + appointmentEntity.getAppointmentTypeEnum() + "\n";
        emailBody += "You have selected credit card: " + selectedCreditCard + "\n";
        emailBody += "Your verification code for payment is: " + verificationCode + "\n";
        emailBody += "Please provide the code when requested to confirm and proceed with the payment process.";

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Verification code to complete payment process");
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);

                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                Transport.send(msg);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    //Email for Updated Order Status
    public Boolean emailUpdatedOrderStatus(OrderEntity orderEntity, CustomerEntity customerEntity, String fromEmailAddress, String toEmailAddress) {
        String emailBody = "Dear " + customerEntity.getFirstName() + "," + "\n";
        emailBody += " \n";
        emailBody += "Your order #" + orderEntity.getSerialNumber() + " has been updated to " + orderEntity.getOrderStatusEnum() + ".";

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Order #" + orderEntity.getSerialNumber() + " has been updated to " + orderEntity.getOrderStatusEnum());
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);

                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                Transport.send(msg);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    //Email Used to Send Verification Code for Order
    public Boolean emailVerificationCode(OrderEntity orderEntity, String selectedCreditCard, String verificationCode, CustomerEntity customerEntity, String fromEmailAddress, String toEmailAddress) {
        String emailBody = "Dear " + customerEntity.getFirstName() + "," + "\n";
        emailBody += " \n";
        emailBody += "You are currently paying for " + orderEntity.getSerialNumber() + "\n";
        emailBody += "You have selected credit card: " + selectedCreditCard + "\n";
        emailBody += "Your verification code for payment is: " + verificationCode + "\n";
        emailBody += "Please provide the code when requested to confirm and proceed with the payment process.";

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Verification code to complete payment process");
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);

                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                Transport.send(msg);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

}
