package var.sockets.tcp.time;

import java.io.InputStream;
import java.net.Socket;

public class TimeClient {
	public static void main(String[] args) {
		try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
				InputStream in = socket.getInputStream()) {
			StringBuilder stringBuilder = new StringBuilder();

			int c;
			while ((c = in.read()) != -1) {
				stringBuilder.append((char) c);
			}

			// stringBuilder-Inhalt in ein Date-Objekt konvertieren und ausgeben
			
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
