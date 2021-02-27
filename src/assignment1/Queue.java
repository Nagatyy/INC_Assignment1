/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import java.net.Socket;

/**
 *
 * @author abud
 */
public class Queue<T> {
    private T []store=null;
    private int in=0, out=0, inStore=0, N;
    public Queue(int n)
    {
        N=n;
        store = (T[]) new Object[N];
    }
    public synchronized void enqueue(T x)
    {
        while(inStore==N)
            try {
                wait();
            } catch (InterruptedException ex) {
                
            }
        store[in]=x;
        in = (in+1) % N;
        inStore++;
        notifyAll();        
    }

    public synchronized T dequeue()
    {
        while(inStore==0)
            try {
                wait();
            } catch (InterruptedException ex) {
                
            }
        T temp = store[out];
        out = (out+1) % N;
        inStore--;
        notifyAll();                
        return temp;
    }
}
