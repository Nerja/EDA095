package threadtest;
public class ThreadTest {

	public static void main(String[] args) {
		for(int i = 0; i < 10; i++) {
			Thread t = new NameThread("Thread"+i);
			t.start();
		}
	}

}
