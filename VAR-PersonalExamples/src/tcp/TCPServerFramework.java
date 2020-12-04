package tcp;

import java.io.BufferedReader;
import java.io.File;
//For Reading files
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServerFramework {
   
    private int port;
    private int backlog;
    private String flag;
    private ExecutorService threadPool;


    public TCPServerFramework(final int port, final int backlog){
        this.port = port;
        this.backlog = backlog;
        this.threadPool = Executors.newFixedThreadPool(4);
    }

    public TCPServerFramework(final String flag, final int port, final int backlog){
        this.flag = flag;
        this.port = port;
        this.backlog = backlog;
        this.threadPool = Executors.newFixedThreadPool(4);
    }

    /** Here we   */
    public void start(){
        //Open up a Server socket with the given port
        try (ServerSocket welcomeSocket = new ServerSocket(port,backlog)) {
            //Verbose announcement that we are awaiting a client
            System.out.println("Server Started at <"+ welcomeSocket.getLocalSocketAddress() + "> and awaiting connections ...");
            //Decide the Type of Server
            switch (flag) {
                //File Server
                case "-f":
                    while (true){
                        Socket socket = welcomeSocket.accept();
                        this.threadPool.execute(new FileThread(socket));
                    }
                //Time Server
                case "-t":
                    while (true){
                        Socket socket = welcomeSocket.accept();
                        this.threadPool.execute(new TimeThread(socket));
                    }
                //Echo Server
                case "-e":
                    while (true){
                        Socket socket = welcomeSocket.accept();
                        this.threadPool.execute(new EchoThread(socket));
                    }
                //Message Server
                default:
                    while (true){
                        Socket socket = welcomeSocket.accept();
                        this.threadPool.execute(new GreeterThread(socket));
                    }
            }
        } 
        catch (Exception e) {
            System.err.println(e);
        }
    }

    private class GreeterThread implements Runnable{
        private Socket socket;

        public GreeterThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            //Connect a Print Writer to the output Stream of our socket
            try(PrintWriter out = new PrintWriter(socket.getOutputStream())){  
                
                //Verbose data About Client
                SocketAddress clientAddr = socket.getRemoteSocketAddress();
                System.out.println("Client<"+ clientAddr + "> has established a connection");
                
                //Printing a welcome message to the client
                out.println("Hello World ! We are open for buisness");

            }
            catch(IOException e ){
                System.err.println(e);
            }
            finally {
                System.out.println("Connection to Client was Terminated");
            }
        }
        
    }

    private class EchoThread implements Runnable{
        private Socket socket;
        
        public EchoThread(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            //Connect a  Buffered Reader(Its needed so we dont read a char at a time) 
            //to the input Stream of our socket to read input from Clients 
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //Connect a Print Writer to the output Stream of our socket
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){
                
                //Verbose data About Client
                SocketAddress clientAddr = socket.getRemoteSocketAddress();
                System.out.println("Client<"+ clientAddr + "> has established a connection");
                
                //Buffer for data from Client
                String line;
                
                //Read Data from Client one line at a time
                while((line = in.readLine()) != null){
                    System.out.println(clientAddr + ">> [" + line + "]");
                    out.println("echo: " + line);
                }

            }
            catch (IOException e){
                System.err.println(e);
            }
            finally {
                System.out.println("Connection to Client was Terminated");
            }

        }
        
    }

    private class FileThread implements Runnable{
        private Socket socket;
        private final static String FILENAME = "/home/sa6o/LoremIpsum.txt";

       public FileThread(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            //Connect a Buffered Reader to the Input Stream of our socket
            try(BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
                //Connect a Print Writer to the Output Stream of our socket
                PrintWriter out = new PrintWriter(socket.getOutputStream())){
                
                //Verbose data About Client
                SocketAddress clientAddr = socket.getRemoteSocketAddress();
                System.out.println("Client<"+ clientAddr + "> has established a connection");

                //Read the File Location from the Client
                String location = in.readLine();
                String line;
                
                //Verbose output about File
                System.out.println("Trying to access file at: [" + location + "]");
                File file = new File(location);
                
                //If file was not found ask 3 times for a valid Location.
                for(int i = 0; i < 3 && !file.exists(); i++ ){
                    out.println("File was not found, please try again: ");
                    file = new File(in.readLine());
                }
                //Use the mock file if all else fails
                if (!file.exists())file = new File(FILENAME);
                
                //Connect a Buffered Reader to our File
                try(BufferedReader inFile= new BufferedReader(new FileReader(file))) {
                    while((line = inFile.readLine()) != null)  {
                        System.out.println("Client<" + clientAddr + "> receives: " + line);
                        out.println(line);
                    }                  
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
            catch(IOException e){
                System.err.println(e);
            }
            finally {
                System.out.println("Connection to Client was Terminated");
            }
        }
    }
    
    private class TimeThread implements Runnable{
        private Socket socket;

        public TimeThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            //Connect a Print Writer to the Output Stream of our socket
            try(PrintWriter out = new PrintWriter(socket.getOutputStream())){
                
                //Verbose data About Client
                SocketAddress clientAddr = socket.getRemoteSocketAddress();
                System.out.println("Client<"+ clientAddr + "> has established a connection");
                //Get current time as a Date object    
                Date now = new Date();
                //Now parse it to unix time
                Long nowLong = now.getTime();
                //Send it to the client
                out.print(nowLong);
            }
            catch(IOException e){
                System.err.println(e);
            }
        }
        
    }

    public static void main(String[] args) {
        //Check for flags
        if (args[0].equals("-f") || args[0].equals("-t")|| args[0].equals("-e")){
            String flag = args[0];
            int port = Integer.parseInt(args[1]);
            int backlog = 50;
            if (args [2] != null ){
                backlog = Integer.parseInt(args[2]);
            }
            new TCPServerFramework(flag, port, backlog);
        }
        //Else run the "Hello-message" Server
        else {
            int port = Integer.parseInt(args[0]);
            int backlog = 50;
            if (args [1] != null ){
                backlog = Integer.parseInt(args[1]);
            }
            new TCPServerFramework(port, backlog);
        }

    }



}
