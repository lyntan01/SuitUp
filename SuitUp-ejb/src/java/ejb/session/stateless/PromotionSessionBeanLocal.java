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
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface PromotionSessionBeanLocal {

    public Long createNewPromotion(PromotionEntity newPromotionEntity) throws PromotionCodeExistException, UnknownPersistenceException, InputDataValidationException;

    public List<PromotionEntity> retrieveAllPromotions();

    public List<PercentagePromotionEntity> retrieveAllPercentagePromotions();

    public List<AbsolutePromotionEntity> retrieveAllAbsolutePromotions();

    public PromotionEntity retrievePromotionByPromotionId(Long promotionId) throws PromotionNotFoundException;

    public PromotionEntity retrievePromotionByPromotionCode(String promoCode) throws PromotionNotFoundException;

    public void updatePromotion(PromotionEntity promotionEntity) throws PromotionNotFoundException, UpdateEntityException, InputDataValidationException;

    public void deletePromotion(Long promotionId) throws PromotionNotFoundException, DeleteEntityException;

    public Boolean isPromotionCodeValid(String promoCode) throws PromotionNotFoundException;

    public BigDecimal getDiscountedAmount(String promotionCode, BigDecimal subtotal) throws PromotionNotFoundException, PromotionCodeExpiredException, PromotionMinimumAmountNotHitException;

}
