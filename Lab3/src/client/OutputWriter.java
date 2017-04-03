package client;

import java.io.PrintWriter;

public class OutputWriter extends Thread {
	private PrintWriter pw;
	private DataMonitor monitor;

	public OutputWriter(PrintWriter pw, DataMonitor monitor) {
		this.pw = pw;
		this.monitor = monitor;
	}

	@Override
	public void run() {
		try {
			while (!isInterrupted()) {
				pw.println(monitor.fetchOutgoing());
				if(pw.checkError())
					interrupt();
			}
		} catch (InterruptedException e) {

		}
		System.out.println("OutputWriter thread terminated!");
	}
}
