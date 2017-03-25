package Runnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Runner implements Runnable {
	PDFScraper scraper;

	public Runner(PDFScraper s) {
		scraper = s;
	}

	@Override
	public void run() {
		boolean moreToDownload = true;
		while (moreToDownload) {
			try {
				URL url = scraper.pop();
				String name = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
				System.out.println("Downloading " + name);
				try (InputStream in = url.openStream()) {
					Files.copy(in, Paths.get(name), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (NoSuchElementException e) {
				moreToDownload = false;
			}
		}
	}
}
