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
public class Farm implements Serializable {

    
    private Integer id;
    private BigDecimal longi1;
    private BigDecimal latit1;
    private BigDecimal longi2;
    private BigDecimal latit2;
    private BigDecimal longi3;
    private BigDecimal latit3;
    private BigDecimal longi4;
    private BigDecimal latit4;
    private BigDecimal area;
    private String community;
    private String ward;
    private String localGovernment;
    private String stateLocated;
    private String crop;
    private BigDecimal areaPlanted;
    private String yearPlanted;
    private BigDecimal harvest;
    private BigDecimal netWorth;
    private BigDecimal income;
    private int farmerid;

    public Farm() {
    }

    public Farm(Integer id) {
        this.id = id;
    }

    public Farm(Integer id, BigDecimal longi1, BigDecimal latit1, BigDecimal longi2, BigDecimal latit2, BigDecimal longi3, BigDecimal latit3, BigDecimal longi4, BigDecimal latit4, BigDecimal area, String community, String ward, String localGovernment, String stateLocated, String crop, BigDecimal areaPlanted, String yearPlanted, BigDecimal harvest, BigDecimal netWorth, BigDecimal income, int farmerid) {
        this.id = id;
        this.longi1 = longi1;
        this.latit1 = latit1;
        this.longi2 = longi2;
        this.latit2 = latit2;
        this.longi3 = longi3;
        this.latit3 = latit3;
        this.longi4 = longi4;
        this.latit4 = latit4;
        this.area = area;
        this.community = community;
        this.ward = ward;
        this.localGovernment = localGovernment;
        this.stateLocated = stateLocated;
        this.crop = crop;
        this.areaPlanted = areaPlanted;
        this.yearPlanted = yearPlanted;
        this.harvest = harvest;
        this.netWorth = netWorth;
        this.income = income;
        this.farmerid = farmerid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getLongi1() {
        return longi1;
    }

    public void setLongi1(BigDecimal longi1) {
        this.longi1 = longi1;
    }

    public BigDecimal getLatit1() {
        return latit1;
    }

    public void setLatit1(BigDecimal latit1) {
        this.latit1 = latit1;
    }

    public BigDecimal getLongi2() {
        return longi2;
    }

    public void setLongi2(BigDecimal longi2) {
        this.longi2 = longi2;
    }

    public BigDecimal getLatit2() {
        return latit2;
    }

    public void setLatit2(BigDecimal latit2) {
        this.latit2 = latit2;
    }

    public BigDecimal getLongi3() {
        return longi3;
    }

    public void setLongi3(BigDecimal longi3) {
        this.longi3 = longi3;
    }

    public BigDecimal getLatit3() {
        return latit3;
    }

    public void setLatit3(BigDecimal latit3) {
        this.latit3 = latit3;
    }

    public BigDecimal getLongi4() {
        return longi4;
    }

    public void setLongi4(BigDecimal longi4) {
        this.longi4 = longi4;
    }

    public BigDecimal getLatit4() {
        return latit4;
    }

    public void setLatit4(BigDecimal latit4) {
        this.latit4 = latit4;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getLocalGovernment() {
        return localGovernment;
    }

    public void setLocalGovernment(String localGovernment) {
        this.localGovernment = localGovernment;
    }

    public String getStateLocated() {
        return stateLocated;
    }

    public void setStateLocated(String stateLocated) {
        this.stateLocated = stateLocated;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public BigDecimal getAreaPlanted() {
        return areaPlanted;
    }

    public void setAreaPlanted(BigDecimal areaPlanted) {
        this.areaPlanted = areaPlanted;
    }

    public String getYearPlanted() {
        return yearPlanted;
    }

    public void setYearPlanted(String yearPlanted) {
        this.yearPlanted = yearPlanted;
    }

    public BigDecimal getHarvest() {
        return harvest;
    }

    public void setHarvest(BigDecimal harvest) {
        this.harvest = harvest;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
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
        if (!(object instanceof Farm)) {
            return false;
        }
        Farm other = (Farm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ng.grain.assoc.mysql_model.Farm[ id=" + id + " ]";
    }
    
}
