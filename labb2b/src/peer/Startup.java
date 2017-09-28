package peer;

import peer.ListenForCalls.CallListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Startup {
    private static Socket socket;
    private static BufferedReader fromPeer;
    private static DataOutputStream toPeer;
    private String name;

    public static void main(String[] args){

        Thread t = null;
        try {
            t = new Thread(new CallListener());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        t.start();

        Scanner s = new Scanner(System.in);
        String input = new String();
        String ip;
        String[] split;
        boolean inSession;
        while(true){ //waiting
            System.out.println("Welcome, if you want to call someone write: call <ip>");
            input = s.nextLine();
            try {
                if (input.substring(0, 5).equalsIgnoreCase("call ")) {
                    ip = input.substring(5);
                    Socket socket = new Socket(ip, 9001);
                    toPeer = new DataOutputStream(socket.getOutputStream());
                    fromPeer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    socket.setSoTimeout(10000);
                    toPeer.writeBytes("INVITE");
                    if(!fromPeer.readLine().equals("TRO")){
                        throw new IOException();
                    }
                    toPeer.writeBytes("ACK");
                    inSession = true;
                    while(inSession=true){ // in session
                        System.out.println("Press x if you want to hang up.");
                        //TODO: skicka voicepackets i annan tråd
                        input = s.nextLine();
                        if(input.equalsIgnoreCase("x")){
                            toPeer.writeBytes("BYE");
                            inSession = false;
                        }
                    }
                }
            } catch(SocketTimeoutException e){
                System.out.println("Connection timed out");
            } catch(StringIndexOutOfBoundsException e){

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    //TODO: se till att voicepacket tråden dödas när denna tcp connection dör
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
