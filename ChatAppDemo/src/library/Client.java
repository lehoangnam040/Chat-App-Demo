/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Nam
 */
public class Client implements Serializable {

    public String userName;
    public String name;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.userName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        return this.userName.equals(other.userName);
    }

    @Override
    public String toString() {
        return userName + " - " + name;
    }

}
