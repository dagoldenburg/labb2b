package peer.ListenForCalls;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class CallListener implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private static String status = "ACK";

    public static String getStatus(){
        return status;
    }
    public static synchronized void setStatus(String status){
        status = status;
    }


    public CallListener() throws IOException{
        serverSocket = new ServerSocket(9001);
    }

    @Override
    public void run(){
        while(true){
            try {
                Socket connectionSocket = serverSocket.accept();
                Thread t = new Thread(new CallerHandlerThread(status,connectionSocket));
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
