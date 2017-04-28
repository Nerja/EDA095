package multi;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WorkMonitor {
	private Set<String> parsedLinks, parsedEmails, visitedLinks;
	private int linkLimit;
	private boolean printInfo;
	
	public WorkMonitor(int linkLimit) {
		this.linkLimit = linkLimit;
		parsedLinks = new HashSet<String>();
		parsedEmails = new HashSet<String>();
		visitedLinks = new HashSet<String>();
	}
	
	public synchronized List<String> addParsedInfo(String urlVisited, List<String> links, List<String> emails) {
		List<String> linksToVisit = new LinkedList<String>();
		parsedEmails.addAll(emails);
		for(String link : links)
			if(parsedLinks.add(link))
				linksToVisit.add(link);
		visitedLinks.add(urlVisited);
		if(visitedLinks.size() % 100 == 0 || !moreWork()) {
			printInfo = true;
			notifyAll();
		}
		return linksToVisit;
	}
	
	public synchronized boolean moreWork() {
		return visitedLinks.size() < linkLimit;
	}

	public synchronized void printInfo() throws InterruptedException {
		while(moreWork()) {
			wait();
			if(printInfo) {
				System.out.println(visitedLinks.size());
				printInfo = false;
			}
		}
		System.out.println("Visited " + visitedLinks.size() + " pages and collected " + parsedLinks.size() + " links and "
				+ parsedEmails.size() + " emails");
		System.out.println("Links:");
		parsedLinks.forEach(e -> System.out.println(e.toString()));
		System.out.println("**************************************");
		System.out.println("Emails:");
		parsedEmails.forEach(e -> System.out.println(e.toString()));
		System.out.println("Visited " + visitedLinks.size() + " pages and collected " + parsedLinks.size() + " links and "
				+ parsedEmails.size() + " emails");
	}
	
	
}
