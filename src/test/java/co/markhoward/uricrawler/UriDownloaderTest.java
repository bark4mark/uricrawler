package co.markhoward.uricrawler;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class UriDownloaderTest {
	@Rule
	public TemporaryFolder tempDirectory = new TemporaryFolder();
	@Test
	public void shouldDownloadUriAsADocument() throws IOException{
		File temp = tempDirectory.newFolder();
		try(Crawl crawl = new Crawl(temp)){
			String uri = "http://bark4mark.blogspot.ie/2015/11/getting-going-with-go.html";
			UriDownloader uriDownloader = new UriDownloader(crawl, uri);
			Optional<Document> document = uriDownloader.download();
			Assert.assertTrue(document.isPresent());
		} catch (Exception exception) {
			Assert.fail(exception.getMessage());
		}
	}
	
}
