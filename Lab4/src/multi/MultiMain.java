package multi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class MultiMain {
	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		long startTime = System.currentTimeMillis();
		int linkLimit = 0;
		String homepage = "";
		int numberThreads = 0;
		try {
			// linkLimit = Integer.parseInt(args[1]);
			// homepage = args[0];
			// numberThreads = Integer.parseInt(args[2]);
			linkLimit = 2000;
			homepage = "http://cs.lth.se/pierre/";
			numberThreads = 10;
		} catch (Exception e) {
			System.err.println("Should give homepage and link limit as arg");
			System.exit(1);
		}
		crawl(linkLimit, homepage, numberThreads);
		double duration = (System.currentTimeMillis() - startTime) / (1000 * 60);
		System.out.println("Took: " + duration + " min");
	}

	private static void crawl(int linkLimit, String homepage, int numberThreads) throws InterruptedException, MalformedURLException {
		URL homepageUrl = new URL(homepage);
		WorkMaster master = new WorkMaster(linkLimit, homepageUrl);
		Thread[] threads = new Thread[numberThreads];
		for(int i = 0; i < numberThreads; i++) {
			threads[i] = new Processor(master);
			threads[i].start();
		}
		master.waitForCompletion();
		master.printParsedInfo();
		for(Thread t : threads)
			t.interrupt();
	}
}
