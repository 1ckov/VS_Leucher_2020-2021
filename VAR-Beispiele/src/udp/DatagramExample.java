package udp;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;



public class DatagramExample {

   private static final int PORT_IN = 8889;

   public static void main(String[] args) {
   //Opening a socket for UDP    
   DatagramSocket socketOut = null;
   DatagramSocket socketIn= null;   
   DatagramPacket packetIn = null;
   DatagramPacket packetOut = null;
      byte[] bufReceive = new byte[512];
      int length = 8;
      byte[] bufSend = {0,1,2,3,4,5,6,7};
      InetSocketAddress receiverAddress = new InetSocketAddress(8888);
      
      try {
         socketOut = new DatagramSocket(null);
         socketOut.bind(new InetSocketAddress(8888));
         //Shorter Notation
         socketIn = new DatagramSocket(PORT_IN);
         packetIn = new DatagramPacket(bufReceive, length);
         packetOut = new DatagramPacket(bufSend, length, receiverAddress);
         RunnerReceiveSocket taskToReceive = new RunnerReceiveSocket(socketIn, packetIn);
         Thread receiverClient = new Thread (taskToReceive);
         receiverClient.start();
         socketIn.send(packetOut);
         socketOut.close();
         socketIn.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   } 
}
