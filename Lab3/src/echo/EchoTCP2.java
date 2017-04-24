package echo;
import java.io.IOException;
import java.net.ServerSocket;

public class EchoTCP2 {

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(30000)) {
			while (true)
				(new EchoSessionThread(serverSocket.accept())).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
