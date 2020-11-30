package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RunnerReceiveSocket implements Runnable{
    DatagramSocket socketReceiver = null;
    DatagramPacket receiveDatagram = null;
    public RunnerReceiveSocket(DatagramSocket socketReceiver, DatagramPacket receiveDatagram ){
        this.socketReceiver = socketReceiver;
        this.receiveDatagram = receiveDatagram;
    }
    @Override
    public void run(){
        try{
            socketReceiver.receive(receiveDatagram);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
