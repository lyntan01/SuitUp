/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.OrderEntity;
import java.util.concurrent.Future;
import javax.ejb.Local;

/**
 *
 * @author keithcharleschan
 */
@Local
public interface EmailSessionBeanLocal {

    public Boolean emailCheckoutNotificationSync(OrderEntity orderEntity, String toEmailAddress);

    public Future<Boolean> emailCheckoutNotificationAsync(OrderEntity orderEntity, String toEmailAddress) throws InterruptedException;

    public Boolean emailVerificationCodeSync(AppointmentEntity appointmentEntity, String selectedCreditCard, String verificationCode, CustomerEntity customerEntity, String toEmailAddress);

    public Future<Boolean> emailVerificationCodeAsync(AppointmentEntity appointmentEntity, String selectedCreditCard, String verificationCode, CustomerEntity customerEntity, String toEmailAddress);

    public Boolean emailUpdatedOrderStatusSync(OrderEntity orderEntity, CustomerEntity customerEntity, String toEmailAddress);

    public Future<Boolean> emailUpdatedOrderStatusAsync(OrderEntity orderEntity, CustomerEntity customerEntity, String toEmailAddress);

    public Boolean emailVerificationCodeSync(OrderEntity orderEntity, String selectedCreditCard, String verificationCode, CustomerEntity customerEntity, String toEmailAddress);

    public Future<Boolean> emailVerificationCodeAsync(OrderEntity orderEntity, String selectedCreditCard, String verificationCode, CustomerEntity customerEntity, String toEmailAddress);


}
