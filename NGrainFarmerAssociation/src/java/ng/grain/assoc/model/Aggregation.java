/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.model;

import java.io.Serializable;

/**
 *
 * @author CONALDES
 */
public class Aggregation implements Serializable {

    
    private Integer id;
    private String town;
    private String ward;
    private String localGovernment;
    private String stateLocated;
    private int groupid;
    private String aggregationLevel;

    public Aggregation() {
    }

    public Aggregation(Integer id) {
        this.id = id;
    }

    public Aggregation(Integer id, String town, String ward, String localGovernment, String stateLocated, int groupid, String aggregationLevel) {
        this.id = id;
        this.town = town;
        this.ward = ward;
        this.localGovernment = localGovernment;
        this.stateLocated = stateLocated;
        this.groupid = groupid;
        this.aggregationLevel = aggregationLevel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
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

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getAggregationLevel() {
        return aggregationLevel;
    }

    public void setAggregationLevel(String aggregationLevel) {
        this.aggregationLevel = aggregationLevel;
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
        if (!(object instanceof Aggregation)) {
            return false;
        }
        Aggregation other = (Aggregation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ng.grain.assoc.mysql_model.Aggregation[ id=" + id + " ]";
    }
    
}
