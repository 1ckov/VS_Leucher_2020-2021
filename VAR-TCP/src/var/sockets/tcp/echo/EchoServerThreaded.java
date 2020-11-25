package var.sockets.tcp.echo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		new EchoServerThreaded(port).start();
	}
}
