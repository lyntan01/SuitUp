/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Local
public interface CategorySessionBeanLocal {

    public List<CategoryEntity> retrieveAllCategories();

    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;

    public CategoryEntity createNewCategory(CategoryEntity newCategoryEntity) throws UnknownPersistenceException, InputDataValidationException;

    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteEntityException;

    public void updateCategory(CategoryEntity categoryEntity) throws InputDataValidationException, CategoryNotFoundException, UpdateEntityException;
    
}
