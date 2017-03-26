import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Test {
	public Test() {
	}
	
	
	public static void main(String[] args) throws IOException {

		URL url = new URL("http://www.lsi.us.es/%7Edbc/dbc_archivos/pubs/Benavides_gttse05.pdf");
		URLConnection conn = url.openConnection();
		String contentType = conn.getContentType();
		System.out.println(contentType);
	}
}
