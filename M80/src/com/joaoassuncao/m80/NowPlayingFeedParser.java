/**
 * 
 */
package com.joaoassuncao.m80;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

/**
 * @author jassuncao
 *
 */
public class NowPlayingFeedParser{
	
	final URL feedUrl;
	
	protected NowPlayingFeedParser(String feedUrl){
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
	
	protected InputStream getInputStream() {
        try {
            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public RadioInfo parse() {
		final RadioInfo radioInfo = new RadioInfo();
		RootElement root = new RootElement("RadioInfo");
		Element table = root.getChild("Table");
		table.getChild("DB_LEAD_ARTIST_NAME").setEndTextElementListener(new EndTextElementListener(){
	            public void end(String body) {
	            	radioInfo.setLeadArtistName(body);
	            }
	    });
		table.getChild("DB_SONG_NAME").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	radioInfo.setSongName(body);
            }
		});
		table.getChild("DB_DALET_TITLE_NAME").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	radioInfo.setTitleName(body);
            }
		});
		table.getChild("DB_DALET_ARTIST_NAME").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	radioInfo.setArtistName(body);
            }
		});
		table.getChild("DB_DALET_ALBUM_NAME").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	radioInfo.setAltAlbumName(body);
            }
		});		
		table.getChild("DB_ALBUM_NAME").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	radioInfo.setAlbumName(body);
            }
		});
		table.getChild("DB_ALBUM_IMAGE").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	radioInfo.setAlbumArt(body);
            }
		});		
		
		table.getChild("DB_IS_MUSIC").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	boolean playing = "1".equals(body);
            	radioInfo.setMusicPlaying(playing);
            }
		});
		Element animadorInfo = root.getChild("AnimadorInfo");
		animadorInfo.getChild("NAME").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	radioInfo.setAnimatorName(body);
            }
		});
		animadorInfo.getChild("SHOW_NAME").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	radioInfo.setShowName(body);
            }
		});
		animadorInfo.getChild("IMAGE").setEndTextElementListener(new EndTextElementListener(){
            public void end(String body) {
            	radioInfo.setAnimatorPhoto(body);
            }
		});
		try {
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
            return radioInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);            
        }
		
	}

}
