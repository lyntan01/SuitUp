/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 *
 * @author xianhui
 */
@Entity
public class StandardProductEntity extends ProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String skuCode;
    @Column(nullable = false, precision=11, scale=2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer=9, fraction=2)
    private BigDecimal unitPrice;
    @Column(nullable = false)
    @NotNull
    @Positive
    private Integer quantityInStock;
    @Column(nullable = false)
    @NotNull
    @Positive
    private Integer reorderQuantity;
    
    @ManyToOne(optional=false)
    @JoinColumn(nullable = false)
    private CategoryEntity category;
    
    @ManyToMany
    @JoinColumn(nullable=true)
    private List<TagEntity> tags;


    public StandardProductEntity() {
        super();
        this.tags = new ArrayList<>();
    }

    public StandardProductEntity(String name, String description, String image, String skuCode, BigDecimal unitPrice, Integer quantityInStock, Integer reorderQuantity, CategoryEntity category, List<TagEntity> tags) {
        super(name, description, image);
        this.skuCode = skuCode;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
        this.reorderQuantity = reorderQuantity;
        this.category = category;
        this.tags = tags;
    }


    @Override
    public String toString() {
        return "entity.StandardProductEntity[ id=" + super.getProductId() + " ]";
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }
    
}
