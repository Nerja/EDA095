package lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
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
		if(args.length != 1) {
			System.err.println("Please enter homepage");
			System.exit(1);
		}
		List<URL> pdfFiles = fetchPdfUrls(args[0]);
		download(pdfFiles);
	}

	private static void download(List<URL> pdfFiles) {
		Iterator<URL> itr = pdfFiles.iterator();
		for(int i = 1; i <= pdfFiles.size(); i++) {
			System.out.println("Downloading: " + i + "/" + pdfFiles.size());
			download(itr.next());
		}
	}

	//http://stackoverflow.com/questions/20265740/how-to-download-a-pdf-from-a-given-url-in-java
	private static void download(URL file) {
		try(InputStream in = file.openStream()){
			String fileName = file.getPath().substring(file.getPath().lastIndexOf('/')+1);
			Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
