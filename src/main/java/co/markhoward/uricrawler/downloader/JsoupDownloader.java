package co.markhoward.uricrawler.downloader;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import co.markhoward.uricrawler.Crawl;

public class JsoupDownloader implements UriDownloader{
	private final Crawl crawl;
	private final String uri;

	public JsoupDownloader(Crawl crawl, String uri) {
		this.crawl = crawl;
		this.uri = uri;
	}

	public Optional<Document> download() {
		try {
			Connection connection = Jsoup.connect(uri);
			connection.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0"); 
			connection.referrer("http://www.google.com");  
			connection.timeout(12000);
			connection.ignoreContentType(true);
			connection.followRedirects(true);
			logger.debug("Downloading: {}", uri);
			Response response = connection.execute();
			if(!response.contentType().toLowerCase().contains(HTML)){
				crawl.getBrokenUris().put(uri, CONTENT_TYPE_ERROR);
				return Optional.empty();
			}
			
			Document document = response.parse();
			this.crawl.getEventBus().post(document);
			return Optional.of(document);
		} catch (IOException exception) {
			logger.error("The URI could not be downloaded: {}", uri, exception);
			crawl.getBrokenUris().put(uri, exception.getMessage());
			return Optional.empty();
		}
	}
	
	private final static String HTML = "html";
	private final static String CONTENT_TYPE_ERROR = "Content type not set for Html";
	private final Logger logger = LogManager.getLogger(JsoupDownloader.class);
}
