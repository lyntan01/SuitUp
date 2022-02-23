/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author lyntan
 */
@Entity
public class ManufacturingIssueEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long manufacturingIssueId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String name;
    @Column(length = 128)
    @Size(max = 128)
    private String description;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private OrderEntity order;

    public ManufacturingIssueEntity() {
    }

    public Long getManufacturingIssueId() {
        return manufacturingIssueId;
    }

    public void setManufacturingIssueId(Long manufacturingIssueId) {
        this.manufacturingIssueId = manufacturingIssueId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (manufacturingIssueId != null ? manufacturingIssueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the manufacturingIssueId fields are not set
        if (!(object instanceof ManufacturingIssueEntity)) {
            return false;
        }
        ManufacturingIssueEntity other = (ManufacturingIssueEntity) object;
        if ((this.manufacturingIssueId == null && other.manufacturingIssueId != null) || (this.manufacturingIssueId != null && !this.manufacturingIssueId.equals(other.manufacturingIssueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ManufacturingIssueEntity[ id=" + manufacturingIssueId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }
    
}
