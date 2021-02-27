/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import static java.net.SocketOptions.SO_REUSEADDR;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abud
 */
public class ServerThread extends Thread {
    private int N;
    private int port;
    private boolean isWorking = true;
    private PersistentThread[] threads = null;
    private Queue<Socket> queue = null;
    
    public void setWorking(boolean status)
    {
        this.isWorking = status;
    }
    
    public ServerThread(int numOfThr, int port)
    {
        threads = new PersistentThread[numOfThr];
        queue = new Queue<>(numOfThr);
        for(int i =0 ; i < numOfThr; i++)
        {
            threads[i] = new PersistentThread(i, queue);
            threads[i].start();
        }
        
        
        this.port = port;
        this.N = numOfThr;
    }
    @Override
    public void run()
    {
        ServerSocket server= null;
        try {
            
            server = new ServerSocket();
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress(this.port));

        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        while(isWorking)
        {
            Socket socket = null;
            try {
                socket = server.accept();
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(isWorking)
            {
              queue.enqueue(socket);
            }
            
        }
        
        for (int i =0; i < this.N; i++)
        {
            queue.enqueue(null);
        }
        for (int i =0; i  < this.N; i++)
        {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    
    public void stopServer()
    {
        isWorking=false;
        try 
        {
            Socket s = new Socket(InetAddress.getLocalHost(), this.port);
            s.close();
            
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
       for(int i=0;i<N;i++)
         queue.enqueue(null);
       
       for(int i=0;i<N;i++)
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
    }
    
    
    
    
    
    
 
    

    

  }
    

