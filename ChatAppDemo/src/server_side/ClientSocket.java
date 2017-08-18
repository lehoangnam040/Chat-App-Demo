/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side;

import java.net.Socket;
import library.Client;

/**
 *
 * @author Nam
 */
public class ClientSocket {

    Client client;
    Socket socket;

    public ClientSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final ClientSocket other = (ClientSocket) obj;
        return this.client.equals(other.client);
    }

}
