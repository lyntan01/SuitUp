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
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author meganyee
 */
@Entity
public class ColourEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long colourId;
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(max = 32)
    private String name;
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(max = 32)
    private String hexCode;
    
    @OneToMany(mappedBy = "colour")
    @JoinColumn(nullable = true)
    private List<FabricEntity> fabrics;
    
    public ColourEntity() {
        this.fabrics = new ArrayList<>();
    }
    
    public ColourEntity(String name, String hexCode) {
        this();
        this.name = name;
        this.hexCode = hexCode;
    }
   
    public Long getColourId() {
        return colourId;
    }

    public void setColourId(Long colourId) {
        this.colourId = colourId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (colourId != null ? colourId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the colourId fields are not set
        if (!(object instanceof ColourEntity)) {
            return false;
        }
        ColourEntity other = (ColourEntity) object;
        if ((this.colourId == null && other.colourId != null) || (this.colourId != null && !this.colourId.equals(other.colourId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ColourEntity[ id=" + colourId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public List<FabricEntity> getFabrics() {
        return fabrics;
    }

    public void setFabrics(List<FabricEntity> fabrics) {
        this.fabrics = fabrics;
    }
    
}
