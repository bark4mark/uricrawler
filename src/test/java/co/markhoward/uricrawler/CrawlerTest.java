package co.markhoward.uricrawler;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CrawlerTest {
	@Rule
	public TemporaryFolder tempDirectory = new TemporaryFolder();
	
	@Test
	public void shouldCrawl() throws IOException{
		File temp = tempDirectory.newFolder();
		try(Crawl crawl = new Crawl(temp)){
			crawl.setSeed("http://www.itb.ie/");
			crawl.getAcceptedBases().add("http://www.itb.ie/");
			crawl.setMaxDepth(3);
			crawl.setMaxCrawled(5);
			
			Crawler crawler = new Crawler(crawl);
			crawler.crawl();
			Assert.assertFalse(crawl.getCrawled().isEmpty());
		} catch (Exception exception) {
			Assert.fail(exception.getMessage());
		}
	}
}
