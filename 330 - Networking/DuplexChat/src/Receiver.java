import java.io.BufferedReader;
		import java.io.IOException;
		import java.io.InputStreamReader;
		import java.net.Socket;

class Receiver implements Runnable {

	private Socket socket;

	Receiver(Socket s) {
		socket = s;
	}

	@Override
	public void run() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String received;
			do {
				received = in.readLine();
				if (received == null) break;
				System.out.println("Them: " + received);
			} while (!received.contains("/dropmic"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}