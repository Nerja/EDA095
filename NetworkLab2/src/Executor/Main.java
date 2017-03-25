package Executor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	public static void main(String[] args) {
		try {
			PDFScraper pdfs = new PDFScraper(new URL("http://cs229.stanford.edu/materials.html"));
			ExecutorService pool = Executors.newFixedThreadPool(10);
			for(URL url : pdfs.getAllURLS()) {
				pool.submit(new Runner(url));
			}
			
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL, please try again!");
		}
	}
}
