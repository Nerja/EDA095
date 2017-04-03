package server;

public class MessageBroadcaster extends Thread {
	private Monitor monitor;
	
	public MessageBroadcaster(Monitor monitor) {
		this.monitor = monitor;
	}
	
	@Override
	public void run() {
		try{
			while(!isInterrupted()) {
				String message = monitor.fetchMessage();
				for(Client cli : monitor.getClients())
					cli.sendMessage(message);
			}
		} catch (InterruptedException e) {
			System.out.println("Broadcaster thread interrupted!");
		}
	}
}
