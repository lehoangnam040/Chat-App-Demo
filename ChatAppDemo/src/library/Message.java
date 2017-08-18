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
public abstract class Message implements Serializable {

    public Client from;
    public Client to;
    public String message;
    public int command;
}
