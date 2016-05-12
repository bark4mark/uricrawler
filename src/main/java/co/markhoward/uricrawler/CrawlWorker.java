package co.markhoward.uricrawler;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

import org.jsoup.nodes.Document;

import co.markhoward.uricrawler.downloader.UriDownloader;

import com.google.common.collect.Sets;

public class CrawlWorker implements Callable<Set<String>>{
	private final String uri;
	private final Crawl crawl;

	public CrawlWorker(String uri, Crawl crawl) {
		this.uri = uri;
		this.crawl = crawl;
	}

	@Override
	public Set<String> call() throws Exception {
		Class<?> uriDownloaderClass = crawl.getUriDownloader();
		Constructor<?> constructor = uriDownloaderClass.getConstructor(Crawl.class, String.class);
		UriDownloader uriDownloader = (UriDownloader) constructor.newInstance(this.crawl, this.uri);
		Optional<Document> downloaded = uriDownloader.download();
		if(!downloaded.isPresent())
			return Sets.newHashSet();
		
		UriExtractor uriExtractor = new UriExtractor(crawl);
		return uriExtractor.extract(downloaded.get());
	}

}
