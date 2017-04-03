package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		InetAddress ip = null;
		int port = 0;
		try {
			// ip = InetAddress.getByName(args[0]);
			// port = Integer.parseInt(args[1]);
			ip = InetAddress.getByName("127.0.0.1");
			port = 30000;
		} catch (Exception e) {
			System.err.println("Please enter ip and port");
			System.exit(1);
		}
		connect(ip, port);
	}

	private static void connect(InetAddress ip, int port) {
		try (Socket socket = new Socket(ip, port);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw = new PrintWriter(socket.getOutputStream())) {
			DataMonitor monitor = new DataMonitor();
			OutputWriter outputThread = new OutputWriter(pw, monitor);
			outputThread.start();
			Gui gui = new Gui(monitor);
			InputReader inputThread = new InputReader(br, gui);
			inputThread.start();
			try {
				inputThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			outputThread.interrupt(); // Kill output thread
			System.out.println("Fun times!");
			gui.setVisible(false);
			gui.dispose();
		} catch (IOException e) {
			System.err.println("Network problem!");
			System.exit(1);
		}
	}
}
