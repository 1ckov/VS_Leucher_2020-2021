package var.sockets.udp.echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

class EchoServer {
  private static final int PORT = 4711;
  private static final int BUFSIZE = 512;

  public static void main(final String[] args) {
    try (DatagramSocket socket = new DatagramSocket(PORT)) {
      DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
      DatagramPacket packetOut = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

      System.out.println("Server gestartet ...");

      while (true) {
        socket.receive(packetIn);
        System.out.println(
            "Received: " + packetIn.getLength() + " bytes: " + new String(packetIn.getData()));
        packetOut.setData(packetIn.getData());
        packetOut.setLength(packetIn.getLength());
        // mehr Eigenschaften von packetOut setzen...
        socket.send(packetOut);
      }
    } catch (IOException e) {
      System.err.println(e);
    }
  }
}
