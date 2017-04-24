package server;

import java.util.LinkedList;
import java.util.List;

public class Monitor {
	private List<String> messages;
	private List<Client> clients;
	
	public Monitor() {
		messages = new LinkedList<String>();
		clients = new LinkedList<Client>();
	}
	
	public synchronized String fetchMessage() throws InterruptedException {
		while(messages.isEmpty())
			wait();
		return messages.remove(0);
	}
	
	public synchronized void putMessage(String message) {
		messages.add(message);
		notifyAll(); //Messages here!
	}
	
	public synchronized void addClient(Client client) {
		clients.add(client);
	}

	public synchronized List<Client> getClients() {
		return new LinkedList<Client>(clients);
	}

	public synchronized void removeClient(Client client) {
		clients.remove(client);
	}
}
