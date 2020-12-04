package var.sockets.tcp.time;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TimeLongServer {
	private int port;

	public TimeLongServer(int port) {
		this.port = port;
	}

	public void startServer() {
		//Open "Welcome Socket"
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			//Server start
			while (true) {
				//Accept new connections
				try (Socket socket = serverSocket.accept();
						//Open a Stream for writing to clients
						PrintWriter out = new PrintWriter(socket.getOutputStream())) {
				    //Get current date
					Date now = new Date();
					//Format current date to milis since 1.1.1970(UNIX Time) 
					long currentTime = now.getTime(); // Zeit von now in ms seit 01.01.1970 00:00:00 GMT abrufen
					//Send time to Clients
					out.print(currentTime);
					//Flush buffer to start anew
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
