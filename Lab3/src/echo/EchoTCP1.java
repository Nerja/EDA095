package echo;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoTCP1 {

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(30000)) {
			while (true)
				serveClient(serverSocket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void serveClient(ServerSocket serverSocket) {
		try (Socket s = serverSocket.accept();
				InputStream is = s.getInputStream();
				OutputStream os = s.getOutputStream()) {
			InetAddress clientAddress = s.getInetAddress();
			System.out.println("Client: " + clientAddress + " connected!");
			int data;
			while((data = is.read()) != -1) {
				os.write(data);
				System.out.print((char)data);
			}
		} catch (IOException e) {

		}
	}

}
