/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TagEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteEntityException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEntityException;

/**
 *
 * @author xianhui
 */
@Local
public interface TagSessionBeanLocal {

    public TagEntity retrieveTagByTagId(Long tagId) throws TagNotFoundException;

    public Long createNewTag(TagEntity newTagEntity) throws UnknownPersistenceException, InputDataValidationException;

    public List<TagEntity> retrieveAllTags();

    public void updateTag(TagEntity tagEntity) throws InputDataValidationException, TagNotFoundException, UpdateEntityException;

    public void deleteTag(Long tagId) throws TagNotFoundException, DeleteEntityException;
    
}
