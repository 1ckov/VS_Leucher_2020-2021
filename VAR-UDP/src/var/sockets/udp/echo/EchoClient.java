package var.sockets.udp.echo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

class EchoClient {
  private static final String HOST = "localhost";
  private static final int PORT = 4711;
  private static final int BUFSIZE = 512;
  private static final int TIMEOUT = 2000;

  public static void main(String[] args) {
    byte[] data = args[0].getBytes();
    try (DatagramSocket socket = new DatagramSocket()) {
      socket.setSoTimeout(TIMEOUT); // Zeit in ms, für wie lange ein read() auf socket blockiert.
                                    // Bei timeout is java.net.SocketTimeoutException (TIMEOUT == 0
                                    // => blockiert für immer)
      InetAddress iaddr = InetAddress.getByName(HOST);
      DatagramPacket packetOut = new DatagramPacket(data, data.length, iaddr, PORT);
      socket.send(packetOut);
      DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
      socket.receive(packetOut);
      String received = new String(packetIn.getData(), 0, packetIn.getLength());
      System.out.println("Received: " + received);
    } catch (SocketTimeoutException e) {
      System.err.println("Timeout: " + e.getMessage());
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}