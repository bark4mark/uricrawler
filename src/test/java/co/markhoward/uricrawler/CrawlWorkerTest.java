package co.markhoward.uricrawler;

import java.io.File;
import java.util.Set;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CrawlWorkerTest {
	@Rule
	public TemporaryFolder tempDirectory = new TemporaryFolder();

	@Test
	public void shouldDownloadAndExtractRelevantUris() throws Exception {
		File temp = tempDirectory.newFolder();
		try(Crawl crawl = new Crawl(temp)){
			crawl.getAcceptedBases().add("http://bark4mark.blogspot.ie");
			CrawlWorker crawlWorker = new CrawlWorker("http://bark4mark.blogspot.ie/2015/11/getting-going-with-go.html",
					crawl);
			Set<String> extractedUris = crawlWorker.call();
			crawl.commitData();
			Assert.assertFalse(extractedUris.isEmpty());
			for(String extractedUri: extractedUris){
				Assert.assertTrue(extractedUri.toLowerCase().startsWith("http://bark4mark.blogspot.ie"));
			}
		}
	}
}
