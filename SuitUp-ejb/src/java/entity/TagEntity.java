/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author xianhui
 */
@Entity
public class TagEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String name;
    
    @ManyToMany
    @JoinColumn(nullable=true)
    private List<StandardProductEntity> standardProducts;

    public TagEntity() {
        standardProducts = new ArrayList<StandardProductEntity>();
    }

    public TagEntity(String name, List<StandardProductEntity> standardProducts) {
        this.name = name;
        this.standardProducts = standardProducts;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StandardProductEntity> getStandardProducts() {
        return standardProducts;
    }

    public void setStandardProducts(List<StandardProductEntity> standardProducts) {
        this.standardProducts = standardProducts;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tagId != null ? tagId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the tagId fields are not set
        if (!(object instanceof TagEntity)) {
            return false;
        }
        TagEntity other = (TagEntity) object;
        if ((this.tagId == null && other.tagId != null) || (this.tagId != null && !this.tagId.equals(other.tagId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TagEntity[ id=" + tagId + " ]";
    }
    
}
