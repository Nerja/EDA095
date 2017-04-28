package mono;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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

public class Crawler {

	public static void main(String[] args) throws MalformedURLException {
		int linkLimit = 0;
		String homepage = "";
		try {
			// linkLimit = Integer.parseInt(args[1]);
			// homepage = args[0];
			linkLimit = 2000;
			homepage = "http://cs.lth.se/pierre/";
		} catch (Exception e) {
			System.err.println("Should give homepage and link limit as arg");
			System.exit(1);
		}
		crawl(linkLimit, homepage);
	}

	private static void crawl(int linkLimit, String homepage) throws MalformedURLException {
		long crawlStartTime = System.currentTimeMillis();
		// Stored information(parsed links and emails)
		List<String> parsedLinks = new LinkedList<>();
		List<String> parsedEmails = new LinkedList<>();

		Set<URL> visitedLinks = new HashSet<URL>(); // Links visited
		List<URL> linksQueue = new LinkedList<>(); // Queue of links to be
													// visited
		linksQueue.add(new URL(homepage)); // Queue given homepage

		while (!linksQueue.isEmpty() && visitedLinks.size() < linkLimit) {
			URL link = linksQueue.remove(0); // Fetch link 0
			if (visitedLinks.add(link)) { // Check if already visited
				try {
					parseLink(link, linksQueue, parsedLinks, parsedEmails, visitedLinks);
				} catch (FileNotFoundException e) {
					System.out.println("Could not load " + link.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		}

		System.out.println("List of URLs(" + parsedLinks.size() + "):");
		parsedLinks.forEach(System.out::println);
		System.out.println("\n\nList of addresses(" + parsedEmails.size() + "):");
		parsedEmails.forEach(System.out::println);
		System.out.println((System.currentTimeMillis() - crawlStartTime) / (1000.0 * 60));
	}

	private static void parseLink(URL link, List<URL> linksQueue, List<String> parsedLinks, List<String> parsedEmails,
			Set<URL> visitedLinks) throws IOException, URISyntaxException {
		URLConnection conn = link.openConnection();
		String contentType = conn.getContentType();
		if (contentType != null && contentType.contains("text/html")) {
			InputStream is = conn.getInputStream();
			String coding = conn.getContentEncoding();
			Document htmlDoc = Jsoup.parse(new BufferedInputStream(is), coding == null ? "UTF-8" : coding, link.getHost());
			is.close();

			// Fetch all elements (a or frame)
			Elements elements = htmlDoc.getElementsByTag("a");
			elements.addAll(htmlDoc.getElementsByTag("frame"));
			for (Element element : elements) {
				String outlinkString = element.tagName().equals("a") ? element.attributes().get("href")
						: element.attributes().get("src");
				if (outlinkString.startsWith("mailto:")) {
					parsedEmails.add(outlinkString);
				} else { // Is not mailto link
					try {
						URL outlink = new URL(link, outlinkString);
						if (!visitedLinks.contains(outlink))
							linksQueue.add(outlink);
						parsedLinks.add(outlink.toString());
					} catch (Exception e) { } 
				}
			}
		}
	}
}
