package udp;

import java.net.NetworkInterface;
import java.net.SocketException;
public class NICTest {
    public static void main(String... args) throws SocketException {
        var nics = NetworkInterface.getNetworkInterfaces().asIterator();
        while(nics.hasNext()) {
            var nic = nics.next();
            if (nic.getMTU() > 0) {
                System.out.println(nic);
                System.out.println("MTU=" + nic.getMTU());
                for (var nif : nic.getInterfaceAddresses()) {
                    if (nif.getBroadcast() != null) {
                        System.out.println(nif.getBroadcast());
                    }
                }
            }
        }
    }
}