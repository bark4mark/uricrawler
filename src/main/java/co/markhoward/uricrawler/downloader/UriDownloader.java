package co.markhoward.uricrawler.downloader;

import java.util.Optional;

import org.jsoup.nodes.Document;

public interface UriDownloader {
	Optional<Document> download();
}
