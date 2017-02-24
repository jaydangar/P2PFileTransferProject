package Network;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class SimpleFileClient extends Thread{

    public int ReceivePort;
    public String IPAddress;
    public String ReceiveFilePath;
    public Message MessageWrite;
    
    public SimpleFileClient(Message MessageWrite,int ReceivePort,String IPAddress,String ReceiveFilePath) {
        this.MessageWrite = MessageWrite;
        this.ReceivePort = ReceivePort;
        this.IPAddress = IPAddress;
        this.ReceiveFilePath = ReceiveFilePath;
    }  
  
    @Override
    public void run(){
        
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        
    try {
        sock = new Socket(IPAddress, ReceivePort);
        MessageWrite.write("Connecting...");
        // receive file
        byte [] mybytearray  = new byte [20000000];
        InputStream is = sock.getInputStream();
        fos = new FileOutputStream(ReceiveFilePath);
        bos = new BufferedOutputStream(fos);
        bytesRead = is.read(mybytearray,0,mybytearray.length);
        current = bytesRead;

        do {
             bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
             if(bytesRead >= 0) current += bytesRead;
           } while(bytesRead > -1);

        bos.write(mybytearray, 0 , current);
        bos.flush();
        MessageWrite.write("File " + ReceiveFilePath + " downloaded (" + current + " bytes read)");    
    }catch(Exception e){
        e.printStackTrace();
    }
    
        finally {
            try{
                if (fos != null) fos.close();
                if (bos != null) bos.close();
                if (sock != null) sock.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}