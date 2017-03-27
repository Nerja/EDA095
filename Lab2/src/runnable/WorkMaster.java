package runnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkMaster {
	
	private List<URL> files;
	
	public WorkMaster(String homepage) throws MalformedURLException {
		files = new LinkedList<URL>();
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
	}

	public synchronized URL fetchWork() {
		if(files.isEmpty())
			return null;
		else
			return files.remove(0);
	}
}
