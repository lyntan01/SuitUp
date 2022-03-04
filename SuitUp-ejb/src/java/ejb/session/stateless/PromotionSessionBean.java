/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AbsolutePromotionEntity;
import entity.PercentagePromotionEntity;
import entity.PromotionEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.PromotionCodeExistException;
import util.exception.PromotionCodeExpiredException;
import util.exception.PromotionMinimumAmountNotHitException;
import util.exception.PromotionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author lyntan
 */
@Stateless
public class PromotionSessionBean implements PromotionSessionBeanLocal {

    @PersistenceContext(unitName = "SuitUp-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PromotionSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewPromotion(PromotionEntity newPromotionEntity) throws PromotionCodeExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<PromotionEntity>> constraintViolations = validator.validate(newPromotionEntity);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newPromotionEntity);
                em.flush();

                return newPromotionEntity.getPromotionId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new PromotionCodeExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<PromotionEntity> retrieveAllPromotions() {
        Query query = em.createQuery("SELECT p FROM PromotionEntity p");

        return query.getResultList();
    }

    @Override
    public List<PercentagePromotionEntity> retrieveAllPercentagePromotions() {
        Query query = em.createQuery("SELECT p FROM PercentagePromotionEntity p");

        return query.getResultList();
    }

    @Override
    public List<AbsolutePromotionEntity> retrieveAllAbsolutePromotions() {
        Query query = em.createQuery("SELECT p FROM AbsolutePromotionEntity p");

        return query.getResultList();
    }

    @Override
    public PromotionEntity retrievePromotionByPromotionId(Long promotionId) throws PromotionNotFoundException {
        PromotionEntity promotionEntity = em.find(PromotionEntity.class, promotionId);

        if (promotionEntity != null) {
            return promotionEntity;
        } else {
            throw new PromotionNotFoundException("Promotion ID " + promotionId + " does not exist!");
        }
    }

    @Override
    public PromotionEntity retrievePromotionByPromotionCode(String promoCode) throws PromotionNotFoundException {
        PromotionEntity promotionEntity = (PromotionEntity) em.createQuery("SELECT p FROM PromotionEntity p WHERE p.promotionCode = :inCode")
                .setParameter("inCode", promoCode)
                .getSingleResult();

        if (promotionEntity != null) {
            return promotionEntity;
        } else {
            throw new PromotionNotFoundException("Promotion Code " + promoCode + " does not exist!");
        }
    }

    @Override
    public void updatePromotion(PromotionEntity promotionEntity) throws PromotionNotFoundException, UpdateEntityException, InputDataValidationException {
        if (promotionEntity != null && promotionEntity.getPromotionId() != null) {
            Set<ConstraintViolation<PromotionEntity>> constraintViolations = validator.validate(promotionEntity);

            if (constraintViolations.isEmpty()) {
                PromotionEntity promotionEntityToUpdate = retrievePromotionByPromotionId(promotionEntity.getPromotionId());

                if (promotionEntityToUpdate.getPromotionId().equals(promotionEntity.getPromotionId())) {
                    promotionEntityToUpdate.setMaxNumOfUsages(promotionEntity.getMaxNumOfUsages());
                    promotionEntityToUpdate.setMinimumSpending(promotionEntity.getMinimumSpending());
                    promotionEntityToUpdate.setExpiryDate(promotionEntity.getExpiryDate());
                    // cannot update promo code

                    if (promotionEntity instanceof PercentagePromotionEntity) {
                        ((PercentagePromotionEntity) promotionEntityToUpdate).setPercentageDiscount(((PercentagePromotionEntity) promotionEntity).getPercentageDiscount());
                    } else if (promotionEntity instanceof AbsolutePromotionEntity) {
                        ((AbsolutePromotionEntity) promotionEntityToUpdate).setAbsoluteDiscount(((AbsolutePromotionEntity) promotionEntity).getAbsoluteDiscount());
                    }

                } else {
                    throw new UpdateEntityException("Promotion ID of promotion record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new PromotionNotFoundException("Promotion ID not provided for promotion to be updated");
        }
    }

    @Override
    // To disable a current promotion (e.g. end it early), update its expiry date
    public void deletePromotion(Long promotionId) throws PromotionNotFoundException, DeleteEntityException {
        PromotionEntity promotionEntityToRemove = retrievePromotionByPromotionId(promotionId);

        if (promotionEntityToRemove.getOrders().isEmpty()) {
            // Disassociation
            promotionEntityToRemove.getOrders().clear();
            em.remove(promotionEntityToRemove);
        } else {
            // Prevent deleting promotions with existing order(s)
            throw new DeleteEntityException("Promotion ID " + promotionId + " is associated with existing order(s) and cannot be deleted!");
        }

    }

    @Override
    // Returns true if expiry date has not passed, false otherwise
    public Boolean isPromotionCodeValid(String promoCode) throws PromotionNotFoundException {

        PromotionEntity promotion = retrievePromotionByPromotionCode(promoCode);
        Date now = new Date();
        return now.before(promotion.getExpiryDate());

    }

    @Override
    public BigDecimal getDiscountedAmount(String promotionCode, BigDecimal subtotal) throws PromotionNotFoundException, PromotionCodeExpiredException, PromotionMinimumAmountNotHitException {

        PromotionEntity promotion = retrievePromotionByPromotionCode(promotionCode);
        BigDecimal discountedTotal = BigDecimal.ZERO;
        
        // Expiry date passed
        if (!isPromotionCodeValid(promotionCode)) {
            throw new PromotionCodeExpiredException("Promotion code is no longer valid.");
        }
        
        // Minimum amount not hit
        BigDecimal minAmount = promotion.getMinimumSpending();
        if (subtotal.compareTo(minAmount) < 0) {
            throw new PromotionMinimumAmountNotHitException("Minimum amount of " + minAmount + " not hit! Discount cannot be applied.");
        }

        if (promotion instanceof PercentagePromotionEntity) {
            // 15% discount = subtotal * (1-0.15)
            PercentagePromotionEntity percentagePromotion = (PercentagePromotionEntity) promotion;
            BigDecimal multiplicand = BigDecimal.ONE.subtract(BigDecimal.valueOf(percentagePromotion.getPercentageDiscount() / 100.0));
            discountedTotal = subtotal.multiply(multiplicand);
            
        } else if (promotion instanceof AbsolutePromotionEntity) {
            
            AbsolutePromotionEntity absolutePromotion = (AbsolutePromotionEntity) promotion;
            discountedTotal = subtotal.subtract(absolutePromotion.getAbsoluteDiscount());
            
        }

        return discountedTotal;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PromotionEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
