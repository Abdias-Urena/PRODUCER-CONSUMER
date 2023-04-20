/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import static server.Server.*;
import server.utilities.Buffer;
import server.utilities.Producer;

/**
 *
 * @author Sebastian
 */
public class ClientManager extends Thread{
    private Socket client;
    private DataInputStream read;
    private DataOutputStream write;
    
    private final Queue<Integer> numbers = new LinkedList<>();
    public Buffer buffer = new Buffer(numbers);
    public Thread producerThread =  new Thread(new Producer(buffer));
    
    public ClientManager(Socket client){
        this.client = client;
        try {
            this.read = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            this.write = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public DataInputStream getRead(){
        return read;
    }
    
    public DataOutputStream getWrite(){
        return write;
    }
    
    @Override
    public void run(){
        producerThread.start();
        while(true){
            try {
                if(!Server.users.isEmpty()){
                    String receive = this.read.readUTF();
                    if(receive.equals("2")){
                        System.out.println(receive);
                        int number = buffer.removeNumber();
                        sendAllMessages(number);
                    }
                    else{
                        System.out.println(receive);
                    }
                }else{
                    System.out.println("Esperando usuarios");
                }
            } catch (IOException ex) {
                try {
                    client.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex1);
                }
                removeUser(this);
                break;
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        try {
            sendAllMessages(-1);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void sendMessage(int number){
        try {
            synchronized(this.write){
                this.write.writeUTF(String.valueOf(number));
                this.write.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
