package co.markhoward.uricrawler.events;

import org.jsoup.nodes.Document;

public interface DocumentListener {
	void execute(Document document);
}
