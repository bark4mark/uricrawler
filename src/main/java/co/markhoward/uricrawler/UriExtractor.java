package co.markhoward.uricrawler;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Sets;

import co.markhoward.urinormalizer.UriNormalizer;

public class UriExtractor {
	private final Crawl crawl;

	public UriExtractor(Crawl crawl) {
		this.crawl = crawl;
	}

	public Set<String> extract(Document document) {
		Set<String> extractedUris = Sets.newHashSet();
		Elements links = document.select(A_HREF);
		for(Element link: links){
			Optional<String> uri = UriNormalizer.normalizeUri(link.attr(ABS), true);
			if(!uri.isPresent() || StringUtils.isBlank(uri.get()))
				continue;
			
			String normalized = uri.get();
			
			boolean matchesBase = false;
			for(String acceptedBase: crawl.getAcceptedBases()){
				if(normalized.toLowerCase().startsWith(acceptedBase.toLowerCase()))
					matchesBase = true;
			}
			
			if(!matchesBase){
				crawl.getExternalUris().add(normalized);
				continue;
			}
				
			
			extractedUris.add(normalized);
		}
		return extractedUris;
	}

	private static final String ABS = "abs:href";
	private static final String A_HREF = "a[href]";
}
