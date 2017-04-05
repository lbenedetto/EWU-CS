import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args) throws IOException {
		String hostName = "10.106.60.246";
		int portNumber = 6969;
		try (
				Socket socket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
		) {
			System.out.println("Chat connected");
			String received;
			String sent;
			do {
				//Send
				System.out.print("You: ");
				sent = stdIn.readLine();
				out.println(sent);
				if (sent.contains("/dropmic")) break;
				//Receive
				received = in.readLine();
				if(received==null)break;
				System.out.println("Them: " + received);
			} while (!received.contains("/dropmic"));
			System.out.println("Chat disconnected gracefully");
		}
	}
}