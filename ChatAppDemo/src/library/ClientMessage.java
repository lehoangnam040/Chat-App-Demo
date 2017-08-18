/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.io.Serializable;

/**
 *
 * @author Nam
 */
public class ClientMessage extends Message implements Serializable {
    //message from client to server
    public static final int LOG_IN = 1;
    public static final int LOG_OUT = 2;
    public static final int CHAT = 3;
}
