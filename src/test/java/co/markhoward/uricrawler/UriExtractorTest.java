package co.markhoward.uricrawler;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class UriExtractorTest {
	private Crawl crawl;
	private Document document;
	@Rule
	public TemporaryFolder tempDirectory = new TemporaryFolder();
	
	@Before
	public void setup() throws URISyntaxException, IOException{
		File temp = tempDirectory.newFolder();
		crawl = new Crawl(temp);
		crawl.getAcceptedBases().add("http://bark4mark.blogspot.ie");
		URL url = Resources.getResource("index.html");
		File file = new File(url.toURI());
		document = Jsoup.parse(file, Charsets.UTF_8.name());
	}
	@Test
	public void shouldExtractUrisThatMatchAcceptedBases() throws IOException, URISyntaxException{
		
		UriExtractor uriExtractor = new UriExtractor(crawl);
		Set<String> extractedUris = uriExtractor.extract(document);
		Assert.assertFalse(extractedUris.isEmpty());
		for(String extractedUri: extractedUris){
			Assert.assertTrue(extractedUri.toLowerCase().startsWith("http://bark4mark.blogspot.ie"));
		}
	}
}
