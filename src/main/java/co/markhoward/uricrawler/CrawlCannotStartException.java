package co.markhoward.uricrawler;

public class CrawlCannotStartException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CrawlCannotStartException(String message) {
		super(message);
	}

}
