package neo4j.Diccionario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class RaePalabra {

	/*public static void main(String[] args) {
		searchWord("https://dle.rae.es/huele");
	}*/
	
	
	public static Document searchWord(String url) {
		final String URL = "https://dle.rae.es/" + url;
		return new Document(URL ,getTextFromHtml(getHtml(URL)));
	}
	
	private static String getHtml(String url) {
		try {
			final InputStream stream = (InputStream) new URL(url).getContent();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			final StringBuffer sb = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				if (sb.length() != 0) {
					sb.append("\n");
				}
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			return "";
		}
	}

	private static String getTextFromHtml(String html) {
		org.jsoup.nodes.Document document = org.jsoup.Jsoup.parse(html);

		document.outputSettings(new org.jsoup.nodes.Document.OutputSettings().prettyPrint(false));
		document.select("br").append("\\n");
		document.select("p").prepend("\\n\\n");
		String result = document.html().replaceAll("\\\\n", "");
		result = result.replaceAll("\n", "");
		result = org.jsoup.Jsoup.clean(result, "", org.jsoup.safety.Whitelist.none(),
				new org.jsoup.nodes.Document.OutputSettings().prettyPrint(false));
		return result;
	}
	
}
