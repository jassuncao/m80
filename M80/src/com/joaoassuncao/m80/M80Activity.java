package com.joaoassuncao.m80;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class M80Activity extends Activity {
	private static final String TAG = "M80";
	private static final int ALBUM_ART_HEIGHT = 90;
	private static final int ALBUM_ART_WIDTH = 90;
	private static final String NOW_PLAYING_URL = "http://cotonete.clix.pt/webservices/now_playing.asmx/GetCurrentRadioInfo?radio_id=30";
	
	private TextView artistNameView;
	private TextView songNameView;
	private TextView djNameView;
	private Timer timer;
	private RadioInfo latestRadioInfo = null;
	private ImageView albumArtView;	
	private LastPlayedSongsAdapter lastPlayedSongsAdapter;
	private ListView listView1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                  
        setContentView(R.layout.now_playing);    
        artistNameView = (TextView) findViewById(R.id.artistName);
        songNameView = (TextView) findViewById(R.id.songName);  
        djNameView = (TextView) findViewById(R.id.dj_name);
        albumArtView = (ImageView) findViewById(R.id.albumArt);        
        albumArtView.setImageResource(R.drawable.capa_indisponivel);   
        
        List<SongInfo> lastPlayedSongs = new ArrayList<SongInfo>();
        /*        
        lastPlayedSongs.add(new SongInfo("21:28:02", "Bryan Adams", "Heaven", "badams_reckless.jpg"));
        lastPlayedSongs.add(new SongInfo("21:23:36", "Counting Crows", "Mr. Jones", "countingcrows_august.jpg"));
        lastPlayedSongs.add(new SongInfo("21:20:01", "Fairground Attraction", "Perfect", "fairgroundattraction_thefir.jpg"));
        lastPlayedSongs.add(new SongInfo("21:13:47", "Xutos & Pontapés", "O Mundo ao Contrário", "xutos.jpg"));
        */
		lastPlayedSongsAdapter = new LastPlayedSongsAdapter(this, R.layout.song_info_view, lastPlayedSongs);        
        listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setAdapter(lastPlayedSongsAdapter);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();   
    	timer = new Timer();
    	timer.scheduleAtFixedRate(new TimerTask() {			
			@Override
			public void run() {
				try {
					retrieveInfo();
				} catch (Exception e) {
					Log.e(TAG,e.getMessage());
				}
			}
		}, 500,5000);
    	Log.d(TAG,"Started");       	
    }
    
    @Override
    protected void onStop() {
		super.onStop();
    	timer.cancel();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {    	
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:                
                return true;          
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    protected void retrieveInfo() {
    	Log.d(TAG,"retrieveInfo");
    	AndroidSaxFeedParser parser = new AndroidSaxFeedParser(NOW_PLAYING_URL);    	
    	final RadioInfo info = parser.parse();
    	Log.d(TAG, info.toString());
    	if(info!=null && !info.equals(latestRadioInfo)) {
    		Log.d(TAG,"Updating info");
			artistNameView.post(new Runnable() {				
				public void run() {
					updateRadioInformation(info);
				}
				
			});
    	}
    	latestRadioInfo = info;		 
	}  

	protected void updateRadioInformation(RadioInfo info) {
		String artistName = "";		
		StringBuilder songName = new StringBuilder();
		if(info.isMusicPlaying()) {
			artistName = info.getLeadArtistName();
			if(artistName==null || artistName.length()==0) {			
				artistName = info.getArtistName();
			}								
			if(info.getSongName()!=null && songName.length()>0) {
				songName.append(info.getSongName());				
			}
			else {
				songName.append(info.getTitleName());
			}
			String albumName = info.getAlbumName();
			if(albumName==null || albumName.length()==0) {
				albumName = info.getAltAlbumName();
			}
			if(albumName!=null) {
				albumName = albumName.trim();
				if(albumName.length()>0) {
					songName.append(" (").append(albumName).append(')');
				}
			}						
			if(info.getAlbumArt()!=null && info.getAlbumArt().length()>0) {
				new DownloadImageTask(albumArtView).execute(info.getAlbumArt());				
			}
			else {
				albumArtView.setImageResource(R.drawable.capa_indisponivel);
			}
		}
		else {
			albumArtView.setImageResource(R.drawable.capa_indisponivel);
		}
		artistNameView.setText(Html.fromHtml(artistName));
		songNameView.setText(Html.fromHtml(songName.toString()));	
		songNameView.setSelected(true);
		String djName = info.getAnimatorName()!=null?info.getAnimatorName() : "";
		djNameView.setText(Html.fromHtml(djName));
		new RetrieveLastPlayedSongsTask(this.lastPlayedSongsAdapter).execute();
	}

	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		//Example url: http://m80.clix.pt/global_aspx/resize.aspx?img=/upload/album/extreme(1).jpg&h=90&w=90
		private final String urlTemplate = "http://m80.clix.pt/global_aspx/resize.aspx?img=/upload/album/%s&h=%d&w=%d";
		protected final ImageView imageView; 
		
		public DownloadImageTask(final ImageView imageView) {
			this.imageView = imageView;
		}
	    
	    protected Bitmap doInBackground(String... filenames) {
	    	try {	 
	    		Log.i(TAG,"Loading image "+filenames[0]);
	            URL url = new URL(String.format(urlTemplate,filenames[0],ALBUM_ART_HEIGHT, ALBUM_ART_WIDTH));
	            URLConnection connection = url.openConnection();
	            connection.setUseCaches(true);
	            InputStream stream = connection.getInputStream();
	            return BitmapFactory.decodeStream(stream);
	        } catch (MalformedURLException e) {
	            throw new RuntimeException(e);
	        } catch (IOException e) {
	        	Log.e(TAG,e.getMessage());
	        	//Ignore
			}
	        return null;
	    }
	    	  
	    protected void onPostExecute(Bitmap result) {
	    	if(result!=null) 
	    		imageView.setImageBitmap(result);
	    	else
	    		imageView.setImageResource(R.drawable.capa_indisponivel);
	    }		
	}
	
	private class DownloadLastPlayedSongCoverTask extends DownloadImageTask{

		private final SongInfo songInfo;

		public DownloadLastPlayedSongCoverTask(SongInfo songInfo) {
			super(null);
			this.songInfo = songInfo;
		}
		
		protected void onPostExecute(Bitmap result) {
			songInfo.setAlbumCoverBitmap(result);
			lastPlayedSongsAdapter.notifyDataSetChanged();   	
	    }
		
	}
	
	private class RetrieveLastPlayedSongsTask extends AsyncTask<Void, Void, List<SongInfo>>{

		private final LastPlayedSongsAdapter adapter;
		
		public RetrieveLastPlayedSongsTask(LastPlayedSongsAdapter adapter) {
			this.adapter = adapter;
		}
		
		@Override
		protected List<SongInfo> doInBackground(Void... params) {			
			try {
				LastPlayedSongsParser parser = new LastPlayedSongsParser("http://m80.clix.pt/includes/lastplayedsongs.aspx");
				return parser.parse();
			} catch (IOException e) {
				Log.e(TAG,e.getMessage());
	        	//Ignore
			} catch (SAXException e) {
				Log.e(TAG,e.getMessage());
			}
			return Collections.emptyList();
			
		}
		
		protected void onPostExecute(java.util.List<SongInfo> result) {
			Log.i(TAG,"Updating last played songs");
			adapter.setNotifyOnChange(false);			
			int count = adapter.getCount();	
			ArrayList<SongInfo> removeList = new ArrayList<SongInfo>();
			for(int i=0; i!=count;++i) {
				SongInfo item = adapter.getItem(i);
				if(!result.contains(item))
					removeList.add(item);
			}
			for (SongInfo item : removeList) {
				adapter.remove(item);
			}
			for (SongInfo songInfo : result) {
				if(adapter.getPosition(songInfo)<0)
					adapter.add(songInfo);
			}
			adapter.sort(SongInfo.getComparator());
			adapter.notifyDataSetChanged();
		};		
	}
		
	public class LastPlayedSongsAdapter extends ArrayAdapter<SongInfo> {
		
		public LastPlayedSongsAdapter(Context context, int textViewResourceId, List<SongInfo> objects) {
			super(context, textViewResourceId, objects);	
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.song_info_view, null);
                Log.i(TAG,"Created view for "+position);
            }
            Log.i(TAG,"GetView "+position);
            SongInfo item = getItem(position);
            if (item != null) {
                    TextView artistName = (TextView) v.findViewById(R.id.songInfoView_artistName);
                    TextView timePlayed = (TextView) v.findViewById(R.id.songInfoView_timePlayed);
                    TextView songName = (TextView) v.findViewById(R.id.songInfoView_songName);
                    ImageView albumArt = (ImageView) v.findViewById(R.id.songInfoView_albumArt);
                    if (artistName != null) {
                    	artistName.setText(Html.fromHtml(item.getArtist().toUpperCase()));
                    }
                    if(timePlayed!=null){
                    	timePlayed.setText(item.getTimePlayed());
                    }
                    if(songName!=null){
                    	songName.setText(Html.fromHtml(item.getSongName()));
                    }
                    if(albumArt!=null) {
                    	if(item.getAlbumCoverBitmap()!=null) {
                    		Log.d(TAG,"Reusing album art");
                    		albumArt.setImageBitmap(item.getAlbumCoverBitmap());
                    	}
                    	else {                    		
                    		albumArt.setImageResource(R.drawable.capa_indisponivel);
                    		synchronized (item) {
                    			if(item.getAlbumCover()!=null && item.isAlbumCoverNeedsLoading()) {
    	                    		Log.d(TAG,"Downloading album art");
    	                    		item.setAbumCoverNeedsLoading(false);
    	                    		new DownloadLastPlayedSongCoverTask(item).execute(item.getAlbumCover());
    	                    	}
							}                    		
                    	}
                    }                    
            }
            return v;
		}
		
	}
}