/**
 * 
 */
package com.joaoassuncao.m80;

import java.util.Comparator;

import android.graphics.Bitmap;

/**
 * @author jassuncao
 *
 */
public class SongInfo {

	private String timePlayed;
	private String artist;
	private String songName;
	private String albumCover;
	private Bitmap albumCoverBitmap;
	private boolean abumCoverNeedsLoading=true;

	public SongInfo(String timePlayed, String artist, String songName, String albumCover) {
		this.timePlayed=timePlayed;
		this.artist=artist;
		this.songName=songName;
		this.albumCover=albumCover;
	}

	public SongInfo() {		
	}

	public void setTimePlayed(String timePlayed) {
		this.timePlayed=timePlayed;
	}

	public void setArtist(String value) {
		this.artist = value;
	}

	public void setSongName(String value) {
		this.songName = value;
	}

	public String getTimePlayed() {
		return timePlayed;
	}

	public String getArtist() {
		return artist;
	}

	public String getSongName() {
		return songName;
	}	

	public void setAlbumCover(String value) {
		this.albumCover = value;
	}
	
	public String getAlbumCover() {
		return albumCover;
	}
	
	public void setAlbumCoverBitmap(Bitmap albumCoverBitmap) {
		this.albumCoverBitmap = albumCoverBitmap;
	}
	 
	public Bitmap getAlbumCoverBitmap() {
		return albumCoverBitmap;
	}
	
	public boolean isAlbumCoverNeedsLoading() {
		return abumCoverNeedsLoading;
	}
	
	public void setAbumCoverNeedsLoading(boolean abumCoverNeedsLoading) {
		this.abumCoverNeedsLoading = abumCoverNeedsLoading;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((albumCover == null) ? 0 : albumCover.hashCode());
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((songName == null) ? 0 : songName.hashCode());
		result = prime * result + ((timePlayed == null) ? 0 : timePlayed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SongInfo other = (SongInfo) obj;
		if (albumCover == null) {
			if (other.albumCover != null)
				return false;
		} else if (!albumCover.equals(other.albumCover))
			return false;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (songName == null) {
			if (other.songName != null)
				return false;
		} else if (!songName.equals(other.songName))
			return false;
		if (timePlayed == null) {
			if (other.timePlayed != null)
				return false;
		} else if (!timePlayed.equals(other.timePlayed))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SongInfo [timePlayed=").append(timePlayed).append(", artist=").append(artist)
				.append(", songName=").append(songName).append(", albumCover=").append(albumCover).append("]");
		return builder.toString();
	}

	public static Comparator<? super SongInfo> getComparator() {
		return comparatorInstance;
	}
	
	private static SongInfoComparator comparatorInstance = new SongInfoComparator(); 
	
	private static class SongInfoComparator implements Comparator<SongInfo>{

		public int compare(SongInfo lhs, SongInfo rhs) {
			return rhs.timePlayed.compareTo(lhs.timePlayed);
		}
		
	}
}
