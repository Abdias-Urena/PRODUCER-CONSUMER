/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian
 */
public class Producer extends Thread{
    private final Buffer buffer;
    private static final ArrayList numbersConteiner = new ArrayList();
    private static final int MAX = 75;
    private static final int MIN = 1;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int random = generateRandomNumber();
        try {
            buffer.addNumber(random);
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
        numbersConteiner.add(random);
        for(int i= 1; i < MAX; i++){
            random = generateRandomNumber();
            if(numbersConteiner.contains(random)){
               i--;
               continue;
            }
            try {
                buffer.addNumber(random);
                numbersConteiner.add(random);
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            buffer.addNumber(-1);
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int generateRandomNumber(){
        int random = (int)(Math.random() * MAX) + MIN;
        return random;
    }
}
