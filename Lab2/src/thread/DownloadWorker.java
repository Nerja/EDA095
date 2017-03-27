package thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadWorker extends Thread {
	private WorkMaster master;
	
	public DownloadWorker(WorkMaster master) {
		this.master = master;
	}
	
	@Override
	public void run() {
		URL work;
		while((work = master.fetchWork()) != null) {
			try(InputStream in = work.openStream()){
				String fileName = work.getPath().substring(work.getPath().lastIndexOf('/')+1);
				Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
