package co.markhoward.uricrawler;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import co.markhoward.uricrawler.downloader.JsoupDownloader;
import co.markhoward.uricrawler.events.DocumentListener;
import lombok.Data;

@Data
public class Crawl implements AutoCloseable{
	private String name = DEFAULT_NAME;
	private final Set<String> acceptedBases;
	private final Set<String> externalUris;
	private final Set<String> crawled;
	private final Map<String, String> brokenUris;
	private final Set<String> excluded;
	private final Set<DocumentListener> listeners;
	private final EventBus eventBus;
	private String seed;
	private int maxDepth = Integer.MAX_VALUE;
	private final DB db;
	private int maxCrawled = Integer.MAX_VALUE;
	private Class<?> UriDownloader = JsoupDownloader.class;
	
	private static final String DEFAULT_NAME = "Crawl";
	
	public Crawl(File storage){
		File dbFile = new File(storage, DEFAULT_NAME + ".db");
		db = DBMaker.fileDB(dbFile).make();
		this.acceptedBases = db.hashSet("acceptedBases");
		this.externalUris = db.hashSet("externalUris");
		this.crawled = db.hashSet("crawled");
		this.brokenUris = db.hashMap("brokenUris");
		this.excluded = db.hashSet("excluded");
		this.eventBus = new EventBus();
		this.listeners = Sets.newHashSet();
	}
	
	public void commitData(){
		db.commit();
	}
	
	public void addListener(DocumentListener documentListener){
		this.eventBus.register(documentListener);
	}

	@Override
	public void close() throws Exception {
		db.commit();
		db.close();
	}
}
