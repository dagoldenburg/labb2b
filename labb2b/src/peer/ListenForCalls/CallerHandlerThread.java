package peer.ListenForCalls;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CallerHandlerThread implements Runnable {

    String status;
    Socket connection;

    CallerHandlerThread(String status,Socket connection){
        this.status = status;
        this.connection = connection;
    }

    @Override
    public void run() {
            try {
                BufferedReader fromPeer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream toPeer = new DataOutputStream(connection.getOutputStream());
                if(status.equals("BUSY")){
                    toPeer.writeBytes("BUSY");
                    return;
                }
                if(fromPeer.readLine().equals("INVITE")){
                    CallListener.setStatus("BUSY");
                    toPeer.writeBytes("TRO");
                    if(fromPeer.readLine().equals("ACK")){
                        System.out.println("Press x if you want to hang up.");
                        //TODO: börja skicka voicepackets i annan tråd
                    }else throw new IOException();
                    if(fromPeer.readLine().equals("BYE")){
                        //TODO: send OK?
                    }
                }else throw new IOException();

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try {
                    connection.close();
                    CallListener.setStatus("ACK");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return;
    }

}
