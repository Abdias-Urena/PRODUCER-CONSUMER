/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.utilities.Buffer;
import server.utilities.Producer;

/**
 *
 * @author Sebastian
 */
public class Server extends Thread{
    private ServerSocket serv = null;
    static List<ClientManager> users = new ArrayList<>();
    
    
    public Server(){
        try {
            this.serv =  new ServerSocket(8000);
            System.out.println("Server opened");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        while(true){
                try {
                    Socket client = this.serv.accept();
                    System.out.println("User connected: " + client.getInetAddress());
                    ClientManager manager = new ClientManager(client);
                    addUser(manager);
                    manager.sendMessage(200);
                    Thread t = new Thread(manager);
                    t.start();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }
        
    }
    
    public static void addUser(ClientManager manager){
        synchronized(Server.users){
            Server.users.add(manager);
        }
    }
    
    public static void removeUser(ClientManager manager){
        //synchronized(Server.users){
            Server.users.remove(manager);
        //}
    }
    
    public static void sendAllMessages(int number) throws InterruptedException{
        synchronized(Server.users){
            for(ClientManager client : users){
                client.sendMessage(number);
            }
        }
    }

    
    public static boolean isReceived(){
        synchronized(Server.users){
            for(ClientManager client : users){
                try {
                    String data = client.getRead().readUTF();
                    if(!data.equals("2")){//se recibio el mensaje
                        return false;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }
    
}
