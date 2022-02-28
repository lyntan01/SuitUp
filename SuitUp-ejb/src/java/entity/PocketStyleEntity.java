/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author meganyee
 */
@Entity
public class PocketStyleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pocketStyleId;
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(max = 32)
    private String name;
    @NotNull
    @Digits(integer = 9, fraction = 2)
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal additionalPrice;
    @Column(nullable = false, length = 128)
    @NotNull
    @Size(min = 2, max = 128)
    private String description;
    @Column(nullable = false, length = 128)
    @NotNull
    @Size(min = 2, max = 128)
    private String image;
    

    public Long getPocketStyleId() {
        return pocketStyleId;
    }

    public void setPocketStyleId(Long pocketStyleId) {
        this.pocketStyleId = pocketStyleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pocketStyleId != null ? pocketStyleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the pocketStyleId fields are not set
        if (!(object instanceof PocketStyleEntity)) {
            return false;
        }
        PocketStyleEntity other = (PocketStyleEntity) object;
        if ((this.pocketStyleId == null && other.pocketStyleId != null) || (this.pocketStyleId != null && !this.pocketStyleId.equals(other.pocketStyleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PocketStyleEntity[ id=" + pocketStyleId + " ]";
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
