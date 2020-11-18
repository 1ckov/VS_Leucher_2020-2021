package var.sockets.udp.time;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Date;

public class TimeServer {
    private static final int PORT = 4711;
    private static final int BUFSIZE = 512;

  public static void main(String[] args) {
      try(DatagramSocket socket = new DatagramSocket(PORT)){
        DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
        DatagramPacket packetOut = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
  
        System.out.println("Server started ...");

        while(true){
          socket.receive(packetIn);
          System.out.println("Time Request Received from: " + packetIn.getAddress().toString());
          packetOut.setSocketAddress(packetIn.getSocketAddress());
          String now = (new Date()).toString();
          packetOut.setData(now.getBytes());
          packetOut.setLength(now.length());
          socket.send(packetOut);
        }
      } 
      catch (Exception e) {
        System.err.println(e);
      }
  }
}
