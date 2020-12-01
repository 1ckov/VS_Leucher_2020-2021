package var.sockets.tcp.filer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileReader;
import java.io.PrintWriter;

public class FileServerThreaded {
	private static final String FILE = "/home/sa6o/Code/VS_Leucher_2020-2021/VAR-TCP/src/var/sockets/tcp/filer/message.txt";
	private int port;

	public FileServerThreaded(int port) {
		this.port = port;
	}

	public void start() {
		//Opening up the welcome port
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			//Verbose 
			System.out.println("FileServer (threaded) auf " + serverSocket.getLocalSocketAddress() + " gestartet ...");
			//Loading file into buffer
			File file = new File(FILE);
			//Check if file is emty
			if (file.exists()) {
				//More verbose
				System.out.println("\"" + file.getAbsolutePath() + "\" soll gesendet werden.");
				//Server working
				while (true) {
					//Start new Thread to handle client
					new FileThread(serverSocket.accept()).start();

				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private class FileThread extends Thread {
		private Socket socket;

		public FileThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			//Verbose
			System.out.println("Verbindung mit " + socket.getRemoteSocketAddress() + "Aufgebaut." );
			//Get file again 
			File file = new File(FILE);
			//Check if file still exists
			if (file.exists()){
				//Open Stream for reading file
				try(BufferedReader in = new BufferedReader(new FileReader(file));
				//Open Stream for writing file
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
					//Verbose
					System.out.println("File: " + file.getCanonicalPath() + " wird an " + socket.getRemoteSocketAddress() + " gesendet" );
					//Create String to hold our current line
					String line;
					while((line = in.readLine()) != null) {
						//Verbose
						System.out.println("Zeile die gerade gelesen wird und an Client<" + socket.getRemoteSocketAddress() + "> :\n" + line);
						//Send line to Client
						out.println(line);
						//Testing purposes
						Thread.sleep(1000);

					}

				} 
				catch (IOException e){
					System.err.println(e);
				}
				catch (InterruptedException e) {
					System.err.println(e);
				}
			}
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		new FileServerThreaded(port).start();
	}
}
