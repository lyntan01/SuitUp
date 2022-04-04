/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import java.util.concurrent.Future;
import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import util.email.EmailManager;

/**
 *
 * @author keithcharleschan
 */
@Stateless
public class EmailSessionBean implements EmailSessionBeanLocal {

    private final String FROM_EMAIL_ADDRESS = "SuitUp <no.reply.suitup@gmail.com>";
    private final String USERNAME = "no.reply.suitup@gmail.com";
    private final String PASSWORD = "suitupstonks";

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public Boolean emailCheckoutNotificationSync(OrderEntity orderEntity, String toEmailAddress) {

        EmailManager emailManager = new EmailManager(USERNAME, PASSWORD);
        Boolean sentEmail = emailManager.emailCheckoutNotification(orderEntity, FROM_EMAIL_ADDRESS, toEmailAddress);

        if (sentEmail == true) {
            System.out.print("Returned True for emailCheckoutNotificationSync");
            return true;
        } else {
            System.out.print("Returned False for emailCheckoutNotificationSync");
            return false;
        }

    }

    @Asynchronous
    @Override
    public Future<Boolean> emailCheckoutNotificationAsync(OrderEntity orderEntity, String toEmailAddress) throws InterruptedException {

        EmailManager emailManager = new EmailManager(USERNAME, PASSWORD);
        Boolean sentEmail = emailManager.emailCheckoutNotification(orderEntity, FROM_EMAIL_ADDRESS, toEmailAddress);

        if (sentEmail == true) {
            System.out.print("Returned AsyncResult True for emailCheckoutNotificationAsync");
            return new AsyncResult<>(true);
        } else {
            System.out.print("Returned AsyncResult False for emailCheckoutNotificationAsync");
            return new AsyncResult<>(false);
        }

    }

}
