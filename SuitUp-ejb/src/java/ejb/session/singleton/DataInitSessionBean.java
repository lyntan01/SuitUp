/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AddressSessionBeanLocal;
import ejb.session.stateless.AppointmentSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.ColourSessionBeanLocal;
import ejb.session.stateless.CreditCardSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.CustomizedJacketSessionBeanLocal;
import ejb.session.stateless.CustomizedPantsSessionBeanLocal;
import ejb.session.stateless.EmailSessionBeanLocal;
import ejb.session.stateless.FabricSessionBeanLocal;
import ejb.session.stateless.JacketMeasurementSessionBeanLocal;
import ejb.session.stateless.JacketStyleSessionBeanLocal;
import ejb.session.stateless.OrderSessionBeanLocal;
import ejb.session.stateless.PromotionSessionBeanLocal;
import ejb.session.stateless.PantsCuttingSessionBeanLocal;
import ejb.session.stateless.PantsMeasurementSessionBeanLocal;
import ejb.session.stateless.PocketStyleSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import ejb.session.stateless.StandardProductSessionBeanLocal;
import ejb.session.stateless.StoreSessionBeanLocal;
import ejb.session.stateless.SupportTicketSessionBeanLocal;
import ejb.session.stateless.TagSessionBeanLocal;
import ejb.session.stateless.TransactionSessionBeanLocal;
import entity.AbsolutePromotionEntity;
import entity.AddressEntity;
import entity.AppointmentEntity;
import entity.CategoryEntity;
import entity.ColourEntity;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.CustomizedJacketEntity;
import entity.CustomizedPantsEntity;
import entity.FabricEntity;
import entity.JacketMeasurementEntity;
import entity.JacketStyleEntity;
import entity.OrderEntity;
import entity.OrderLineItemEntity;
import entity.PercentagePromotionEntity;
import entity.PantsCuttingEntity;
import entity.PantsMeasurementEntity;
import entity.PocketStyleEntity;
import entity.ProductEntity;
import entity.StaffEntity;
import entity.StandardProductEntity;
import entity.StoreEntity;
import entity.SupportTicketEntity;
import entity.TagEntity;
import entity.TransactionEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import util.exception.AppointmentNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.ColourIdExistException;
import util.exception.ColourNotFoundException;
import util.exception.CreateNewAppointmentException;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewOrderException;
import util.exception.CreateNewTagException;
import util.exception.CreateNewTransactionException;
import util.exception.CreateStandardProductException;
import util.exception.CreditCardNumberExistException;
import util.exception.CustomerEmailExistException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomizationIdExistException;
import util.exception.CustomizationNotFoundException;
import util.exception.CustomizedProductIdExistsException;
import util.exception.CustomizedProductNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.PromotionCodeExistException;
import util.exception.JacketMeasurementNotFoundException;
import util.exception.OrderNotFoundException;
import util.exception.PantsMeasurementNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.StandardProductNotFoundException;
import util.exception.StoreNotFoundException;
import util.exception.SupportTicketIdExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UnsuccessfulTicketException;

