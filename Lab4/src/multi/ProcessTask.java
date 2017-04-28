package multi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProcessTask implements Runnable {

	private WorkMonitor workMonitor;
	private URL urlToVisit;
	private ExecutorService executorService;
	
	public ProcessTask(WorkMonitor workMonitor, URL urlToVisit, ExecutorService executorService) {
		this.workMonitor = workMonitor;
		this.urlToVisit = urlToVisit;
		this.executorService = executorService;
	}
	
	@Override
	public void run() {
		if(workMonitor.moreWork())
			visit();
	}

	private void visit() {
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
				parsePage(document);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Can not load " + urlToVisit.toString());
		} catch (IOException e) {
			System.out.println("Visiting " + urlToVisit.toString() + " : " + e.getMessage());
		}
	}

	private void parsePage(Document document) throws MalformedURLException {
		URL baseUrl = urlToVisit;
		Element base = document.getElementsByTag("base").first();
		if (base != null)
			baseUrl = new URL(base.attr("href"));
		Elements elementsToParse = document.getElementsByTag("a");
		elementsToParse.addAll(document.getElementsByTag("frame"));
		List<String> parsedLinks = new LinkedList<String>();
		List<String> parsedEmails = new LinkedList<String>();
		for (Element el : elementsToParse)
			parseElement(baseUrl, el, parsedLinks, parsedEmails);
		for(String link : workMonitor.addParsedInfo(urlToVisit.toString(), parsedLinks, parsedEmails))
			executorService.submit(new ProcessTask(workMonitor, new URL(link), executorService));
	}

	private void parseElement(URL baseUrl, Element el, List<String> parsedLinks, List<String> parsedEmails) {
		String linkString = el.tagName().equals("a") ? el.attr("href") : el.attr("src");
		if (linkString.startsWith("mailto:"))
			parsedEmails.add(linkString.substring(7));
		else {
			try {
				URL newURL = new URL(baseUrl, linkString);
				parsedLinks.add(newURL.toString());
			} catch (IOException e) {
			}
		}
	}

}
