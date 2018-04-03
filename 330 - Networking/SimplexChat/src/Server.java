import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int portNumber = 6969;
		try (
				ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
		) {
			System.out.println("Chat connected");
			String received;
			String sent;
			do {
				//Receive
				received = in.readLine();
				if(received==null)break;
				System.out.println("Them: " + received);
				if (received.contains("/dropmic")) break;
				//Send
				System.out.print("You: ");
				sent = stdIn.readLine();
				out.println(sent);
			} while (!sent.contains("/dropmic"));
		} catch (SocketException e) {
			System.out.println("Connection terminated unexpectedly");
		}
		main(args);
	}
}
