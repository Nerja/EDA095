package mono;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MonoSearch {

	public static void crawl(String startPage, int linkLimit) throws MalformedURLException {
		// Queue data
		List<URL> urlQueue = new LinkedList<URL>();
		urlQueue.add(new URL(startPage));
		Set<String> visitedUrls = new HashSet<String>();

		// Parsed information
		Set<String> parsedLinks = new HashSet<String>();
		parsedLinks.add(startPage);
		Set<String> parsedEmails = new HashSet<String>();

		while (visitedUrls.size() < linkLimit && !urlQueue.isEmpty())
			visitUrl(urlQueue, visitedUrls, parsedLinks, parsedEmails);

		printParsedInformation(linkLimit, parsedLinks, parsedEmails, visitedUrls);
	}

	private static void visitUrl(List<URL> urlQueue, Set<String> visitedUrls, Set<String> parsedLinks,
			Set<String> parsedEmails) {
		URL urlToVisit = urlQueue.remove(0);
		try {
			URLConnection conn = urlToVisit.openConnection();
			conn.setConnectTimeout(5000); // yolo
			conn.setReadTimeout(5000);
			String contentType = conn.getContentType();
			if (contentType != null && contentType.toLowerCase().contains("text/html")) {
				String contentEncoding = conn.getContentEncoding();
				contentEncoding = contentEncoding != null ? contentEncoding : "UTF-8";
				InputStream is = conn.getInputStream();
				Document document = Jsoup.parse(conn.getInputStream(), contentEncoding, urlToVisit.getHost());
				is.close();
				parsePage(urlToVisit, document, urlQueue, parsedLinks, parsedEmails);
				visitedUrls.add(urlToVisit.toString());
				System.out.println(visitedUrls.size());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Can not load " + urlToVisit.toString());
		} catch (IOException e) {
			System.out.println("Visiting " + urlToVisit.toString() + " : " + e.getMessage());
		}
	}

	private static void parsePage(URL url, Document document, List<URL> urlQueue, Set<String> parsedLinks,
			Set<String> parsedEmails) throws MalformedURLException {
		Element base = document.getElementsByTag("base").first();
		if (base != null)
			url = new URL(base.attr("href"));
		Elements elementsToParse = document.getElementsByTag("a");
		elementsToParse.addAll(document.getElementsByTag("frame"));
		for (Element el : elementsToParse)
			parseElement(url, el, urlQueue, parsedLinks, parsedEmails);
	}

	private static void parseElement(URL base, Element el, List<URL> urlQueue, Set<String> parsedLinks,
			Set<String> parsedEmails) throws MalformedURLException {
		String linkString = el.tagName().equals("a") ? el.attr("href") : el.attr("src");
		if (linkString.startsWith("mailto:"))
			parsedEmails.add(linkString.substring(7));
		else {
			try {
				URL newURL = new URL(base, linkString);
				if (parsedLinks.add(newURL.toString()))
					urlQueue.add(newURL);
			} catch (IOException e) {
			}
		}
	}

	private static void printParsedInformation(int linkLimit, Set<String> parsedLinks, Set<String> parsedEmails, Set<String> visitedUrls) {
		System.out.println("Visited " + visitedUrls.size() + " pages and collected " + parsedLinks.size() + " links and "
				+ parsedEmails.size() + " emails");
		System.out.println("Links:");
		parsedLinks.forEach(e -> System.out.println(e.toString()));
		System.out.println("**************************************");
		System.out.println("Emails:");
		parsedEmails.forEach(e -> System.out.println(e.toString()));
		System.out.println("Visited " + visitedUrls.size() + " pages and collected " + parsedLinks.size() + " links and "
				+ parsedEmails.size() + " emails");
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		int linkLimit = 2000;
		String startPage = "http://cs.lth.se/pierre/";
		try {
			crawl(startPage, linkLimit);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage() + " is not a valid url");
		}
		long time = System.currentTimeMillis() - startTime;
		double timeInMin = time / (1000.0 * 60);
		System.out.println("Min: " + timeInMin);
	}

}
