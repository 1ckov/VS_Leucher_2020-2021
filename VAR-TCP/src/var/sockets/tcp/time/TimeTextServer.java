package var.sockets.tcp.time;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;

public class TimeTextServer {
	private int port;

	public TimeTextServer(int port) {
		this.port = port;
	}

	public void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				try (Socket socket = serverSocket.accept();
						PrintWriter out = new PrintWriter(socket.getOutputStream())) {
					Date now = new Date();
					String currentTime = // DateFormat Instanz holen und mit dessen format Methode now zum String machen
					out.print(currentTime);
					out.flush();
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {
		new TimeTextServer(Integer.parseInt(args[0])).startServer();
	}
}
