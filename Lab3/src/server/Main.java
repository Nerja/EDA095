package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	public static void main(String[] args) {
		try(ServerSocket serverSocket = new ServerSocket(30000)){
			Monitor monitor = new Monitor();
			MessageBroadcaster mb = new MessageBroadcaster(monitor);
			mb.start();
			while(true)
				try{
					acceptConnection(serverSocket, monitor);
				} catch (IOException e) {
					e.printStackTrace();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void acceptConnection(ServerSocket serverSocket, Monitor monitor) throws IOException {
		Socket socket = serverSocket.accept();
		Client client = new Client(socket);
		monitor.addClient(client);
		ClientListener clientListener = new ClientListener(client, monitor);
		clientListener.start();
	}
}
