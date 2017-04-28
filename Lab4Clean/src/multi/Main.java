package multi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		int linkLimit = 2000;
		URL startPage = new URL("http://cs.lth.se/pierre/");
		int nbrThreads = 10;
		
		crawl(linkLimit, startPage, nbrThreads);
	}

	private static void crawl(int linkLimit, URL startPage, int nbrThreads) throws InterruptedException {
		long startTime = System.currentTimeMillis();
		WorkMonitor workMonitor = new WorkMonitor(linkLimit);
		ExecutorService service = Executors.newFixedThreadPool(nbrThreads);
		service.submit(new ProcessTask(workMonitor, startPage, service));
		workMonitor.printInfo();
		service.shutdownNow();
		double timeInMin = (System.currentTimeMillis() - startTime) / (1000.0 * 60);
		System.out.println("Took " + timeInMin + " min with " + nbrThreads + " threads.");
	}

}
