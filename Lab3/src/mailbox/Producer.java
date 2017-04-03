package mailbox;

public class Producer extends Thread {

	private String name;
	private Mailbox mailbox;

	public Producer(String name, Mailbox mailbox) {
		this.name = name;
		this.mailbox = mailbox;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 5; i++) {
				mailbox.put(name);
				Thread.sleep((int) (10 * Math.random()));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
