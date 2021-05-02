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
public class Subscriber implements Serializable {

    private Integer id;
    private String email;
    private String status;

    public Subscriber() {
    }

    public Subscriber(Integer id) {
        this.id = id;
    }

    public Subscriber(Integer id, String email, String status) {
        this.id = id;
        this.email = email;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof Subscriber)) {
            return false;
        }
        Subscriber other = (Subscriber) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ng.grain.assoc.mysql_model.Subscriber[ id=" + id + " ]";
    }
    
}
