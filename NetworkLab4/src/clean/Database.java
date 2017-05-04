package clean;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Database {
	
	private LinkedList<URL> urlQueue;
	
	private HashSet<URL> emails;
	private HashSet<URL> visited;
	
	private int limit; 
	
	public Database(URL url, int limit) {
		this.limit = limit;
				
		urlQueue = new LinkedList<URL>();
		urlQueue.add(url);
		
		emails = new HashSet<URL>();
		visited = new HashSet<URL>();
		
		
	}
	
	public synchronized URL pop(int threadNbr) throws InterruptedException {
		URL url = null;
		while (url == null && visited.size() < limit) {
			while(urlQueue.size() == 0 && visited.size() < limit) {
				wait();
			}
			url = urlQueue.pop();
			if(visited.contains(url)) {
				url = null;
			}
		}
		if(url != null) {
			visited.add(url);
			notifyAll();
			System.out.println("Crawled " + visited.size() + "/" + limit + " by thread " + threadNbr);
		}
		return url;
	}
	
	public synchronized boolean keepCrawling() {
		return visited.size() < limit;
	}
	
	public synchronized void add(URL url) {
		urlQueue.add(url);
		notifyAll();
	}

	public synchronized boolean visited(URL href) {
		return visited.contains(href);
	}

	public synchronized void addEmail(URL u) {
		emails.add(u);
	}


	public synchronized void printResults() throws InterruptedException {
		long t = System.currentTimeMillis();
		while(visited.size() < limit)
			wait();
		long elapsed = System.currentTimeMillis() - t;

		System.out.println("\nList of URLs:");
		for(URL u : visited) {
			System.out.println(u);
		}
		
		System.out.println("List of emails:");
		for (URL u : emails) {
			System.out.println(u);
		}
		System.out.println("");	
		System.out.println("Time elapsed: " + elapsed + "ms");
	}
	

}
