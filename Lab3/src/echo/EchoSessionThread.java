package echo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class EchoSessionThread extends Thread {
	private Socket socket;
	
	public EchoSessionThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try (InputStream is = socket.getInputStream(); OutputStream os = socket.getOutputStream()) {
			InetAddress clientAddress = socket.getInetAddress();
			System.out.println("Client: " + clientAddress + " connected!");
			int data;
			while((data = is.read()) != -1) {
				os.write(data);
				System.out.print((char)data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
