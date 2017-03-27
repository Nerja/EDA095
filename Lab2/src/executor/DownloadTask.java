package executor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadTask implements Runnable {
	private URL file;

	public DownloadTask(URL file) {
		this.file = file;
	}

	@Override
	public void run() {
		try (InputStream in = file.openStream()) {
			String fileName = file.getPath().substring(file.getPath().lastIndexOf('/') + 1);
			Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
