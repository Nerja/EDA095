import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Monitor {
	private HashSet<Socket> clients;
	private HashMap<Socket, List<String>> messages;
	
	public Monitor() {
		clients = new HashSet<Socket>();
		messages = new HashMap<Socket, List<String>>();
	}

	public synchronized void addClient(Socket client) {
		clients.add(client);
	}
	
	
	public synchronized void sendToAll(byte[] buffer, int length, Socket sender) {
		for (Socket client : clients) {
			if (client != sender) {
				try {
					OutputStream os = client.getOutputStream();
					os.write(buffer, 0, length);
					os.flush();
				} catch (Exception e) {
					clients.remove(client);
				}
			}
		}
	}

}
