package var.sockets.tcp.filer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServerThreaded {
	private static final String FILE = "src/var/sockets/tcp/filer/message.txt";
	private int port;

	public FileServerThreaded(int port) {
		this.port = port;
	}

	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("FileServer (threaded) auf " + serverSocket.getLocalSocketAddress() + " gestartet ...");
			File file = new File(FILE);
			if (file.exists()) {
				System.out.println("\"" + file.getAbsolutePath() + "\" soll gesendet werden.");
				while (true) {
					// hier müssen Verbindungswünsche von Clients in einem neuen
					// Thread angenommen werden
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
			// hier muss die Verbindung mit dem Client über this.socket
			// abgearbeitet werden
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		new FileServerThreaded(port).start();
	}
}
