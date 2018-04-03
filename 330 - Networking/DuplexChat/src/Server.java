import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int portNumber = 6969;
		ServerSocket serverSocket = new ServerSocket(portNumber);
		Socket clientSocket = serverSocket.accept();
		try {
			System.out.println("Chat connected");
			//Receiver
			if (!clientSocket.isClosed())
				new Thread(new Receiver(clientSocket)).start();
			//Sender
			if (!clientSocket.isClosed())
				new Thread(new Sender(clientSocket)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
