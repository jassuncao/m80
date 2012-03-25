package com.joaoassuncao.m80;

import java.util.ArrayList;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class LastPlayedSongsHandler extends DefaultHandler {
	
	private boolean processTimePlayed = false;
	private ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
	private SongInfo currentItem;
	private StringBuilder charactersCollector = new StringBuilder();
	
	public ArrayList<SongInfo> getSongs() {
		return songs;
	}
	
	@Override
	public void endDocument() throws SAXException {
		if(currentItem!=null)
			songs.add(currentItem);
		currentItem=null;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if("DIV".equalsIgnoreCase(localName)) {
			String classAttr = attributes.getValue("class"); 
			if("songInfo".equals(classAttr)) {
				handleSongInfoTag();
			}
			else if("timePlayed".equals(classAttr)) {
				processTimePlayed = true;
				charactersCollector.setLength(0);
			}
		}
		else if("SPAN".equalsIgnoreCase(localName)){
			String classAttr = attributes.getValue("class"); 
			if("artistInfo".equals(classAttr)) {
				if(currentItem!=null) {
					currentItem.setArtist(attributes.getValue("title"));
				}
			}
			else if("songName".equals(classAttr)) {	
				if(currentItem!=null) {
					currentItem.setSongName(attributes.getValue("title"));
				}
			}
		}
		else if("IMG".equalsIgnoreCase(localName)) {
			String classAttr = attributes.getValue("class"); 
			if("capaAlbum".equals(classAttr)) {
				String src = attributes.getValue("src");
				if(currentItem!=null && src!=null) {
					handleAlbumCover(src);					
				}
			}
		}		
	};
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if("DIV".equalsIgnoreCase(localName)) {
			if(processTimePlayed) {
				currentItem.setTimePlayed(charactersCollector.toString().trim());
				processTimePlayed=false;
			}
		}
	};
	
	public void characters(char[] ch, int start, int length) throws SAXException {		
		if(processTimePlayed) {
			charactersCollector.append(ch,start,length);						
		}
	};
	
	private void handleAlbumCover(String src) {
		int idx = src.indexOf("?img=");
		if(idx>0) {	
			idx+=5;
			StringBuilder aux = new StringBuilder();
			int lastSlashIdx=-1;
			for(;idx<src.length();++idx) {
				char c = src.charAt(idx);							
				if(c=='&') 
					break;							
				else
					aux.append(c);
				if(c=='/') {								
					lastSlashIdx=aux.length();
				}
			}							
			currentItem.setAlbumCover(aux.substring(lastSlashIdx));
		}
	}
		
	protected void handleSongInfoTag() {
		if(currentItem!=null)
			songs.add(currentItem);
		currentItem = new SongInfo();
	}

}
