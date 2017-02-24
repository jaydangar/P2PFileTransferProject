/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jay
 */
public class FileServerConnection extends Thread {
    
    SimpleFileServer simpleFileServer;
    Socket socket;
    boolean ShouldRun = true;
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    ServerSocket ss;
        
    Scanner scan = new Scanner(System.in);
    
    public int SendPort;
    public String SendFilePath;
    public Message MessageWrite;
    public String ConnectedIP;
    static int count = 0;
    
    FileServerConnection(Socket socket, SimpleFileServer simpleFileServer, int SendPort, String SendFilePath, Message MessageWrite, String ConnectedIP, ServerSocket ss) {
        this.socket = socket;
        this.simpleFileServer = simpleFileServer;
        this.SendPort = SendPort;
        this.SendFilePath = SendFilePath;
        this.MessageWrite = MessageWrite;
        this.ss = ss;
    }

    @Override
    public void run() {
        
        try {
            fis = null;
            bis = null;
            os = null;
            File myFile = new File(SendFilePath);
            byte[] mybytearray = new byte[(int) myFile.length()];
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
            
            this.MessageWrite.write("Waiting....");
            
            for (int index = 0; index <= simpleFileServer.Connection.size(); index++) {
                    os = socket.getOutputStream();
                    MessageWrite.write("Sending " + SendFilePath + "(" + mybytearray.length + " bytes)");
                    os.write(mybytearray, 0, mybytearray.length);
                    fis.close();
                    bis.close();
                    os.flush();
                    os.close();
                    MessageWrite.write("Done.");
                    count++;
                    socket.close();
                    System.out.println(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(count == 3){         // Number of connections
                    ss.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            }  
        }
    }

/* @Override
    public void run() 
    {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            servsock = new ServerSocket(SendPort);
          while (true) {
            MessageWrite.write("Waiting........");
            try {
                
                sock = servsock.accept();
                MessageWrite.write("Accepted Connection......");
                
                // send file
                File myFile = new File (SendFilePath);
                byte [] mybytearray  = new byte [(int)myFile.length()];
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray,0,mybytearray.length);
                os = sock.getOutputStream();
                MessageWrite.write("Sending " + SendFilePath + "(" + mybytearray.length + " bytes)");
                os.write(mybytearray,0,mybytearray.length);
                os.flush();
                MessageWrite.write("Done.");
            }
            catch (Exception e) {
                MessageWrite.write(e.getMessage()+": An Inbound Connection Was Not Resolved");
            }
            finally {
                try{
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock!=null) sock.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
       }
       }catch (IOException ex) {
          Logger.getLogger(SimpleFileServer.class.getName()).log(Level.SEVERE, null, ex);
       }
        finally {
               if (servsock != null)
               try {
                   servsock.close();
               } catch (IOException ex) {
                   Logger.getLogger(SimpleFileServer.class.getName()).log(Level.SEVERE, null, ex);
               }
        }
    }*/
