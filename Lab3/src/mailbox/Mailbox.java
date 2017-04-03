package mailbox;
public class Mailbox {
	private String message;
	
	public synchronized void put(String msg) throws InterruptedException {
		while(message != null)
			wait();
		message = msg;
		notifyAll(); //Message in mailbox
	}
	
	public synchronized String fetch() throws InterruptedException {
		while(message == null)
			wait();
		String msg = message;
		message = null;
		notifyAll(); //Notify free slot
		return msg;
	}
}
