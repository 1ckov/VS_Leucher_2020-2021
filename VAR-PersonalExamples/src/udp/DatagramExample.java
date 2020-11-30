package udp;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class DatagramExample {
   public static void main(String[] args) {
   //Opening a socket for UDP    
      DatagramSocket socketSender = null;
      DatagramSocket socketReceiver= null;   
      DatagramPacket receiveDatagram = null;
      DatagramPacket sendDatagram = null;
      byte[] bufReceive = new byte[8];
      int length = 8;
      byte[] bufSend = {0,1,2,3,4,5,6,7};
      InetSocketAddress receiverAddress = new InetSocketAddress(8888);
      
      try {
         socketSender = new DatagramSocket(null);
         socketSender.bind(new InetSocketAddress(8888));
         //Shorter Notation
         socketReceiver = new DatagramSocket(8889);
         receiveDatagram = new DatagramPacket(bufReceive, length);
         sendDatagram = new DatagramPacket(bufSend, length, receiverAddress);
         RunnerReceiveSocket taskToReceive = new RunnerReceiveSocket(socketReceiver, receiveDatagram);
         Thread receiverClient = new Thread (taskToReceive);
         receiverClient.start();
         socketReceiver.send(sendDatagram);
         socketSender.close();
         socketReceiver.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   } 
}
