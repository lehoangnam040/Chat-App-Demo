/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side;

import java.awt.CardLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import library.Client;
import library.ClientMessage;
import library.ServerMessage;

/**
 *
 * @author Nam
 */
public class MainScreen extends javax.swing.JFrame {

    /**
     * Creates new form ChatScreen
     */
    LoginForm login;
    ChatScreen chat;
    Socket socket;
    Client client;
    HashMap<Client, ChatDialog> online = new HashMap<>();
    boolean running;

    public MainScreen() {
        initComponents();
        login = new LoginForm(this);
        chat = new ChatScreen(this);
        panelMain.setLayout(new CardLayout());
        panelMain.add(login, "Login");
        panelMain.add(chat, "Chat");
        ((CardLayout) panelMain.getLayout()).show(panelMain, "Login");
    }

    class ServerListener extends Thread {

        @Override
        public void run() {
            running = true;
            try {
                while (running) {
                    InputStream is = socket.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    ServerMessage mess = (ServerMessage) ois.readObject();
                    DefaultListModel<Client> model;
                    ChatDialog dialog;
                    switch (mess.command) {
                        case ServerMessage.LOGIN_SUCCESS:
                            ((CardLayout) panelMain.getLayout()).show(panelMain, "Chat");
                            chat.lblName.setText(mess.from.name);
                            model = new DefaultListModel();
                            for (Client c : mess.user) {
                                model.addElement(c);
                                dialog = new ChatDialog(chat.main, false, c);
                                online.put(c, dialog);
                                dialog.setVisible(false);
                            }
                            chat.listUser.setModel(model);
                            break;
                        case ServerMessage.LOGIN_FAIL:
                            JOptionPane.showMessageDialog(panelMain, "this account has been used");
                            break;
                        case ServerMessage.NEW_ONLINE:
                            model = (DefaultListModel) chat.listUser.getModel();
                            model.addElement(mess.from);
                            dialog = new ChatDialog(chat.main, false, mess.from);
                            online.put(mess.from, dialog);
                            dialog.setVisible(false);
                            break;
                        case ServerMessage.NEW_MESS:
                            System.out.println("nhan dc tin nhan tu " + mess.from);
                            online.get(mess.from).setVisible(true);
                            online.get(mess.from).getMessage(mess.from.name, mess.message);
                            break;
                        case ServerMessage.USER_OUT:
                            ((DefaultListModel) chat.listUser.getModel()).removeElement(mess.from);
                            online.get(mess.from).dispose();
                            online.remove(mess.from, online.get(mess.from));
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void sendToServer(ClientMessage message) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void login(LoginForm loginForm) {
        try {
            // TODO add your handling code here:
            socket = new Socket("localhost", 9999);
            MainScreen.ServerListener listener = new ServerListener();
            listener.start();
            client = new Client();
            client.userName = loginForm.txtUser.getText();
            client.name = loginForm.txtName.getText();
            ClientMessage mess = new ClientMessage();
            mess.command = ClientMessage.LOG_IN;
            mess.from = client;
            this.sendToServer(mess);
        } catch (IOException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMain = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel panelMain;
    // End of variables declaration//GEN-END:variables
}
