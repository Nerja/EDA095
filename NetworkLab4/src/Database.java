import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Database {

	HashSet<String> visited;
	LinkedList<URL> urlQueue;
	
	HashSet<URL> emails;
	LinkedList<URL> links;
	
	int limit; 
	int count = 0;
	
	public Database(URL url, int l) {
		urlQueue = new LinkedList<URL>();
		this.add(url);
		
		emails = new HashSet<URL>();
		links = new LinkedList<URL>();
		
		limit = l;
		
		visited = new HashSet<String>();
		count = 0;
		
	}
	
	synchronized URL pop() throws NoSuchElementException {
		if(urlQueue.size() > 0 && count < limit) {
			URL url = urlQueue.pop();
			links.add(url);
			visited.add(url.toString());
			
			count++;
			System.out.println("Crawled " + count + "/" + limit);
			return url;
		}
		throw new NoSuchElementException();
	}
	
	synchronized void add(URL url) {
		urlQueue.add(url);
	}

	public synchronized boolean visited(String href) {
		return visited.contains(href);
	}

	public synchronized void addEmail(URL u) {
		emails.add(u);
	}

	public synchronized boolean finished() {
		return count >= limit - 1;
	}

	public void printResults() {
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