/**
 *
 * @author lyntan
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private PromotionSessionBeanLocal promotionSessionBeanLocal;
    
    @EJB
    private CustomizedPantsSessionBeanLocal customizedPantsSessionBean;

    @EJB
    private CustomizedJacketSessionBeanLocal customizedJacketSessionBean;

    @EJB
    private PantsMeasurementSessionBeanLocal pantsMeasurementSessionBean;

    @EJB
    private JacketMeasurementSessionBeanLocal jacketMeasurementSessionBean;

    @EJB
    private JacketStyleSessionBeanLocal jacketStyleSessionBean;

    @EJB
    private PocketStyleSessionBeanLocal pocketStyleSessionBean;

    @EJB
    private PantsCuttingSessionBeanLocal pantsCuttingSessionBean;

    @EJB
    private FabricSessionBeanLocal fabricSessionBean;

    @EJB
    private ColourSessionBeanLocal colourSessionBean;

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
    
    @EJB
    private SupportTicketSessionBeanLocal supportTicketSessionBeanLocal;
    
    

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
            
//          <------------------------------STORE INIT ------------------------->
            Long storeId = storeSessionBeanLocal.createNewStore(new StoreEntity("SuitUp", "Best Suit Store", "09:00", "22:00", "62313264"));
            addressSessionBeanLocal.createNewStoreAddress(new AddressEntity("SuitUp Store Address", "62313264", "10 Orchard Road", "Far East Plaza", "228213"), storeId);

//          <------------------------------STAFF INIT ------------------------->
            StoreEntity storeEntity = storeSessionBeanLocal.retrieveStoreByStoreId(storeId);
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Manager", AccessRightEnum.MANAGER, "manager", "password"));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Cashier One", AccessRightEnum.CASHIER, "cashier1", "password", storeEntity));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Cashier Two", AccessRightEnum.CASHIER, "cashier2", "password", storeEntity));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Tailor One", AccessRightEnum.TAILOR, "tailor1", "password", storeEntity));
            staffSessionBeanLocal.createNewStaff(new StaffEntity("Default", "Tailor Two", AccessRightEnum.TAILOR, "tailor2", "password", storeEntity));

//          <------------------------------CUSTOMER 1 INIT ------------------------->
            customerSessionBeanLocal.createNewCustomer(new CustomerEntity("Bobby", "Tan", "bobby@gmail.com", "password", "99999999")); //Customer - 1L
            addressSessionBeanLocal.createNewCustomerAddress(new AddressEntity("Bobby", "9999999", "5 Avenue", "Beepbop", "420420"), 1L); //Tagged to above customer

            Calendar calendar = new Calendar.Builder()
                    .setDate(2022, Calendar.JUNE, 1)
                    .setTimeOfDay(0, 0, 0)
                    .build(); 
            creditCardSessionBeanLocal.createNewCreditCard(new CreditCardEntity("Bobby", "1111222233334444", "910", calendar.getTime()), 1L);
            creditCardSessionBeanLocal.createNewCreditCard(new CreditCardEntity("Bobby Home", "2222333344445555", "320", calendar.getTime()), 1L);
            
//          <------------------------------CUSTOMER 2 INIT ------------------------->
//            customerSessionBeanLocal.createNewCustomer(new CustomerEntity("Charles", "Chan", "charlesChan@gmail.com", "password", "99999999")); //Customer - 1L
//            addressSessionBeanLocal.createNewCustomerAddress(new AddressEntity("Charles Home", "91425342", "Thomson Ave 3", "Private House 3", "210309"), 1L); //Tagged to above customer
//
//            Calendar anotherCalendar = new Calendar.Builder()
//                    .setDate(2022, Calendar.JUNE, 1)
//                    .setTimeOfDay(0, 0, 0)
//                    .build();
//            creditCardSessionBeanLocal.createNewCreditCard(new CreditCardEntity("Bobby", "5555223333221111", "910", anotherCalendar.getTime()), 1L);
//            creditCardSessionBeanLocal.createNewCreditCard(new CreditCardEntity("Bobby", "2222113344223356", "320", anotherCalendar.getTime()), 1L);

//          <------------------------------STANDARD AND CUSTOMIZED PRODUCTS NEED FIX IMAGE------------------------->
            initialiseCategories();
            initialiseTags();
            initialiseStandardProducts();
            initialiseCustomisationProducts();

            //<--------------Order 1, Containing Standard Products---------->//
            ProductEntity productOne = standardProductSessionBeanLocal.retrieveStandardProductByStandardProductId(1L);
            ProductEntity productTwo = standardProductSessionBeanLocal.retrieveStandardProductByStandardProductId(2L);
            ProductEntity productThree = standardProductSessionBeanLocal.retrieveStandardProductByStandardProductId(3L);

            List<OrderLineItemEntity> items = new ArrayList<>();
            items.add(new OrderLineItemEntity(2, new BigDecimal("20.00"), productOne));
            items.add(new OrderLineItemEntity(2, new BigDecimal("40.00"), productTwo));
            items.add(new OrderLineItemEntity(2, new BigDecimal("60.00"), productThree));

            //SerialNumber, TotalLineItems, TotalQuantity, TotalAmount, OrderDateTime, expressOrder, orderStatus, collectionMethod, List of OrderLineItems
            OrderEntity testOrder = new OrderEntity("ABCDEF", 3, 6, new BigDecimal("120.00"), new Date(), false, OrderStatusEnum.PROCESSING, CollectionMethodEnum.DELIVERY, items);

            //Uses Bobby Tan and Bobby's Tan Address
            //orderSessionBeanLocal.createNewOrder(customerId, addressId, new OrderEntity)
            testOrder = orderSessionBeanLocal.createNewOrder(1L, 2L, testOrder);
            transactionSessionBeanLocal.createNewTransaction(new TransactionEntity(testOrder.getTotalAmount(), new Date(), null, testOrder), null, 1L);
            
            //<--------------Order 2, Containing Customised Jacket---------->//
            initialiseCustomisedOrder();

            //<--------------Appointment, support tickets and promotions---------->//
            initialiseAppointments();
            initialiseSupportTickets();
            initialisePromotions();


        } catch (StaffUsernameExistException | UnknownPersistenceException | InputDataValidationException | CustomerEmailExistException
                | CustomerNotFoundException | CreateNewOrderException | AddressNotFoundException | StandardProductNotFoundException | CreditCardNumberExistException 
                | StoreNotFoundException | AppointmentNotFoundException | OrderNotFoundException | CreateNewTransactionException ex) {
            ex.printStackTrace();
        }
    }

    private void initialiseCategories() {

        try {
            categorySessionBeanLocal.createNewCategory(new CategoryEntity("Ties", "Different Ties that complete your suit!", new ArrayList<>())); // 1L
            categorySessionBeanLocal.createNewCategory(new CategoryEntity("Shirts", "Complete your looks with these shirts", new ArrayList<>())); // 2L
            categorySessionBeanLocal.createNewCategory(new CategoryEntity("Cufflinks", "Add Cufflings to sleeves", new ArrayList<>())); // 3L
            categorySessionBeanLocal.createNewCategory(new CategoryEntity("Pocket Square", "Pocket Squares for Suit Pockets", new ArrayList<>())); // 4L
            categorySessionBeanLocal.createNewCategory(new CategoryEntity("Brooches", "For Females", new ArrayList<>())); // 5L
            categorySessionBeanLocal.createNewCategory(new CategoryEntity("Belt", "For Suit Pants", new ArrayList<>())); // 6L
        } catch (InputDataValidationException | UnknownPersistenceException | CreateNewCategoryException ex) {
            ex.printStackTrace();
        }

    }

    private void initialiseTags() {

        try {
            tagSessionBeanLocal.createNewTag(new TagEntity("New", new ArrayList<>())); // 1L
            tagSessionBeanLocal.createNewTag(new TagEntity("Male", new ArrayList<>())); // 2L
            tagSessionBeanLocal.createNewTag(new TagEntity("Female", new ArrayList<>())); // 3L
            tagSessionBeanLocal.createNewTag(new TagEntity("Popular", new ArrayList<>())); // 4L
            tagSessionBeanLocal.createNewTag(new TagEntity("Reccomended", new ArrayList<>())); // 5L
        } catch (InputDataValidationException | UnknownPersistenceException | CreateNewTagException ex) {
            ex.printStackTrace();
        }

    }

    private void initialiseStandardProducts() {

        try {
            CategoryEntity catOne = categorySessionBeanLocal.retrieveCategoryByCategoryId(1L);
            CategoryEntity catTwo = categorySessionBeanLocal.retrieveCategoryByCategoryId(2L);
            CategoryEntity catThree = categorySessionBeanLocal.retrieveCategoryByCategoryId(3L);
            CategoryEntity catFour = categorySessionBeanLocal.retrieveCategoryByCategoryId(4L);
            CategoryEntity catFive = categorySessionBeanLocal.retrieveCategoryByCategoryId(5L);
            CategoryEntity catSix = categorySessionBeanLocal.retrieveCategoryByCategoryId(6L);

            //1L
            standardProductSessionBeanLocal.createNewStandardProduct(new StandardProductEntity("Geometric Print Tie", "Geometric Print Tie", "GeometricPrintTie.png", "SKU001", new BigDecimal("10.00"), 1000, 50, catOne, new ArrayList<>()), 1L, new ArrayList<>(Arrays.asList(1L, 2L)));
            //2L
            standardProductSessionBeanLocal.createNewStandardProduct(new StandardProductEntity("White Collar Tee", "White Collar Long Sleeve", "WhiteCollarLongSleeve.png", "SKU002", new BigDecimal("20.00"), 1000, 50, catTwo, new ArrayList<>()), 2L, new ArrayList<>(Arrays.asList(2L, 3L)));
            //3L
            standardProductSessionBeanLocal.createNewStandardProduct(new StandardProductEntity("StarWalker Cufflinks", "StarWalker Cufflinks", "Cufflinks.png", "SKU003", new BigDecimal("5.00"), 1000, 50, catThree, new ArrayList<>()), 3L, new ArrayList<>(Arrays.asList(4L, 5L)));
            //4L
            standardProductSessionBeanLocal.createNewStandardProduct(new StandardProductEntity("Silver Pocket Square", "Silver Pocket Square", "SilverPocketSquare.png", "SKU004", new BigDecimal("10.50"), 1000, 50, catFour, new ArrayList<>()), 4L, new ArrayList<>(Arrays.asList(2L)));
            //5L
            standardProductSessionBeanLocal.createNewStandardProduct(new StandardProductEntity("Leaf Moonstone Brooch", "Leaf Moonstone Brooch", "LeafBrooche.png", "SKU005", new BigDecimal("15.50"), 1000, 50, catFive, new ArrayList<>()), 5L, new ArrayList<>(Arrays.asList(3L)));
            //6L
            standardProductSessionBeanLocal.createNewStandardProduct(new StandardProductEntity("Black Leather Belt", "Black Leather Belt", "BlackLeatherBelt.png", "SKU006", new BigDecimal("50.00"), 1000, 50, catSix, new ArrayList<>()), 6L, new ArrayList<>(Arrays.asList(4L)));
            
            //standardProductSessionBeanLocal.createNewStandardProduct(newStandardProductEntity, Long.MAX_VALUE, tagsId)
            //new StandardProductEntity(name, description, image, skuCode, BigDecimal.ONE, Integer.BYTES, Integer.MIN_VALUE, category, tags)
        } catch (CategoryNotFoundException | CreateStandardProductException | InputDataValidationException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }
    
    private void initialiseCustomisationProducts() {

        try {

            //<-------------------POCKET STYLE------------------------>
            //1L
            pocketStyleSessionBean.createNewPocketStyle(new PocketStyleEntity("Straight Flap", new BigDecimal("10.80"), "Straight Flap Pocket Style", "StraightFlapPocketStyle.png")); //1L
            //2L
            pocketStyleSessionBean.createNewPocketStyle(new PocketStyleEntity("Straight No Flap", new BigDecimal("0.00"), "Straight No Flap Pocket Style", "StraightNoFlap.png")); //2L
            //3L
            jacketStyleSessionBean.createNewJacketStyle(new JacketStyleEntity("Single Breasted", new BigDecimal("0.00"), "Single Breasted Jacket Style", "SingleBreasted.png")); //3L
            //4L
            jacketStyleSessionBean.createNewJacketStyle(new JacketStyleEntity("Double Breasted", new BigDecimal("40.00"), "Double Breasted Jacket Style", "DoubleBreasted.png")); //4L
            //5L
            pantsCuttingSessionBean.createNewPantsCutting(new PantsCuttingEntity("Pleat Pants", new BigDecimal("30.50"), "Pleat Pants Style", "PleatPants.png")); //5L
            //6L
            pantsCuttingSessionBean.createNewPantsCutting(new PantsCuttingEntity("No Pleat Pants", new BigDecimal("0.00"), "No Pleat Pants Style", "NoPleatPants.png")); //6L
            //1L
            colourSessionBean.createNewColour(new ColourEntity("Glacier Grey", "#C6CBCC")); //1L
            //7L
            fabricSessionBean.createNewFabric(new FabricEntity("Grey Wool Fabric", new BigDecimal("0.00"), "Glacer Grey, Wool", "GlacierGreyWool.jpg"), 1L); //7L
            //2L
            colourSessionBean.createNewColour(new ColourEntity("Hurley", "#FFFFED")); //2L
            //8L
            fabricSessionBean.createNewFabric(new FabricEntity("Hurley Linen", new BigDecimal("0.00"), "Hurley, Linen", "Hurley Linen.jpg"), 1L); //7L
  
            //PocketStyleEntity(String name, BigDecimal additionalPrice, String description, String image)
            //new pocketStyleEntity(String name, BigDecimal additionalPrice, String description, String image)
        } catch (CustomizationIdExistException | ColourIdExistException | ColourNotFoundException | InputDataValidationException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }

    private void initialiseCustomisedOrder() {
        try {
            //CREATE JACKET MEASUREMENT FOR CUSTOMER ID1 
            JacketMeasurementEntity jacketMeasurement = new JacketMeasurementEntity(new BigDecimal("3.2"), new BigDecimal("10.0"), new BigDecimal("7.8"), new BigDecimal("42.1"), new BigDecimal("12.9"), new BigDecimal("38.4"), new BigDecimal("4.5"), new BigDecimal("5.6"), new BigDecimal("15.2"), new BigDecimal("4.2"), new BigDecimal("18.4"), new BigDecimal("6.0"), new BigDecimal("3.8"));
            jacketMeasurementSessionBean.createNewJacketMeasurement(jacketMeasurement, 1L);
            //createNewJacketMeasurement(JacketMeasurementEntity newJacketMeasurementEntity, Long customerId)
            //JacketMeasurementEntity(BigDecimal neck, BigDecimal frontLength, BigDecimal chestGrith, BigDecimal frontChestWidth, BigDecimal upperWaistGrith, BigDecimal hipGrith, BigDecimal armhole, BigDecimal shoulderWidth, BigDecimal sleeveLength, BigDecimal backwidth, BigDecimal bicepGrith, BigDecimal forearmGrith, BigDecimal wristGrith)
            
            //CREATE PANTS MEASUREMENT FOR CUSTOMER ID1 
            PantsMeasurementEntity pantsMeasurement = new PantsMeasurementEntity(new BigDecimal("3.4"), new BigDecimal("4.5"), new BigDecimal("5.6"), new BigDecimal("15.2"), new BigDecimal("4.2"), new BigDecimal("18.4"), new BigDecimal("6.0"), new BigDecimal("3.8"));
            pantsMeasurementSessionBean.createNewPantsMeasurement(pantsMeasurement, 1L);
            //createNewPantsMeasurement(PantsMeasurementEntity newPantsMeasurementEntity, Long customerId)
            //PantsMeasurementEntity(Long pantsMeasurementId, BigDecimal legsLength, BigDecimal lowerWaistGirth, BigDecimal hipGirth, BigDecimal crotch, BigDecimal thighGrith, BigDecimal kneeGrith, BigDecimal calfGrith, BigDecimal pantsOpeningWidth) {
            
            //CREATE CUSTOMISED JACKET (Bobby)
            CustomizedJacketEntity customisedJacket = new CustomizedJacketEntity("Formal Suit", "New Formal Suit", new BigDecimal("0.00"), "Male");
            //CustomizedJacketEntity(String name, String description, BigDecimal totalPrice, String gender) 
            customisedJacket.setJacketMeasurement(jacketMeasurement);
            customisedJacket.setInnerFabric(fabricSessionBean.retrieveFabricById(7L));
            customisedJacket.setJacketStyle(jacketStyleSessionBean.retrieveJacketStyleById(3L));
            customisedJacket.setOuterFabric(fabricSessionBean.retrieveFabricById(8L));
            customisedJacket.setPocketStyle(pocketStyleSessionBean.retrievePocketStyleById(1L));
            Long newId = customizedJacketSessionBean.createNewCustomizedJacket(customisedJacket, 1L, 3L, 7L, 8L, 1L);
            System.out.println(customizedJacketSessionBean.retrieveCustomizedJacketById(newId));
            //createNewCustomizedJacket(CustomizedJacketEntity newCustomizedJacket, Long pocketStyleId, Long jacketStyleId, Long innerFabricId, Long outerFabricId, Long jacketMeasurementId)
            //CREATE CUSTOMISED PANTS (Bobby)
            CustomizedPantsEntity customisedPants = new CustomizedPantsEntity("Formal Pants", "New Formal Pants", new BigDecimal("0.00"), "Male");
            customisedPants.setPantsCutting(pantsCuttingSessionBean.retrievePantsCuttingById(5L));
            customisedPants.setPantsMeasurement(pantsMeasurement);
            customisedPants.setFabric(fabricSessionBean.retrieveFabricById(7L));
            customizedPantsSessionBean.createNewCustomizedPants(customisedPants, 7L, 5L, 1L);
            
            List<OrderLineItemEntity> items = new ArrayList<>();
            items.add(new OrderLineItemEntity(1, customisedJacket.getTotalPrice(), customisedJacket));
            items.add(new OrderLineItemEntity(1, customisedPants.getTotalPrice(), customisedPants));
            BigDecimal totalAmount = customisedJacket.getTotalPrice().add(customisedPants.getTotalPrice());
            //OrderLineItemEntity(Integer quantity, BigDecimal subTotal, ProductEntity product)
            
            //SerialNumber, TotalLineItems, TotalQuantity, TotalAmount, OrderDateTime, expressOrder, orderStatus, collectionMethod, List of OrderLineItems
            OrderEntity testOrder = new OrderEntity("KQJS", 2, 2, totalAmount, new Date(), false, OrderStatusEnum.PROCESSING, CollectionMethodEnum.DELIVERY, items);
            
            //Uses Bobby Tan and Bobby's Tan Address
            //orderSessionBeanLocal.createNewOrder(customerId, addressId, new OrderEntity)
            OrderEntity order = orderSessionBeanLocal.createNewOrder(1L, 1L, testOrder);
            System.out.println(order.getOrderId());
        } catch (UnknownPersistenceException | CustomerNotFoundException | CustomizedProductNotFoundException | PantsMeasurementNotFoundException | CustomizedProductIdExistsException | JacketMeasurementNotFoundException | CustomizationNotFoundException | InputDataValidationException | AddressNotFoundException | CreateNewOrderException ex) {
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
    
    private void initialiseSupportTickets() {

        try {
            supportTicketSessionBeanLocal.createNewSupportTicket(new SupportTicketEntity("Suit came in the wrong colour", "Hi, I ordered a suit in navy but it came in grey. It also doesn't seem to fit me well. By any chance, was the wrong suit delivered to me?", new Date()), 1L);
            supportTicketSessionBeanLocal.createNewSupportTicket(new SupportTicketEntity("When will my suit come?", "Hello I ordered my suit 2 days ago but it still has not arrived. When is it coming?", new Date(), "Hello Mr Bobby, thank you for reaching out to us. Since you opted for normal delivery, the expected delivery time is 3-7 days. Thank you for your understanding and do reach out to us should you require any assistance. Cheers, The SuitUp Team."), 1L);
        
            customerSessionBeanLocal.createNewCustomer(new CustomerEntity("Suzy", "Bae", "suzy@hotmail.com", "password", "88888888")); //Customer - 3L
            supportTicketSessionBeanLocal.createNewSupportTicket(new SupportTicketEntity("Seeking colour recommendation", "Which colour will suit my skin tone more? I can't decide between navy and grey.", new Date()), 2L);
            supportTicketSessionBeanLocal.createNewSupportTicket(new SupportTicketEntity("Female Suits", "Do you also customise female suits?", new Date(), "Hello Ms Suzy, we do offer customisation services for female suits, with a wide range of fabrics and customisations to choose from. Do let us know if there is any way we can help should you require further assistance. Cheers, The SuitUp Team."), 2L);
        } catch (CustomerEmailExistException | SupportTicketIdExistException | UnsuccessfulTicketException | InputDataValidationException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }

    }
    
    private void initialisePromotions() 
    {
        try {
            
            // Promotion 1: $10 off min spend $50, first 50 customers
            Date expiryDate = new GregorianCalendar(2022, Calendar.MARCH, 1).getTime();
            promotionSessionBeanLocal.createNewPromotion(new AbsolutePromotionEntity(BigDecimal.valueOf(10), "CNY2022", 50, BigDecimal.valueOf(50), expiryDate));
            
            // Promotion 2: 20% off no min spend, for the whole of April
            expiryDate = new GregorianCalendar(2022, Calendar.MAY, 1).getTime();
            promotionSessionBeanLocal.createNewPromotion(new PercentagePromotionEntity(20, "APR20OFF", 10000000, BigDecimal.ZERO, expiryDate));
            
            // Promotion 3: $5 off min spend $100, for the whole 2022
            expiryDate = new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime();
            promotionSessionBeanLocal.createNewPromotion(new AbsolutePromotionEntity(BigDecimal.valueOf(5), "MEGAN5OFF100", 10000000, BigDecimal.valueOf(100), expiryDate));
            
        } catch (PromotionCodeExistException | UnknownPersistenceException | InputDataValidationException ex) {
            ex.printStackTrace();
        }
    }

}
