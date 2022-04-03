/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AddressSessionBeanLocal;
import ejb.session.stateless.AppointmentSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.CreditCardSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmailSessionBeanLocal;
import ejb.session.stateless.OrderSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import ejb.session.stateless.StandardProductSessionBeanLocal;
import ejb.session.stateless.StoreSessionBeanLocal;
import ejb.session.stateless.TagSessionBeanLocal;
import entity.AddressEntity;
import entity.AppointmentEntity;
import entity.CategoryEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.ProductEntity;
import entity.StaffEntity;
import entity.StandardProductEntity;
import entity.StoreEntity;
import entity.TagEntity;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.AppointmentTypeEnum;
import util.enumeration.CollectionMethodEnum;
import util.enumeration.OrderStatusEnum;
import util.exception.AddressNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewAppointmentException;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewOrderException;
import util.exception.CreateNewTagException;
import util.exception.CreateStandardProductException;
import util.exception.CreditCardNumberExistException;
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.StandardProductNotFoundException;
import util.exception.StoreNotFoundException;
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
    private TagSessionBeanLocal tagSessionBeanLocal;

    @EJB
    private EmailSessionBeanLocal emailSessionBeanLocal;

    @EJB
    private StaffSessionBeanLocal staffSessionBeanLocal;

    @EJB
    private OrderSessionBeanLocal orderSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private AddressSessionBeanLocal addressSessionBeanLocal;

    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;

    @EJB
    private StandardProductSessionBeanLocal standardProductSessionBeanLocal;

    @EJB
    private AppointmentSessionBeanLocal appointmentSessionBeanLocal;

    @EJB
    private StoreSessionBeanLocal storeSessionBeanLocal;

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

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
            
            Long storeId = storeSessionBeanLocal.createNewStore(new StoreEntity("SuitUp", "Best suit store", "09:00", "22:00", "62313264"));
            addressSessionBeanLocal.createNewStoreAddress(new AddressEntity("SuitUp Store Address", "62313264", "10 Orchard Road", "Far East Plaza", "228213"), storeId);

            StoreEntity storeEntity = storeSessionBeanLocal.retrieveStoreByStoreId(storeId);
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Manager", AccessRightEnum.MANAGER, "manager", "password"));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Cashier One", AccessRightEnum.CASHIER, "cashier1", "password", storeEntity));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Cashier Two", AccessRightEnum.CASHIER, "cashier2", "password", storeEntity));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Tailor One", AccessRightEnum.TAILOR, "tailor1", "password", storeEntity));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Tailor Two", AccessRightEnum.TAILOR, "tailor2", "password", storeEntity));

            customerSessionBeanLocal.createNewCustomer(new CustomerEntity("Bobby", "Tan", "bobby@gmail.com", "password", "9999999")); //Customer - 1L
            addressSessionBeanLocal.createNewCustomerAddress(new AddressEntity("Bobby", "9999999", "5 Avenue", "Beepbop", "420420"), 1L); //Tagged to above customer

            Calendar calendar = new Calendar.Builder()
                    .setDate(2022, Calendar.JUNE, 1)
                    .setTimeOfDay(0, 0, 0)
                    .build(); 
            creditCardSessionBeanLocal.createNewCreditCard(new CreditCardEntity("Bobby", "1111222233334444", "910", calendar.getTime()), 1L);
            creditCardSessionBeanLocal.createNewCreditCard(new CreditCardEntity("Bobby", "2222333344445555", "320", calendar.getTime()), 1L);

            //--------------[[Order Management Testing]]----------//
            initialiseCategories();
            initialiseStandardProducts();

            ProductEntity productOne = standardProductSessionBeanLocal.retrieveStandardProductByStandardProductId(1L);
            ProductEntity productTwo = standardProductSessionBeanLocal.retrieveStandardProductByStandardProductId(2L);
            ProductEntity productThree = standardProductSessionBeanLocal.retrieveStandardProductByStandardProductId(3L);

            List<OrderLineItemEntity> items = new ArrayList<>();
            items.add(new OrderLineItemEntity(2, new BigDecimal("20.00"), productOne));
            items.add(new OrderLineItemEntity(2, new BigDecimal("40.00"), productTwo));
            items.add(new OrderLineItemEntity(2, new BigDecimal("60.00"), productThree));

            //SerialNumber, TotalLineItems, TotalQuantity, TotalAmount, OrderDateTime, expressOrder, orderStatus, collectionMethod, List of OrderLineItems
            OrderEntity testOrder = new OrderEntity("ABCD", 3, 6, new BigDecimal("120.00"), new Date(), false, OrderStatusEnum.PROCESSING, CollectionMethodEnum.DELIVERY, items);

            //Uses Bobby Tan and Bobby's Tan Address
            //orderSessionBeanLocal.createNewOrder(customerId, addressId, new OrderEntity)
            orderSessionBeanLocal.createNewOrder(1L, 1L, testOrder);
            //--------------[[END of Order Management Testing]]----------//

            initialiseAppointments();

        } catch (StaffUsernameExistException | UnknownPersistenceException | InputDataValidationException | CustomerEmailExistException
                | CustomerNotFoundException | CreateNewOrderException | AddressNotFoundException | StandardProductNotFoundException | CreditCardNumberExistException 
                | StoreNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void initialiseCategories() {

        try {
            categorySessionBeanLocal.createNewCategory(new CategoryEntity("Category 1", "Cat 1 Desc", new ArrayList<>())); // 1L
            categorySessionBeanLocal.createNewCategory(new CategoryEntity("Category 2", "Cat 2 Desc", new ArrayList<>())); // 2L
            categorySessionBeanLocal.createNewCategory(new CategoryEntity("Category 3", "Cat 3 Desc", new ArrayList<>())); // 3L
        } catch (InputDataValidationException | UnknownPersistenceException | CreateNewCategoryException ex) {
            ex.printStackTrace();
        }

    }

    private void initialiseTags() {

        try {
            tagSessionBeanLocal.createNewTag(new TagEntity("Tag 1", new ArrayList<>())); // 1L
            tagSessionBeanLocal.createNewTag(new TagEntity("Tag 2", new ArrayList<>())); // 1L
            tagSessionBeanLocal.createNewTag(new TagEntity("Tag 3", new ArrayList<>())); // 1L
        } catch (InputDataValidationException | UnknownPersistenceException | CreateNewTagException ex) {
            ex.printStackTrace();
        }

    }

    private void initialiseStandardProducts() {

        try {
            CategoryEntity catOne = categorySessionBeanLocal.retrieveCategoryByCategoryId(1L);
            CategoryEntity catTwo = categorySessionBeanLocal.retrieveCategoryByCategoryId(2L);
            CategoryEntity catThree = categorySessionBeanLocal.retrieveCategoryByCategoryId(3L);

            //1L
            standardProductSessionBeanLocal.createNewStandardProduct(new StandardProductEntity("Product A1", "Product A1", "Image1", "A001", new BigDecimal("10.00"), 100, 10, catOne, new ArrayList<>()), 1L, new ArrayList<>());
            //2L
            standardProductSessionBeanLocal.createNewStandardProduct(new StandardProductEntity("Product A2", "Product A2", "Image2", "A002", new BigDecimal("20.00"), 100, 10, catTwo, new ArrayList<>()), 2L, new ArrayList<>());
            //3L
            standardProductSessionBeanLocal.createNewStandardProduct(new StandardProductEntity("Product A3", "Product A3", "Image3", "A003", new BigDecimal("30.00"), 100, 10, catThree, new ArrayList<>()), 3L, new ArrayList<>());

            //standardProductSessionBeanLocal.createNewStandardProduct(newStandardProductEntity, Long.MAX_VALUE, tagsId)
            //new StandardProductEntity(name, description, image, skuCode, BigDecimal.ONE, Integer.BYTES, Integer.MIN_VALUE, category, tags)
        } catch (CategoryNotFoundException | CreateStandardProductException | InputDataValidationException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }

    private void initialiseAppointments() {

        try {
            //1L
            AppointmentEntity apptOne = new AppointmentEntity(new Date(), AppointmentTypeEnum.ALTERATION, false);
            appointmentSessionBeanLocal.createNewAppointment(apptOne, 1L, 1L);
            //2L
            AppointmentEntity apptTwo = new AppointmentEntity(new Date(), AppointmentTypeEnum.CONSULTATION, true);
            appointmentSessionBeanLocal.createNewAppointment(apptTwo, 1L, 1L);
            //3L
            AppointmentEntity apptThree = new AppointmentEntity(new Date(), AppointmentTypeEnum.MEASUREMENT, true);
            appointmentSessionBeanLocal.createNewAppointment(apptThree, 1L, 1L);

        } catch (CreateNewAppointmentException | StoreNotFoundException | CustomerNotFoundException | InputDataValidationException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }

}
