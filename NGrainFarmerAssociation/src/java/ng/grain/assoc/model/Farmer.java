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
public class Farmer implements Serializable {

    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String stateOfOrigin;
    private String localGovernment;
    private String ward;
    private String homeAddress;
    private String phonenum;
    private String bvn;
    private String nim;
    private int groupid;

    public Farmer() {
    }

    public Farmer(Integer id) {
        this.id = id;
    }

    public Farmer(Integer id, String firstName, String middleName, String lastName, String stateOfOrigin, String localGovernment, String ward, String homeAddress, String phonenum, String bvn, String nim, int groupid) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.stateOfOrigin = stateOfOrigin;
        this.localGovernment = localGovernment;
        this.ward = ward;
        this.homeAddress = homeAddress;
        this.phonenum = phonenum;
        this.bvn = bvn;
        this.nim = nim;
        this.groupid = groupid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getLocalGovernment() {
        return localGovernment;
    }

    public void setLocalGovernment(String localGovernment) {
        this.localGovernment = localGovernment;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
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
        if (!(object instanceof Farmer)) {
            return false;
        }
        Farmer other = (Farmer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ng.grain.assoc.mysql_model.Farmer[ id=" + id + " ]";
    }
    
}
