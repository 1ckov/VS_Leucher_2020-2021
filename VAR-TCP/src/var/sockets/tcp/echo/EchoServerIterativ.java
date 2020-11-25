package var.sockets.tcp.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class EchoServerIterativ {
	private int port;
	private int backlog;

	public EchoServerIterativ(int port, int backlog) {
		this.port = port;
		this.backlog = backlog;
	}

	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(port, backlog)) {
			System.out.println("EchoServer (iterativ) auf " + serverSocket.getLocalSocketAddress() + " gestartet ...");
			while (true) {
				handleClient(serverSocket);
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void handleClient(ServerSocket server) {
		SocketAddress socketAddress = null;
		try (Socket socket = server.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
			socketAddress = socket.getRemoteSocketAddress();
			System.out.println("Verbindung zu " + socketAddress + " aufgebaut");
			out.println("Server ist bereit ...");
			String input;
			while ((input = in.readLine()) != null) {
				System.out.println(socketAddress + ">> [" + input + "]");
				out.println("echo: " + input);
			}
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			System.out.println("Verbindung zu " + socketAddress + " abgebaut");
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		int backlog = 50;
		if (args.length == 2) {
			backlog = Integer.parseInt(args[1]);
		}

		new EchoServerIterativ(port, backlog).start();
	}
}
