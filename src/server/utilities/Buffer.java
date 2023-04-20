/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;
import java.util.Queue;

/**
 *
 * @author Sebastian
 */
public class Buffer {
    private final Queue<Integer> numbers;
    private static final int MAX_BUFFER_SIZE = 3;
    
    public Buffer(Queue<Integer> numbers) {
        this.numbers = numbers;
    }

    public synchronized void addNumber(int number) throws InterruptedException {
        if(numbers.size() >= MAX_BUFFER_SIZE){
            wait();
        }
        numbers.offer(number);
        System.out.println("Produciendo: " + number);
        notifyAll();
    }
    public synchronized int removeNumber() throws InterruptedException {
        while (numbers.isEmpty()) {
            wait();
        }
        int number = numbers.poll();
        notifyAll();
        return number;
    }
    public synchronized Queue<Integer> getBuffer(){
        return numbers;
    }
}  

