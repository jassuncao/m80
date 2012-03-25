/**
 * 
 */
package com.joaoassuncao.m80;

/**
 * @author jassuncao
 *
 */
public class RadioInfo {
	
	private String animatorName;
	private String animatorPhoto;
	private String leadArtistName;
	private String songName;
	private String albumName;
	private boolean musicPlaying;
	private String titleName;
	private String artistName;
	private String altAlbumName;
	private String albumArt;
	private String showName;
	
	public RadioInfo() {		
	}	

	public String getAnimatorName() {
		return animatorName;
	}

	public String getAnimatorPhoto() {
		return animatorPhoto;
	}

	public String getLeadArtistName() {
		return leadArtistName;
	}

	public String getSongName() {
		return songName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAnimatorName(String animatorName) {
		this.animatorName = animatorName;
	}

	public void setAnimatorPhoto(String animatorPhoto) {
		this.animatorPhoto = animatorPhoto;
	}

	public void setLeadArtistName(String leadArtistName) {
		this.leadArtistName = leadArtistName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public boolean isMusicPlaying() {
		return musicPlaying;
	}

	public void setMusicPlaying(boolean musicPlaying) {
		this.musicPlaying = musicPlaying;
	}	

	public String getTitleName() {
		return titleName;
	}
	
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getAltAlbumName() {
		return altAlbumName;
	}
	
	public void setAltAlbumName(String altAlbumName) {
		this.altAlbumName = altAlbumName;
	}

	public String getAlbumArt() {
		return albumArt;
	}
	
	public void setAlbumArt(String albumArt) {
		this.albumArt = albumArt;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RadioInfo [animatorName=").append(animatorName).append(", animatorPhoto=")
				.append(animatorPhoto).append(", leadArtistName=").append(leadArtistName).append(", songName=")
				.append(songName).append(", albumName=").append(albumName).append(", musicPlaying=")
				.append(musicPlaying).append(", titleName=").append(titleName).append(", artistName=")
				.append(artistName).append(", altAlbumName=").append(altAlbumName).append(", albumArt=")
				.append(albumArt).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((albumArt == null) ? 0 : albumArt.hashCode());
		result = prime * result + ((albumName == null) ? 0 : albumName.hashCode());
		result = prime * result + ((altAlbumName == null) ? 0 : altAlbumName.hashCode());
		result = prime * result + ((animatorName == null) ? 0 : animatorName.hashCode());
		result = prime * result + ((animatorPhoto == null) ? 0 : animatorPhoto.hashCode());
		result = prime * result + ((artistName == null) ? 0 : artistName.hashCode());
		result = prime * result + ((leadArtistName == null) ? 0 : leadArtistName.hashCode());
		result = prime * result + (musicPlaying ? 1231 : 1237);
		result = prime * result + ((songName == null) ? 0 : songName.hashCode());
		result = prime * result + ((titleName == null) ? 0 : titleName.hashCode());
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
		RadioInfo other = (RadioInfo) obj;
		if (albumArt == null) {
			if (other.albumArt != null)
				return false;
		} else if (!albumArt.equals(other.albumArt))
			return false;
		if (albumName == null) {
			if (other.albumName != null)
				return false;
		} else if (!albumName.equals(other.albumName))
			return false;
		if (altAlbumName == null) {
			if (other.altAlbumName != null)
				return false;
		} else if (!altAlbumName.equals(other.altAlbumName))
			return false;
		if (animatorName == null) {
			if (other.animatorName != null)
				return false;
		} else if (!animatorName.equals(other.animatorName))
			return false;
		if (animatorPhoto == null) {
			if (other.animatorPhoto != null)
				return false;
		} else if (!animatorPhoto.equals(other.animatorPhoto))
			return false;
		if (artistName == null) {
			if (other.artistName != null)
				return false;
		} else if (!artistName.equals(other.artistName))
			return false;
		if (leadArtistName == null) {
			if (other.leadArtistName != null)
				return false;
		} else if (!leadArtistName.equals(other.leadArtistName))
			return false;
		if (musicPlaying != other.musicPlaying)
			return false;
		if (songName == null) {
			if (other.songName != null)
				return false;
		} else if (!songName.equals(other.songName))
			return false;
		if (titleName == null) {
			if (other.titleName != null)
				return false;
		} else if (!titleName.equals(other.titleName))
			return false;
		return true;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getShowName() {
		return showName;
	}
	
}
