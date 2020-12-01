package var.sockets.tcp.time;

import java.io.InputStream;
import java.net.Socket;
import java.util.Date;

public class TimeClient {
	public static void main(String[] args) {
		//Open a connection to Time Server
		try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
				//Open a stream to read from time Server
				InputStream in = socket.getInputStream()) {
			//Initialize a String builder to Format our char array into a string		
			StringBuilder stringBuilder = new StringBuilder();
			//Initialise a temp buffer for our chars	
			int c;

			while ((c = in.read()) != -1) {
				stringBuilder.append((char) c);
			}

			Date now = null;
			String result = stringBuilder.toString();

			try {
				now = new Date();
			} catch (Exception e) {
				//TODO: handle exception
			}

			// stringBuilder-Inhalt in ein Date-Objekt konvertieren und ausgeben
			
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
