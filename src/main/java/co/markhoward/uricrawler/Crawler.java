package co.markhoward.uricrawler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import co.markhoward.urinormalizer.UriNormalizer;

public class Crawler {
	private final Crawl crawl;
	private final Deque<String> backFrontier;
	private final Deque<String> frontFrontier;
	private final ExecutorService service;
	private final Set<Pattern> excluded;

	public Crawler(Crawl crawl) {
		this.crawl = crawl;
		this.backFrontier = new ArrayDeque<>();
		this.frontFrontier = new ArrayDeque<>();
		this.service = Executors.newCachedThreadPool();
		this.excluded = compilePatterns(crawl.getExcluded());
	}

	public void crawl() {
		String seed = this.crawl.getSeed();
		logger.debug("Starting crawl at: {}", seed);
		int depth = 0;
		if(seed == null)
			throw new CrawlCannotStartException("Seed should be populated but isn't");
		Optional<String> seedOptional = UriNormalizer.normalizeUri(seed);
		if(!seedOptional.isPresent())
			throw new CrawlCannotStartException("Seed is invalid");
		
		this.frontFrontier.push(seedOptional.get());
		
		while(depth < this.crawl.getMaxDepth()){
			run();
			this.frontFrontier.addAll(this.backFrontier);
			this.backFrontier.clear();
			this.crawl.commitData();
			depth ++;
		}
		logger.debug("Crawl complete");
	}
	
	public void run(){
		List<Future<Set<String>>> workers = Lists.newArrayList();
		while(!this.frontFrontier.isEmpty()){
			String uri = this.frontFrontier.pop();
			if(this.crawl.getCrawled().contains(uri))
				continue;
			
			if(this.crawl.getMaxCrawled() <= this.crawl.getCrawled().size())
				continue;
			
			boolean excluded = false;
			for(Pattern shouldBeExcluded: this.excluded)
				if(shouldBeExcluded.matcher(uri).matches())
					excluded = true;
			
			if(excluded)
				continue;
			
			CrawlWorker crawlWorker = new CrawlWorker(uri, crawl);
			workers.add(service.submit(crawlWorker));
			this.crawl.getCrawled().add(uri);
		}
		
		for(Future<Set<String>> worker: workers){
			try {
				Set<String> extractedUris = worker.get();
				this.backFrontier.addAll(extractedUris);
			} catch (InterruptedException | ExecutionException exception) {
				logger.error("An error has occurred: ", exception);
			}
		}
	}
	
	private Set<Pattern> compilePatterns(Set<String> excluded) {
		Set<Pattern> patterns = Sets.newHashSet();
		for(String patternForExclusion:excluded){
			try{
				patterns.add(Pattern.compile(patternForExclusion));
			}
			catch(PatternSyntaxException exception){
				logger.error("The pattern cannot be compiled: {}", patternForExclusion, exception);
			}
		}
		return patterns;
	}
	
	private final Logger logger = LogManager.getLogger(Crawler.class);
}
