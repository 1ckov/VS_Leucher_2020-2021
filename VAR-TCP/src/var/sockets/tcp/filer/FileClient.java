package var.sockets.tcp.filer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class FileClient {
	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		try (Socket socket = new Socket(host, port);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
