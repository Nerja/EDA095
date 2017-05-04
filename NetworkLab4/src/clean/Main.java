package clean;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		URL url = new URL("http://cs.lth.se/pierre_nugues/");
		Database database = new Database(url, 2000);
		
		for(int i = 0; i < 10; i++) {
			SpiderThread th = new SpiderThread(database, (i+1));
			System.out.println(th.getNbr());
			th.start();
		}
		database.printResults();
	}

}
