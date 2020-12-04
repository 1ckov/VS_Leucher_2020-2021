package tcp;

import java.net.Socket;

public class TestClient {

    public static Socket connectToServer(final String host, final int port){
        //Give socket host addr and port
        try (Socket socket = new Socket(host, port)){
            System.out.println("Connection Established");
            return socket;
        }
        catch(Exception e){
            System.err.println(e);
        }
        return null;
    }

    public static void readFile(final Socket socket){

    }

    public static void getTime(final Socket socket){

    }

    public static void getMessage(final Socket socket){

    }

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Socket socket = null;
        if ((socket = connectToServer(host, port)) != null){

        }
        
        
    }
    
}
