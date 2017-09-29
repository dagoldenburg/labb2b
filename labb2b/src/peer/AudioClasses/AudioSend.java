package peer.AudioClasses;

import peer.ListenForCalls.CallListener;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class AudioSend extends Audio implements Runnable {
    TargetDataLine microphone;
    DataLine.Info info;
    String ip;
//https://stackoverflow.com/questions/2083342/how-to-send-audio-stream-via-udp-in-java

    //https://stackoverflow.com/questions/25798200/java-record-mic-to-byte-array-and-play-sound

    public AudioSend(String ip){
        super();
        try {
            this.ip = ip;
            microphone = AudioSystem.getTargetDataLine(format);
            info  = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
        }catch (LineUnavailableException e2) {
            e2.printStackTrace();
        }
    }
    private void send() throws IOException{
        try {
            microphone.read(sendBuffer, 0, sendBuffer.length);
            DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(ip), super.port);
            super.udpSocket.send(packet);
        } catch (IOException ex) {
            return;
        }
    }
    @Override
    public void run() {
        try {
            while (CallListener.getStatus().equals("BUSY")) {
                send();
            }
        }catch(IOException e){
            return;
        }finally{
            microphone.close();
            udpSocket.close();
        }
    }
}
