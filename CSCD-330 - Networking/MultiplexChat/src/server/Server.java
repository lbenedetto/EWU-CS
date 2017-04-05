package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

class Server {
	static ArrayList<ServerReceiver> clients;

	public static void main(String[] args) throws IOException {
		int portNumber = 6969;
		ServerSocket serverSocket = new ServerSocket(portNumber);
		clients = new ArrayList<>();
		while (true) {
			addClient(serverSocket);
		}
	}

	private static void addClient(ServerSocket serverSocket) {
		try {
			Socket clientSocket = serverSocket.accept();
			System.out.println("Chat connected");
			//Receiver
			ServerReceiver receiver = new ServerReceiver(clientSocket);
			new Thread(receiver).start();
			clients.add(receiver);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
