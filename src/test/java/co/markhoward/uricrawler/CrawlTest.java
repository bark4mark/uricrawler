package co.markhoward.uricrawler;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CrawlTest {
	private Crawl crawl;
	@Rule
	public TemporaryFolder tempDirectory = new TemporaryFolder();
	
	@Before
	public void setup() throws IOException{
		File temp = tempDirectory.newFolder();
		crawl = new Crawl(temp);
	}
	
	@Test
	public void shouldNotReturnNullValues(){
		Assert.assertTrue(crawl.getAcceptedBases() != null);
		Assert.assertTrue(crawl.getName() != null);
		Assert.assertTrue(crawl.getBrokenUris() != null);
		Assert.assertTrue(crawl.getExternalUris() != null);
	}
}
