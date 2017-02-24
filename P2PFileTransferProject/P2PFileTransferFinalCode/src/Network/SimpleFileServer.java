package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleFileServer extends Thread {

    ServerSocket ss;
    ArrayList<FileServerConnection> Connection = new ArrayList<FileServerConnection>();

    public int SendPort;
    public String SendFilePath;
    public Message MessageWrite;
    public String ConnectedIP;
        
    Scanner scan = new Scanner(System.in);
            
    public SimpleFileServer(int SendPort, String SendFilePath, Message MessageWrite) {
        this.SendPort = SendPort;
        this.SendFilePath = SendFilePath;
        this.MessageWrite = MessageWrite;

        try {
            
            ss = new ServerSocket(SendPort,50);
            while (true) {
                Socket s = ss.accept();
                FileServerConnection sc = new FileServerConnection(s, this, SendPort, SendFilePath, MessageWrite, ConnectedIP, ss);
                Connection.add(sc);
                sc.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(SimpleFileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
