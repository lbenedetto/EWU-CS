package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
	public static void main(String[] args) throws IOException {
		String hostName = "localhost";
		int portNumber = 6969;
		Socket socket = new Socket(hostName, portNumber);
		try {
			System.out.print("Enter username: ");
			Scanner kb = new Scanner(System.in);
			String name = kb.nextLine();
			//Sender
			Sender sender = new Sender(socket, name);
			new Thread(sender).start();
			//Receiver
			Receiver receiver = new Receiver(socket);
			new Thread(receiver).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}