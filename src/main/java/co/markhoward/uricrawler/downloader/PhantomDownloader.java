package co.markhoward.uricrawler.downloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import org.jsoup.nodes.Document;

public class PhantomDownloader implements UriDownloader{

	@Override
	public Optional<Document> download() {
		ProcessBuilder processBuilder = new ProcessBuilder("CMD", "/C", "phantomjs", "loader.js");
		processBuilder.directory(new File("C:\\phantom"));

	    Process process = null;
		try {
			process = processBuilder.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    InputStream is = process.getInputStream();
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    try {
			while ((line = br.readLine()) != null) {
			  System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	    System.out.println("Program terminated!");
		return null;
	}
	
}
