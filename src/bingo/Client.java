/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bingo;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 *
 * @author Sebastian
 */
public class Client {
    static Socket sfd = null;
    static DataInputStream EntradaSocket;
    static DataOutputStream SalidaSocket;
    
    static TextField text;

    public static void main(String[] args) throws IOException, InterruptedException {
        try
        {
          sfd = new Socket("25.49.127.138",8000);
          EntradaSocket = new DataInputStream(new BufferedInputStream(sfd.getInputStream()));
          SalidaSocket = new DataOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
        }catch (UnknownHostException uhe)
        {
          System.out.println("No se puede acceder al servidor.");
          System.exit(1);
        }
        catch (IOException ioe)
        {
          System.out.println("Comunicación rechazada.");
          System.exit(1);
        }
        while (true)
        {
          try
          {
            int linea = Integer.parseInt(EntradaSocket.readUTF());
            //System.out.println("Consumiendo: " + linea);
            if(linea == 200){
                SalidaSocket.writeUTF("2");
                SalidaSocket.flush();
                System.out.println("Welcome to Bingo");
            }
            if(linea <= 75){
                SalidaSocket.writeUTF("2");
                SalidaSocket.flush();
                System.out.println("Viene numero\nEl: " + linea);
                //sleep(2000);
            }
            if(linea ==-1){
                System.out.println("Se termino la tombola");
                //sleep(2000);
            }
            
          }
          catch(IOException ioe)
          {
            System.out.println(ioe.getMessage());
            System.exit(1);
          }
        }
    }

}