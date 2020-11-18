package udp;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;

public class Broadcast {
    private static final int PORT_SENDER = 4117;
    private static final int PORT_TARGET = 4118;
    private static final int BUFSIZE = 512;
    //Just to be on the safe side.
    private static final String BROADCAST = "192.168.0.255";

    public static void main(String[] args) throws SocketException {

        var nics = NetworkInterface.getNetworkInterfaces().asIterator();
        while(nics.hasNext()){
            var nic = nics.next();
            if(nic.getMTU() >= 512){
                System.out.println("Checking " + nic + " Interface");
                System.out.println("MTU Required 512, MTU given " + nic.getMTU());
                for (var nif : nic.getInterfaceAddresses()){
                    if(nif.getBroadcast() != null){
                        System.out.println("Sending to " + nif.getBroadcast());
                        try(DatagramSocket socket = new DatagramSocket(PORT_SENDER)) {
                            InetAddress iaddr = nif.getBroadcast();
                            DatagramPacket packetOut = new DatagramPacket(new byte[BUFSIZE] , BUFSIZE, iaddr, PORT_TARGET);
                            /**We are gonna do this 20 Times instead of forever,
                             *  so we can see check if there is another network to broadcast to */
                            for(int i = 0; i < 20; i++){
                                byte[] data = (new Date().toString()).getBytes();
                                packetOut.setData(data);
                                System.out.println(new String(data));
                                socket.send(packetOut);
                                Thread.sleep(20000);
                            }
                            socket.close();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                        

                    }
                }
                
            }
        }

    
        

    }

    
}
