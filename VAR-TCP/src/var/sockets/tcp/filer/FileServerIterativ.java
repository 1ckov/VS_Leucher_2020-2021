
package var.sockets.tcp.filer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class FileServerIterativ {
	private static final String FILE = "/home/sa6o/Code/VS_Leucher_2020-2021/VAR-TCP/src/var/sockets/tcp/filer/message.txt";
	private int port;
	private int backlog;

	public FileServerIterativ(int port, int backlog) {
		this.port = port;
		this.backlog = backlog;
	}
	//Server begins to work
	public void start() {
		//Always open socket wuth try 
		try (ServerSocket serverSocket = new ServerSocket(port, backlog)) {
			//Verbose data
			System.out.println("FileServer (iterativ) auf " + serverSocket.getLocalSocketAddress() + " gestartet ...");
			//We load the file that has to get sent over the network
			File file = new File(FILE);
			//If file is not null
			if (file.exists()) {
				//File Path verbose
				System.out.println("\"" + file.getAbsolutePath() + "\" soll gesendet werden.");
				//Server accepting clients
				while (true) {
					handleClient(serverSocket);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void handleClient(ServerSocket server) {
		//Prepare the socketAdress we need for printing
		SocketAddress socketAddress = null;
		//This try has 3 Operations starting in it
		//First we accept the client on our welcome socket
		try (Socket socket = server.accept();
				//Now we load the file into our input stream
				BufferedReader in = new BufferedReader(new FileReader(FILE));
				//And we prepare our output stream going to the client
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
			//Here we get our clients ip adress and port for verbose purposes
			socketAddress = socket.getRemoteSocketAddress();
			String line;
			System.out.println("Verbindung zu  " + socketAddress + " aufgebaut");
			//Send file input 1 line at a time to server output stream 
			while((line=in.readLine()) != null){
				System.out.println(socketAddress + ": Bekommt die line: " + line);
				out.println(line);
				Thread.sleep(1000);
			}
		} 
		catch(InterruptedException e) {
			System.err.println(e);
		}
		catch (IOException e) {
			System.err.println(e);
		} finally {
			System.out.println("Verbindung zu  " + socketAddress + " abgebaut");
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		int backlog = 50;
		if (args.length == 2) {
			backlog = Integer.parseInt(args[1]);
		}

		new FileServerIterativ(port, backlog).start();
	}
}
