package Runnable;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {
	public static void main(String[] args) {
		try {
			PDFScraper pdfs = new PDFScraper(new URL("http://cs229.stanford.edu/materials.html"));
			for(int i = 0; i < 10; i++) {
				Thread downloadThread = new Thread(new Runner(pdfs));
				downloadThread.start();
			}
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL, please try again!");
		}
	}
}
