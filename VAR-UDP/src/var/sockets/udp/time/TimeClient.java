package var.sockets.udp.time;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class TimeClient {
  private static final int PORT = 4711;
  private static final int BUFSIZE = 512;
  private static final String HOSTNAME = "localhost";
  private static final int TIMEOUT = 2000;

  public static void main(String[] args) {
    try(DatagramSocket socket = new DatagramSocket()) {
      socket.setSoTimeout(TIMEOUT);
      InetAddress iaddr = InetAddress.getByName(HOSTNAME);
      /**Would have been shorter to use this constructor.
       * DatagramPacket packetOut = new DatagramPacket(new byte[0], 0, iaddr, PORT);*/
      DatagramPacket packetOut = new DatagramPacket(new byte[0], 0);
      DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
      packetOut.setAddress(iaddr);
      packetOut.setPort(PORT);
      socket.send(packetOut);
      socket.receive(packetIn);
      String received = new String(packetIn.getData(), 0, packetIn.getLength());
      System.out.println(
        "Received " + packetIn.getLength() + " bytes of Data.\nThe current time is: " + received 
      );

    }
    catch (SocketTimeoutException e) {
      System.err.println("Timeout of " + TIMEOUT/1000 + "s has been Reached: " + e.getMessage());
    }
    catch (Exception e) {
      System.err.println(e);
    }
  }
}
