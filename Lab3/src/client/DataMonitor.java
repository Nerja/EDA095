package client;

import java.util.LinkedList;
import java.util.List;

public class DataMonitor {
	private List<String> outgoing;

	public DataMonitor() {
		outgoing = new LinkedList<String>();
	}

	public synchronized void putOutgoing(String msg) {
		outgoing.add(msg);
		notifyAll(); // Notify new out msg
	}

	public synchronized String fetchOutgoing() throws InterruptedException {
		while (outgoing.isEmpty()) // Wait for msg to send
			wait();
		return outgoing.remove(0);
	}
}
