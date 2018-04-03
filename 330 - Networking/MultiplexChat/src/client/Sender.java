package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Sender implements Runnable {
	private PrintWriter out;
	private final BufferedReader stdIn;
	private final String name;

	Sender(Socket socket, String n) {
		name = n;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("Failed to open output stream");
			e.printStackTrace();
		}
		stdIn = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	public void run() {
		try {
			String message;
			do {
				message = stdIn.readLine();
				if (message == null) break;
				out.println(name + ": " + message);
			} while (!message.contains("/dropmic"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}