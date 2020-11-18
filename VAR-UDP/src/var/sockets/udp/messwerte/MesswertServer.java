package var.sockets.udp.messwerte;

import java.net.DatagramSocket;
import java.net.DatagramPacket;


public class MesswertServer {
  private static final int PORT = 4117;
  private static final int BUFSEIZE = 512;

  public static void main(String[] args) {
    try (DatagramSocket socket = new DatagramSocket(PORT)) {
      DatagramPacket packetIn = new DatagramPacket(new byte[BUFSEIZE], BUFSEIZE);

      System.out.println("Server started...");
      while(true){
        socket.receive(packetIn);
        String receivedData = new String(packetIn.getData(), 0, packetIn.getLength());
        String senderAddr = packetIn.getAddress().getHostAddress() + ":" + packetIn.getPort();
        System.out.println(senderAddr + " " + receivedData);
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
