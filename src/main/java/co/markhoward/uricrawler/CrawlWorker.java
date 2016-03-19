package co.markhoward.uricrawler;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

import org.jsoup.nodes.Document;

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
		UriDownloader uriDownloader = new UriDownloader(this.crawl, uri);
		Optional<Document> downloaded = uriDownloader.download();
		if(!downloaded.isPresent())
			return Sets.newHashSet();
		
		UriExtractor uriExtractor = new UriExtractor(crawl);
		return uriExtractor.extract(downloaded.get());
	}

}
