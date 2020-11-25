package var.sockets.tcp.time;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;

public class TimeLongServer {
	private int port;

	public TimeLongServer(int port) {
		this.port = port;
	}

	public void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				try (Socket socket = serverSocket.accept();
						PrintWriter out = new PrintWriter(socket.getOutputStream())) {
					Date now = new Date();
					long currentTime = // Zeit von now in ms seit 01.01.1970 00:00:00 GMT abrufen
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
		new TimeLongServer(Integer.parseInt(args[0])).startServer();
	}
}
