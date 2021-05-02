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
public class Fmgroup implements Serializable {

    private Integer id;
    private String groupName;
    private String address;
    private String ward;
    private String localGovernment;
    private String stateLocated;
    private String crops;

    public Fmgroup() {
    }

    public Fmgroup(Integer id) {
        this.id = id;
    }

    public Fmgroup(Integer id, String groupName, String address, String ward, String localGovernment, String stateLocated, String crops) {
        this.id = id;
        this.groupName = groupName;
        this.address = address;
        this.ward = ward;
        this.localGovernment = localGovernment;
        this.stateLocated = stateLocated;
        this.crops = crops;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCrops() {
        return crops;
    }

    public void setCrops(String crops) {
        this.crops = crops;
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
        if (!(object instanceof Fmgroup)) {
            return false;
        }
        Fmgroup other = (Fmgroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ng.grain.assoc.mysql_model.Fmgroup[ id=" + id + " ]";
    }
    
}
