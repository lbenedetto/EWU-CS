import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args) throws IOException {
		String hostName = "localhost";
		int portNumber = 6969;
		Socket socket = new Socket(hostName, portNumber);
		try {
			System.out.println("Chat connected");
			//Sender
			if (!socket.isClosed())
				new Thread(new Sender(socket)).start();
			//Receiver
			if (!socket.isClosed())
				new Thread(new Receiver(socket)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}