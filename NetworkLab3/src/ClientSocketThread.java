import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketThread extends Thread {
	private Monitor serverSender;
	private Socket socket;
	
	public ClientSocketThread(Monitor server, Socket client) {
		this.serverSender = server;
		this.socket = client;
	}
	
	@Override
	public void run() {
		boolean isConnected = false;
		InputStream is = null;
		
		try {
			is = socket.getInputStream();
			isConnected = true;
		} catch (IOException e1) {
			
		}
		
		while(isConnected) {
			byte[] buffer = new byte[1024*16];
			try {
				int length = is.read(buffer);
				serverSender.sendToAll(buffer, length, socket);
			}
			catch (Exception e) {
				isConnected = false;
			}
		}
		
	}
}
