package clean;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

public class Database {
	
	private LinkedList<URL> urlQueue;
	
	private HashSet<String> emails;
	private HashSet<String> visited;
	
	private int limit; 
	
	public Database(URL url, int limit) {
		this.limit = limit;
				
		urlQueue = new LinkedList<URL>();
		urlQueue.add(url);
		
		emails = new HashSet<String>();
		visited = new HashSet<String>();
		
		
	}
	
	public synchronized URL pop(int threadNbr) throws InterruptedException {
		URL url = null;
		while (url == null && visited.size() < limit) {
			while(urlQueue.size() == 0 && visited.size() < limit) {
				wait();
			}
			url = urlQueue.pop();
			if(visited(url.toString())) {
				url = null;
			}
		}
		if(url != null) {
			visited.add(url.toString());
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

	public synchronized boolean visited(String string) {
		return visited.contains(string);
	}

	public synchronized void addEmail(URL u) {
		emails.add(u.toString());
	}


	public synchronized void printResults() throws InterruptedException {
		long t = System.currentTimeMillis();
		while(visited.size() < limit)
			wait();
		long elapsed = System.currentTimeMillis() - t;

		/*System.out.println("\nList of URLs:");
		for(String u : visited) {
			System.out.println(u);
		}
		
		System.out.println("List of emails:");
		for (URL u : emails) {
			System.out.println(u);
		}*/
		
		
		System.out.println("Time elapsed: " + elapsed + "ms");
		System.out.println("Amount of Links: " + visited.size());
		System.out.println("Amount of Emails: " + emails.size());
	
		try {
			save(visited, "links.txt");
			save(emails, "emails.txt");
		} catch (Exception e) {
			System.out.println("Save of links failed!");
		}
		
	}
	
	public static void save(Set<String> obj, String path) throws Exception {
	    PrintWriter pw = null;
	    try {
	        pw = new PrintWriter(
	            new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
	        for (Object s : obj) {
	            pw.println(s.toString());
	        }
	        pw.flush();
	    } finally {
	        pw.close();
	    }
	}
	

}
