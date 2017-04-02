package multi;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Processor extends Thread {
	private WorkMaster master;

	public Processor(WorkMaster master) {
		this.master = master;
	}

	@Override
	public void run() {
		URL work;
		try {
			while (!isInterrupted() && (work = master.fetchWork()) != null) {
				if (work.toString().contains("pierre"))
					System.out.println(work.toString());
				try {
					process(work);
				} catch (FileNotFoundException e) {
					System.out.println("Unable to load " + work);
				} catch (MalformedURLException e) {
					// wrong protocol
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Crawler thread stopped");
		}
	}

	private void process(URL work) throws IOException {
		URLConnection conn = work.openConnection();
		conn.setConnectTimeout(3000);
		conn.setReadTimeout(3000);
		String content = conn.getContentType();
		// Ignore non html
		if (content != null && content.contains("text/html")) {
			InputStream is = conn.getInputStream();
			String coding = conn.getContentEncoding();
			Document htmlDoc = Jsoup.parse(new BufferedInputStream(is), coding == null ? "UTF-8" : coding,
					work.getHost());
			is.close();

			Elements elements = htmlDoc.getElementsByTag("a");
			elements.addAll(htmlDoc.getElementsByTag("frame"));
			List<String> parsedLinks = new LinkedList<String>();
			List<String> parsedEmails = new LinkedList<String>();
			for (Element element : elements)
				processElement(element, parsedLinks, parsedEmails);
			master.addParsedInfo(work, parsedLinks, parsedEmails);
		}
	}

	private void processElement(Element element, List<String> parsedLinks, List<String> parsedEmails) {
		String outlinkString = element.tagName().equals("a") ? element.attributes().get("href")
				: element.attributes().get("src");
		if (outlinkString.startsWith("mailto:"))
			parsedEmails.add(outlinkString);
		else
			parsedLinks.add(outlinkString);
	}
}
