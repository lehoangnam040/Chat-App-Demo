/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import library.Client;
import library.ClientMessage;
import library.ServerMessage;

/**
 *
 * @author Nam
 */
public class MyServer {

    ServerSocket server;
    ArrayList<ClientSocket> clients = new ArrayList<>();

    public MyServer() {
        try {
            server = new ServerSocket(9999);
            ClientAccepter accepter = new ClientAccepter();
            accepter.start();
        } catch (IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    class ClientAccepter extends Thread {

        @Override
        public void run() {
            System.out.println("Server starting........!");
            while (true) {
                try {
                    Socket socket = server.accept();
                    ClientSocket served = new ClientSocket(socket);
                    ClientHandler handler = new ClientHandler(served);
                    handler.start();
                } catch (IOException ex) {
                    Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    class ClientHandler extends Thread {

        ClientSocket served;

        public ClientHandler(ClientSocket served) {
            this.served = served;
        }

        @Override
        public void run() {
            try {
                OutputStream os = served.socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                OUTER:
                while (true) {
                    InputStream is = served.socket.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    ClientMessage mess = (ClientMessage) ois.readObject();
                    ServerMessage serverMess;
                    switch (mess.command) {
                        case ClientMessage.LOG_IN:
                            served.client = mess.from;
                            serverMess = new ServerMessage();
                            serverMess.from = mess.from;
                            if (clients.contains(served)) {
                                serverMess.command = ServerMessage.LOGIN_FAIL;
                            } else {
                                serverMess.command = ServerMessage.LOGIN_SUCCESS;
                                for (ClientSocket other : clients) {
                                    ObjectOutputStream other_oos = new ObjectOutputStream(other.socket.getOutputStream());
                                    ServerMessage notify = new ServerMessage();
                                    notify.from = served.client;
                                    notify.command = ServerMessage.NEW_ONLINE;
                                    other_oos.writeObject(notify);
                                }
                                serverMess.user = this.getUserOnline();
                                clients.add(served);
                            }
                            oos.writeObject(serverMess);
                            break;
                        case ClientMessage.LOG_OUT:
                            clients.remove(served);
                            serverMess = new ServerMessage();
                            serverMess.from = mess.from;
                            serverMess.command = ServerMessage.USER_OUT;
                            for (ClientSocket other : clients) {
                                ObjectOutputStream other_oos = new ObjectOutputStream(other.socket.getOutputStream());
                                other_oos.writeObject(serverMess);
                            }
                            break OUTER;
                        case ClientMessage.CHAT:
                            serverMess = new ServerMessage();
                            serverMess.from = mess.from;
                            serverMess.message = mess.message;
                            serverMess.command = ServerMessage.NEW_MESS;
                            for (ClientSocket user : clients) {
                                if (user.client.equals(mess.to)) {
                                    //find the receiver of this message and send the message to them
                                    ObjectOutputStream other_oos = new ObjectOutputStream(user.socket.getOutputStream());
                                    other_oos.writeObject(serverMess);
                                    System.out.println("Send message from " + mess.from + " to " + mess.to);
                                    break;
                                }
                            }
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private ArrayList<Client> getUserOnline() {
            ArrayList<Client> online = new ArrayList<>();
            for (ClientSocket user : clients) {
                online.add(user.client);
            }
            return online;
        }
    }
}
