/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmailSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.OrderEntity;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author lyntan
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private EmailSessionBeanLocal emailSessionBeanLocal;

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            staffSessionBeanLocal.retrieveStaffByUsername("manager");
//            emailSessionBeanLocal.emailCheckoutNotificationSync(new OrderEntity(), "LynRani16@gmail.com");
//            emailSessionBeanLocal.emailCheckoutNotificationSync(new OrderEntity(), "MeganYee1404@gmail.com");
//            emailSessionBeanLocal.emailCheckoutNotificationSync(new OrderEntity(), "keexianhui@gmail.com");
//            emailSessionBeanLocal.emailCheckoutNotificationSync(new OrderEntity(), "keithcharleschan@gmail.com");
        } catch (StaffNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {
        try {
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Manager", AccessRightEnum.MANAGER, "manager", "password"));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Cashier One", AccessRightEnum.CASHIER, "cashier1", "password"));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Cashier Two", AccessRightEnum.CASHIER, "cashier2", "password"));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Tailor One", AccessRightEnum.TAILOR, "tailor1", "password"));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Tailor Two", AccessRightEnum.TAILOR, "tailor2", "password"));
        } catch (StaffUsernameExistException | UnknownPersistenceException | InputDataValidationException ex) {
            ex.printStackTrace();
        }
    }

}
