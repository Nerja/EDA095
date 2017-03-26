import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	LinkedList<URL> urlQueue;
	int limit;
	int count;

	HashSet<String> visited;

	LinkedList<URL> emails;
	LinkedList<URL> links;
	
	public Crawler(URL startUrl, int l) {
		urlQueue = new LinkedList<URL>();
		urlQueue.add(startUrl);
		emails = new LinkedList<URL>();
		links = new LinkedList<URL>();
		limit = l;
		visited = new HashSet<String>();
		count = 0;
	}

	public void start() throws IOException {
		long t = System.currentTimeMillis();
		while (urlQueue.size() > 0 && count < limit) {
			parse(urlQueue.pop());
		}
		
		System.out.println("List of addresses:");
		for (URL u : emails) {
			System.out.println(u);
		}
		System.out.println("\nList of URLs:");
		for(URL u : links) {
			System.out.println(u);
		}
		System.out.println("");
		System.out.println("Took " + (System.currentTimeMillis() - t) + "ms");
	}

	void parse(URL url) {
		try {
			URLConnection conn = url.openConnection();
			String contentType = conn.getContentType();
			if (contentType != null && contentType.contains("text/html")) {

				count++;
				System.out.println("Crawled " + count + "/" + limit);
				this.links.add(url);
				
				InputStream is = conn.getInputStream();
				Document doc = Jsoup.parse(is, "UTF-8", url.getHost());

				Elements links = doc.getElementsByTag("a");
				addLinks(links);
				links = doc.getElementsByTag("frame");
				addLinks(links);
				
				is.close();
			}
		} catch (IOException e) {

		}

	}
	void addLinks(Elements links) throws MalformedURLException {
		for (Element link : links) {
			String href = link.attr("abs:href");
			if (href.length() > 0 && !visited.contains(href)) {
				URL u = new URL(href);
				if(href.contains("mailto:")) {
					emails.add(u);
				}
				else {
					urlQueue.add(u);
				}
				visited.add(href);
			}
		}
	}
}
