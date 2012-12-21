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
public class TrafficInformationParser {
	
	private final InputStream inputStream;
	
	public TrafficInformationParser(URL feedUrl){		
        try {
        	this.inputStream = feedUrl.openConnection().getInputStream();            
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
        	throw new RuntimeException(e);
		}
    }
	
	public TrafficInformationParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	protected InputStream getInputStream() throws IOException {
        return inputStream;        
    }
	
	public List<TrafficInformationGroup> parse() throws IOException, SAXException {
		TrafficInformationHandler dataHandler = new TrafficInformationHandler(); 
		Parser p = new Parser();
		 p.setContentHandler(dataHandler);
		 InputSource source = new InputSource(getInputStream());
		 source.setEncoding("ISO-8859-1");
		 p.parse(source);
		 return dataHandler.getResult();				
	}		 

}
