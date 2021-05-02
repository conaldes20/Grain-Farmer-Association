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
public class Useraccount implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private String phonenum;
    private String userrole;

    public Useraccount() {
    }

    public Useraccount(Integer id) {
        this.id = id;
    }

    public Useraccount(Integer id, String username, String password, String phonenum, String userrole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phonenum = phonenum;
        this.userrole = userrole;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
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
        if (!(object instanceof Useraccount)) {
            return false;
        }
        Useraccount other = (Useraccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ng.grain.assoc.mysql_model.Useraccount[ id=" + id + " ]";
    }
    
}
