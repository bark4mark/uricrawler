package co.markhoward.uricrawler.downloader;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import co.markhoward.uricrawler.Crawl;
import co.markhoward.uricrawler.downloader.JsoupDownloader;


public class JsoupDownloaderTest {
	@Rule
	public TemporaryFolder tempDirectory = new TemporaryFolder();
	@Test
	public void shouldDownloadUriAsADocument() throws IOException{
		File temp = tempDirectory.newFolder();
		try(Crawl crawl = new Crawl(temp)){
			String uri = "http://bark4mark.blogspot.ie/2015/11/getting-going-with-go.html";
			JsoupDownloader uriDownloader = new JsoupDownloader(crawl, uri);
			Optional<Document> document = uriDownloader.download();
			Assert.assertTrue(document.isPresent());
		} catch (Exception exception) {
			Assert.fail(exception.getMessage());
		}
	}
	
}
