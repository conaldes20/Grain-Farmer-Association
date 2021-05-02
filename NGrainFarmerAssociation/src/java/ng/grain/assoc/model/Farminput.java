/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author CONALDES
 */
public class Farminput implements Serializable {
    private Integer id;
    private String farmInput;
    private BigDecimal inputCost;
    private String yearGiven;
    private int farmerid;

    public Farminput() {
    }

    public Farminput(Integer id) {
        this.id = id;
    }

    public Farminput(Integer id, String farmInput, BigDecimal inputCost, String yearGiven, int farmerid) {
        this.id = id;
        this.farmInput = farmInput;
        this.inputCost = inputCost;
        this.yearGiven = yearGiven;
        this.farmerid = farmerid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFarmInput() {
        return farmInput;
    }

    public void setFarmInput(String farmInput) {
        this.farmInput = farmInput;
    }

    public BigDecimal getInputCost() {
        return inputCost;
    }

    public void setInputCost(BigDecimal inputCost) {
        this.inputCost = inputCost;
    }

    public String getYearGiven() {
        return yearGiven;
    }

    public void setYearGiven(String yearGiven) {
        this.yearGiven = yearGiven;
    }

    public int getFarmerid() {
        return farmerid;
    }

    public void setFarmerid(int farmerid) {
        this.farmerid = farmerid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Farminput)) {
            return false;
        }
        Farminput other = (Farminput) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ng.grain.assoc.mysql_model.Farminput[ id=" + id + " ]";
    }
    
}
