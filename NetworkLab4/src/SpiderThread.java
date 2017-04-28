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
	Database db;
	
	public SpiderThread(Database db) {
		this.db = db;
	}
	
	@Override
	public void run() {
		boolean keepCrawling = true;
		while(keepCrawling) {
			try {
				parse(db.pop());
			}
			catch(Exception e) {
				keepCrawling = false;
			}
		}
	}
	
	void parse(URL url) {
		try {
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(2000);
			String contentType = conn.getContentType();
			if (contentType != null && contentType.contains("text/html")) {
				
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
}
