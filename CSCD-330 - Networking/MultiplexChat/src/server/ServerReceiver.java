package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

class ServerReceiver implements Runnable {

	private final Socket socket;
	private boolean skip;

	ServerReceiver(Socket s) {
		socket = s;
		skip = false;
	}

	@Override
	public void run() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String received;
			do {
				received = in.readLine();
				if (received == null) break;
				System.out.println(received);
				sendAll(received);
			} while (!received.contains("/dropmic"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendAll(String message) {
		for (ServerReceiver sr : Server.clients) {
			if (sr != this && !sr.skip)
				send(message, sr);
		}
	}

	private void send(String message, ServerReceiver sr) {
		try {
			PrintWriter out = new PrintWriter(sr.socket.getOutputStream(), true);
			out.println(message);
		} catch (SocketException e) {
			System.out.println("Client disconnected");
			sr.skip = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}