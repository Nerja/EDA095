package mailbox;

public class Consumer extends Thread {
	private Mailbox mailbox;
	
	public Consumer(Mailbox mailbox) {
		this.mailbox = mailbox;
	}
	
	@Override
	public void run() {
		try {
			while(!isInterrupted()) 
				System.out.println(mailbox.fetch());
		} catch (InterruptedException e) {
			System.out.println("Consumer interrupted!");
		}
	}
}
