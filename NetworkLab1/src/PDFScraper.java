import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFScraper {

	URL baseUrl;
	List<URL> pdfs = new LinkedList<URL>();
	
	public PDFScraper(URL urlToScrape) throws MalformedURLException {
		baseUrl = urlToScrape;
		findPdfs();
	}

	public void findPdfs() throws MalformedURLException {
		String html = getHTML(baseUrl);
		
		String aHref = "href=[\"']([^\"]*\\.pdf)[\"']";
		
		Pattern r = Pattern.compile(aHref);
		Matcher matcher = r.matcher(html);
		while(matcher.find()) {
			pdfs.add(new URL(baseUrl, matcher.group(1)));
		}
	}

	public void download() {
		int count = 0;
		for(URL url : pdfs) {
			String name = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
			System.out.println("Downloading " + name + "(" + (++count) + " of " + pdfs.size()+")");
			try(InputStream in = url.openStream()){
				Files.copy(in, Paths.get(name), StandardCopyOption.REPLACE_EXISTING);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getHTML(URL url) {
		URLConnection connection;
		try {
			connection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder response = new StringBuilder();
			String inputLine;

			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);

			in.close();
			return response.toString();
		} catch (IOException e) {
			System.out.println("Could not fetch HTML from " + url.toString());
		}
		return null;
	}

}
