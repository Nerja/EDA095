import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Database {

	HashSet<URL> visited;
	LinkedList<URL> urlQueue;
	
	HashSet<URL> emails;
	HashSet<URL> links;
	
	int limit; 
	int count = 0;
	
	public Database(URL url, int l) {
		urlQueue = new LinkedList<URL>();
		this.add(url);
		
		emails = new HashSet<URL>();
		links = new HashSet<URL>();
		
		limit = l;
		
		visited = new HashSet<URL>();
		count = 0;
		
	}
	
	public synchronized URL pop() throws InterruptedException {
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
			System.out.println("Crawled " + visited.size() + "/" + limit);
		}
		return url;
	}
	
	synchronized void add(URL url) {
		urlQueue.add(url);
	}

	public synchronized boolean visited(URL href) {
		return visited.contains(href);
	}

	public synchronized void addEmail(URL u) {
		emails.add(u);
	}


	public synchronized void printResults() throws InterruptedException {
		while(visited.size() < limit)
			wait();
		System.out.println("List of addresses:");
		for (URL u : emails) {
			System.out.println(u);
		}
		System.out.println("\nList of URLs:");
		for(URL u : links) {
			System.out.println(u);
		}
		System.out.println("");	
	}
	

}
