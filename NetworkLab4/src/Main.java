import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws IOException {
		URL url = new URL("http://cs.lth.se/pierre_nugues/");
		Database database = new Database(url, 1000);
		long t = System.currentTimeMillis();
		for(int i = 0; i < 1; i++) {
			SpiderThread th = new SpiderThread(database);
			th.start();
		}
		while(!database.finished()) {
			
		}
		database.printResults();
		System.out.println("Took " + (System.currentTimeMillis() - t) + "ms");	
	}

}
