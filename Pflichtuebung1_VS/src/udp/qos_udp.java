package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.CRC32;

/*

    This file combines client and server for a quality of service test via udp.
    The mode in which the program is operating is defined via commandline arguments.

    As Server:
    Usage: s <port: int> <expected packages: int> <timeout: int>

    As Client:
    "Usage: c <address: ip address> <port: int> <packages to send: int>"

*/

class QoSUDP {
    private static final int DATA_SIZE = 1024; // size of random data to be generated
    private static final int WAIT_AFTER_SEND = 20; // wait time between sending messages in ms


    public static void main(String[] args) {
        if (args[0].equals("s")) {
            server(args);
        } else if (args[0].equals("c")) {
            client(args);
        } else {
            System.out.println("Specify either client (c) or server (s) mode");
        }
    }

    public static void server(String[] args) {
        try {
            int port = Integer.parseInt(args[1]);
            int num_packages = Integer.parseInt(args[2]);
            int timeout = Integer.parseInt(args[3]);
            MeasureServer s = new MeasureServer(port, num_packages, timeout, DATA_SIZE);
            s.run();
        } catch (Exception e) {
            System.out.println("Usage: s <port: int> <expected packages: int> <timeout: int>");
            System.out.println(e);
        }
    }

    public static void client(String[] args) {
        try {
            String address = args[1];
            int port = Integer.parseInt(args[2]);
            int num_packages = Integer.parseInt(args[3]);
            MeasureClient c = new MeasureClient(port, address, num_packages, WAIT_AFTER_SEND, DATA_SIZE);
            c.run();
        } catch (Exception e) {
            System.out.println("Usage: c <address: ip address> <port: int> <packages to send: int>");
            System.out.println(e);
        }

    }
}

class CRC32Packet {
    public static byte[] encode(byte[] data) {

        // Generate crc code
        CRC32 crc = new CRC32();
        crc.update(data);
        
        // Buffer for crc checksum, will be put at the end of data part of the package
        ByteBuffer crc_buf = ByteBuffer.allocate(Long.BYTES);
        crc_buf.putLong(crc.getValue());

        // crc as byte array
        byte[] crc_value = crc_buf.array();

        // combined buffer
        byte[] payload = new byte[data.length + 8];

        // assemble payload, first part is a variable length amount of data
        for (int i = 0; i < data.length; i++) {
            payload[i] = data[i];
        }

        // assemble payload, second part is a fixed length crc checksum
        for (int i = 0; i < crc_value.length; i++) {
            payload[data.length + i] = crc_value[i];
        }
        return payload;
    }

    public static boolean validate(byte[] data) {
        byte[] payload_data = Arrays.copyOfRange(data, 0, data.length - Long.BYTES); // extract package content without crc supplied as long
        byte[] crc_data = Arrays.copyOfRange(data, data.length - Long.BYTES, data.length); // extract crc saved as long from package

        CRC32 crc = new CRC32();
        crc.update(payload_data);
        long crc_value_calculated = crc.getValue();

        // get crc value from package
        long crc_value_received = ByteBuffer.wrap(crc_data).getLong();

        return crc_value_received == crc_value_calculated;
    }
}

class MeasureClient {
    private int num_packages;
    private int data_size;
    private int port;
    private int waitAfterSend;
    private String addr;

    public MeasureClient(int port, String addr, int num_packages, int waitAfterSend, int data_size) {
        this.data_size = data_size;
        this.num_packages = num_packages;
        this.addr = addr;
        this.port = port;
        this.waitAfterSend = waitAfterSend;
    }

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(addr);

            // Send init package telling the server that the test is about to start
            socket.send(new DatagramPacket("start".getBytes(), "start".getBytes().length, address, port));

            while (num_packages > 0) {
                num_packages--;

                // Generate random data to be sent
                byte[] b = new byte[data_size];
                new Random().nextBytes(b);

                // append crc
                byte[] payload = CRC32Packet.encode(b);

                socket.send(new DatagramPacket(payload, payload.length, address, port));

                // wait to not overhelm the network interface
                Thread.sleep(waitAfterSend);
            }
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}

class MeasureServer {
    private int remaining_packages;
    private int port;
    private int timeout;
    private int num_packages;
    private int packages_recieved;
    private int packages_faulty;
    private DatagramSocket socket;
    private byte[] buf;


    public MeasureServer(int port, int num_packages, int timeout, int data_size) {
        this.remaining_packages = num_packages;
        this.num_packages = num_packages;
        this.port = port;
        this.timeout = timeout;
        this.buf = new byte[data_size + Long.BYTES];
    }

    public void run() {

        // Open Socket
        try {
            socket = new DatagramSocket(port);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        // Wait for begin of transmission
        try {
            waitForInitPackage();
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        // Set Timeout
        try {
            socket.setSoTimeout(timeout);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        // Count and evaluate loop
        while(remaining_packages > 0) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException et) {
                System.out.println("Timed out waiting for packet");
                this.remaining_packages--;
                continue;
            } catch (Exception e) {
                System.out.println(e);
            }

            // Update package count
            this.remaining_packages--;
            this.packages_recieved++;

            // check if package was damaged
            if (!CRC32Packet.validate(packet.getData())) {
                packages_faulty++;
            }
        }

        // Post result
        StringBuilder sb = new StringBuilder();
        sb.append("Package Size: " + buf.length + "\n");
        sb.append("Total packages: " + num_packages + "\n");
        sb.append("Packages Lost: " + (num_packages - packages_recieved) + "\n");
        sb.append("Packages damaged: " + packages_faulty + "\n");
        System.out.println(sb.toString());
    }

    private void waitForInitPackage() throws IOException {
        boolean init_package_received = false;

        // Wait until package with "start" as content is received to let client controll when test starts
        while (!init_package_received){
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            if (new String(packet.getData()).trim().equals("start")) init_package_received = true;
        }
    }
}