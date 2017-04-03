package client;

import java.io.BufferedReader;
import java.io.IOException;

public class InputReader extends Thread {
	private BufferedReader br;
	private Gui gui;

	public InputReader(BufferedReader br, Gui gui) {
		this.br = br;
		this.gui = gui;
	}

	@Override
	public void run() {
		try {
			while (!isInterrupted()) {
				String msg = br.readLine();
				if (msg != null)
					gui.appendMessage(msg);
				else
					interrupt();
			}
		} catch (IOException e) {

		}
		System.out.println("InputReader terminated!");
	}
}
