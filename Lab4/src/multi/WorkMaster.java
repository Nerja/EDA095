package multi;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WorkMaster {
	// Stored information(parsed links and emails)
	private List<String> parsedLinks;
	private List<String> parsedEmails;
	private Set<URL> visitedLinks; // Links visited
	private List<URL> linksQueue;
	private int linkLimit, lastUpdate;

	public WorkMaster(int linkLimit, URL homepage) {
		parsedLinks = new LinkedList<>();
		parsedEmails = new LinkedList<>();
		visitedLinks = new HashSet<URL>();
		linksQueue = new LinkedList<>();
		linksQueue.add(homepage);
		this.linkLimit = linkLimit;
		lastUpdate = 0;
	}

	public synchronized void addParsedInfo(URL base, List<URL> links, List<String> emails)
			throws MalformedURLException {
		parsedEmails.addAll(emails);
		for(URL url : links) {
			parsedLinks.add(url.toString());
			if (!visitedLinks.contains(url))
				linksQueue.add(url);
		}
		notifyAll(); // Notify new work
	}

	public synchronized URL fetchWork() throws InterruptedException {
		URL urlToVisit = null;
		while (urlToVisit == null && visitedLinks.size() < linkLimit) {
			while (linksQueue.isEmpty() && visitedLinks.size() < linkLimit)
				wait();
			urlToVisit = linksQueue.remove(0);
			if (visitedLinks.contains(urlToVisit))
				urlToVisit = null;
		}
		if (urlToVisit != null) {
			visitedLinks.add(urlToVisit);
			return urlToVisit;
		}
		return null;
	}

	public synchronized void printParsedInfo() {
		try (PrintWriter pw = new PrintWriter(new FileOutputStream("result"))) {
			pw.println("Parsed links: ");
			parsedLinks.forEach(pw::println);
			pw.println();
			pw.println("\n");
			pw.println("Parsed emails: ");
			parsedEmails.forEach(pw::println);
		} catch (Exception e) {
			System.out.println("Cant save result, gg");
		}
	}

	public synchronized void waitForCompletion() throws InterruptedException {
		while (visitedLinks.size() < linkLimit) {
			wait();
			if (visitedLinks.size() > lastUpdate + 100) {
				System.out.println(visitedLinks.size());
				lastUpdate = visitedLinks.size();
			}
		}
	}
}
