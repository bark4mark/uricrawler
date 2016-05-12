package co.markhoward.uricrawler.downloader;

import org.junit.Test;

public class PhantomDownloaderTest {
	
	@Test
	public void test() {
		PhantomDownloader phantomDownloader = new PhantomDownloader();
		phantomDownloader.download();
	}

}
