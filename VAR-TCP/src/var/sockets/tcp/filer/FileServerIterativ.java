
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
	private static final String FILE = "src/var/sockets/tcp/filer/message.txt";
	private int port;
	private int backlog;

	public FileServerIterativ(int port, int backlog) {
		this.port = port;
		this.backlog = backlog;
	}

	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(port, backlog)) {
			System.out.println("FileServer (iterativ) auf " + serverSocket.getLocalSocketAddress() + " gestartet ...");
			File file = new File(FILE);
			if (file.exists()) {
				System.out.println("\"" + file.getAbsolutePath() + "\" soll gesendet werden.");
				while (true) {
					handleClient(serverSocket);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void handleClient(ServerSocket server) {
		SocketAddress socketAddress = null;
		try (Socket socket = server.accept();
				BufferedReader in = new BufferedReader(new FileReader(FILE));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
			socketAddress = socket.getRemoteSocketAddress();
			System.out.println("Verbindung zu  " + socketAddress + " aufgebaut");
			// Inhalt von in zeilenweise an out senden
		} catch (IOException e) {
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
