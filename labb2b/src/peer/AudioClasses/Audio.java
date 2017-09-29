package peer.AudioClasses;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Audio {


    int port = 9002;
    DatagramSocket udpSocket;
    AudioFormat format;
    byte[] sendBuffer;

    public Audio(){
        format = new AudioFormat(8000.0f, 16, 1, true, true);
        sendBuffer = new byte[1024];
        try {
            udpSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
