package server;

import java.io.IOException;

public class ClientListener extends Thread {

	private Client client;
	private Monitor monitor;

	public ClientListener(Client client, Monitor monitor) {
		this.client = client;
		this.monitor = monitor;
	}

	@Override
	public void run() {
		String address = client.getAddress();
		try {
			System.out.println("Client: " + address + " connected!");
			while (!isInterrupted()) {
				String command = client.readCommand();
				char commandCode = command.charAt(0);
				switch (commandCode) {
				case 'M':
					handleMessageCommand(command);
					break;
				case 'E':
					handleEchoCommand(command);
					break;
				case 'Q':
					handleExitCommand();
					break;
				}
			}
		} catch (Exception e) {
			monitor.removeClient(client);
			try {
				client.close();
			} catch (IOException e1) {
			}
			interrupt();
		}
		System.out.println("Client: " + address + " diconnected!");
	}

	private void handleExitCommand() {
		monitor.removeClient(client);
		try {
			client.close();
		} catch (IOException e) {
		}
		interrupt();
	}

	private void handleEchoCommand(String command) {
		client.sendMessage(command.substring(2));
	}

	private void handleMessageCommand(String command) {
		monitor.putMessage(command.substring(2));
	}

}
