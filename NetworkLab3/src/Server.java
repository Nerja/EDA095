import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
	private ServerSocket socket;
	private boolean isOn;
	private Monitor serverSender;
	
	public Server(int port) {
		try {
			socket = new ServerSocket(port);
			isOn = true;
			serverSender = new Monitor();
		} catch (IOException e) {
			System.out.println("Could not bind to port " + port);
			System.out.println("Please try again..");
		}
	}
	
	@Override
	public void run() {
		while(isOn) {
			try {
				Socket client = socket.accept();
				
				System.out.println("Accepted connection from " + client.getInetAddress()+":"+client.getPort());
				
				ClientSocketThread clientThread = new ClientSocketThread(serverSender, client);
				clientThread.start();
				
				serverSender.addClient(client);
				
			} catch (IOException e) {
				isOn = false;
			}
		}
	}
	
	

}
