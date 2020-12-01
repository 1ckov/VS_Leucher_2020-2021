package var.sockets.tcp.echo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketAddress;


public class EchoServerThreaded {
	private int port;

	public EchoServerThreaded(int port) {
		this.port = port;
	}

	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("EchoServer (threaded) auf " + serverSocket.getLocalSocketAddress() + " gestartet ...");
			// hier müssen Verbindungswünsche von Clients in einem neuen Thread
			// angenommen werden
			while(true){
				new EchoThread(serverSocket.accept()).start();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private class EchoThread extends Thread {
		private Socket socket;

		public EchoThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			// hier muss die Verbindung mit dem Client über this.socket
			// abgearbeitet werden
			SocketAddress socketAddress = null;
			try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
				socketAddress = socket.getRemoteSocketAddress();
				System.out.println("Verbindung zu " + socketAddress + " aufgebaut");
				out.println("Server ist bereit ...");
				String input;
			while ((input = in.readLine()) != null) {
				System.out.println(socketAddress + ">> [" + input + "]");
				out.println("echo: " + input);
			}
			} catch (IOException e) {
				System.err.println(e);
			} finally {
				System.out.println("Verbindung zu " + socketAddress + " abgebaut");
			}
			
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		new EchoServerThreaded(port).start();
	}
}
