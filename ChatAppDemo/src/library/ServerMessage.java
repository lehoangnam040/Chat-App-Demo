/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Nam
 */
public class ServerMessage extends Message implements Serializable {

    //message from server to client
    public ArrayList<Client> user;
    public static final int LOGIN_SUCCESS = -1;
    public static final int LOGIN_FAIL = -2;
    public static final int NEW_ONLINE = -3;
    public static final int NEW_MESS = -4;
    public static final int USER_OUT = -5;
}
