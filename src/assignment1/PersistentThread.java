/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abud
 */
public class PersistentThread extends Thread {
    private Queue<Socket> queue = null;
    private int ID;
         
    public PersistentThread(int i, Queue<Socket> queue )
    {
        this.ID = i;
        this.queue = queue;
    }
    
    public String md5(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
        return hashtext;
    }
    
    

    @Override
    public void run() {
        Socket socket = queue.dequeue();
        while (socket !=null)
        {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                String message = reader.readLine();
                System.out.println(md5(message));
                while (!message.equals("close"))
                {
                     writer.println(md5(message));
                     message = reader.readLine();
                     System.out.println(md5(message));
                }
                
                reader.close();
                writer.close();
                socket.close();
                socket = queue.dequeue();  
            } catch (IOException ex) {
                Logger.getLogger(PersistentThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(PersistentThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally{
                try{
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(PersistentThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    
    
}
