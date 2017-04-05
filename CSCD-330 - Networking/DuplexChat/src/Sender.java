import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Sender implements Runnable {
	private Socket socket;

	Sender(Socket s) {
		socket = s;
	}

	@Override
	public void run() {
		try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			 BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
			String sent;
			do {
				//System.out.print("You: ");
				sent = stdIn.readLine();
				if (sent == null) break;
				out.println(sent);
			} while (!sent.contains("/dropmic"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}