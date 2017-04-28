import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		URL url = new URL("http://cs.lth.se/pierre_nugues/");
		Database database = new Database(url, 2000);
		long t = System.currentTimeMillis();
		for(int i = 0; i < 10; i++) {
			SpiderThread th = new SpiderThread(database);
			th.start();
		}
		database.printResults();
		System.out.println("Took " + (System.currentTimeMillis() - t) + "ms");	
	}

}
