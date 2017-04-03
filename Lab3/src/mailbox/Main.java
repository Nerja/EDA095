package mailbox;

public class Main {

	public static void main(String[] args) {
		Mailbox mailbox = new Mailbox();
		
		for(int i = 1; i < 11; i++)
			(new Producer("Thread"+i, mailbox)).start();
		
		Consumer consumer = new Consumer(mailbox);
		consumer.start();
	}

}
