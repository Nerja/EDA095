package lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader {
	
	public static List<URL> fetchPdfUrls(String homepage) throws MalformedURLException {
		List<URL> files = new LinkedList<URL>();
		URL page = new URL(homepage);
		try(BufferedReader br = new BufferedReader(new InputStreamReader(page.openStream()))) {
			String pageCode = "", line;
			while((line = br.readLine()) != null)
				pageCode += line;
			Pattern linkPattern = Pattern.compile("href=\"([^\"]*\\.pdf)\"");
			Matcher linkMatcher = linkPattern.matcher(pageCode);
			while(linkMatcher.find()) 
				files.add(new URL(page, linkMatcher.group(1)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}
	
	public static void main(String[] args) throws IOException {
		/*if(args.length != 2) {
			System.err.println("Please enter homepage");
			System.exit(1);
		}*/
		List<URL> pdfFiles = fetchPdfUrls("http://cs229.stanford.edu/materials.html");
		for(URL url : pdfFiles)
			System.out.println(url);
	}

}
