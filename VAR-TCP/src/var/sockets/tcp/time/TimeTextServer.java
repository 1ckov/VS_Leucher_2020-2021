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
					//Format current date to the System locale  
					String currentTime = DateFormat.getInstance().format(now);// DateFormat Instanz holen und mit dessen format Methode now zum String machen
					//Send time to Clients
					out.print(currentTime);
					//Flush buffer so we can dont send old time stamps 
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
