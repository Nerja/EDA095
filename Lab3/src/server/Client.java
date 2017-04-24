package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;

	public Client(Socket socket) throws IOException {
		this.socket = socket;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream());
	}

	public void sendMessage(String msg) {
		synchronized (pw) { // Only one write
			pw.println(msg.trim());
			pw.flush();
		}
	}

	public String readCommand() throws IOException {
		String msg = "";
		synchronized (br) {
			msg = br.readLine();
		}
		return msg;
	}

	public String getAddress() {
		return socket.getInetAddress().toString();
	}

	public void close() throws IOException {
		br.close();
		pw.close();
		socket.close();
	}
}
