package executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
		System.out.println("*** Executor ***");
		if(args.length != 2) {
			System.err.println("Please enter homepage and number threads");
			System.exit(1);
		}
		int numThreads = 0;
		try {
			numThreads = Integer.parseInt(args[1]);
			if(numThreads <= 0) {
				System.out.println("Please enter positive nbr");
				System.exit(1);
			}
		} catch (Exception e) {
			System.out.println("Please enter positive nbr");
			System.exit(1);
		}
		
		//Create numThreads workers
		ExecutorService service = Executors.newFixedThreadPool(numThreads);
		
		//Create a "task" for each url and submit to workpool
		for(URL url : fetchPdfUrls(args[0]))
			service.submit(new DownloadTask(url));
		service.shutdown();
	}
}
