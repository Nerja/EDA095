package clean;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderThread extends Thread {
	private int nbr;
	private Database db;
	
	public SpiderThread(Database db, int nbr) {
		this.db = db;
		this.nbr = nbr;
	}
	
	@Override
	public void run() {
		while(db.keepCrawling())
			crawl();
		
		System.out.println("Thread " + nbr + " shutting down");
	}
	
	private void crawl() {
		URL url = null;
		try {
			url = db.pop(nbr);
			
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(500);
			conn.setReadTimeout(500);
			String contentType = conn.getContentType();
			
			//if webpage is html
			if (contentType != null && contentType.contains("text/html")) {
				InputStream is = conn.getInputStream();
				Document doc = Jsoup.parse(is, "UTF-8", url.getHost());
				is.close();
				
				addLinks(doc, url);
			}
		}catch (FileNotFoundException e) {
			System.out.println("Load fail: " + url.toString());
		} catch (IOException e) {
			System.out.println(url.toString() + " error: " + e.getMessage());
		}
		catch (InterruptedException e) {
			System.out.println("Crawler Thread Shutting Down");
		}
	}
	

	private void addLinks(Document doc, URL base) throws MalformedURLException {
		Element baseElement = doc.getElementsByTag("base").first();
		if (baseElement != null)
			base = new URL(baseElement.attr("href"));
		
		Elements links = doc.getElementsByTag("a");
		links.addAll(doc.getElementsByTag("frame"));
		
		for (Element link : links) {
			String href = link.tagName().equals("a") ? link.attr("abs:href") : link.attr("abs:src");
			if (href.length() > 0 && !db.visited(new URL(href))) {
				URL u = new URL(href);
				if(href.contains("mailto:")) {
					db.addEmail(u);
				}
				else {
					db.add(u);
				}
			}
		}
	}

	public int getNbr() {
		return nbr;
	}
}
