package co.markhoward.uricrawler.events;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.eventbus.Subscribe;

import co.markhoward.uricrawler.Crawl;
import co.markhoward.uricrawler.Crawler;

public class DocumentListenerTest {

	@Rule
	public TemporaryFolder tempDirectory = new TemporaryFolder();
	
	@Test
	public void testListenerGetsCalled() throws IOException {
		File temp = tempDirectory.newFolder();
		try(Crawl crawl = new Crawl(temp)){
			crawl.setSeed("http://bark4mark.blogspot.ie/");
			crawl.getAcceptedBases().add("http://bark4mark.blogspot.ie/");
			crawl.setMaxDepth(1);
			crawl.setMaxCrawled(1);
			
			final AtomicBoolean called = new AtomicBoolean(false);
			
			Crawler crawler = new Crawler(crawl);
			crawl.addListener(new DocumentListener() {
				@Override
				@Subscribe
				public void execute(Document document) {
					called.set(true);
				}
			});
			crawler.crawl();
			Assert.assertTrue(called.get());
			Assert.assertFalse(crawl.getCrawled().isEmpty());
		} catch (Exception exception) {
			Assert.fail(exception.getMessage());
		}
	}

}
