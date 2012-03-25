/**
 * 
 */
package com.joaoassuncao.m80;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author jassuncao
 *
 */
public class LastPlayedSongsParser {
	
	private final URL feedUrl;
	
	protected LastPlayedSongsParser(String feedUrl){
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
	
	protected InputStream getInputStream() throws IOException {
        return feedUrl.openConnection().getInputStream();        
    }
	
	public List<SongInfo> parse() throws IOException, SAXException {
		LastPlayedSongsHandler dataHandler = new LastPlayedSongsHandler(); 
		Parser p = new Parser();
		 p.setContentHandler(dataHandler);
		 InputSource source = new InputSource(getInputStream());
		 source.setEncoding("ISO-8859-1");
		 p.parse(source);
		 return dataHandler.getSongs();				
	}

}
