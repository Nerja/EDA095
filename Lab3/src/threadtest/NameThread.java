package threadtest;

public class NameThread extends Thread {

	private String name;
	
	public NameThread(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 5; i++) {
			System.out.println(name);
			try {
				Thread.sleep((int)(10*Math.random()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
