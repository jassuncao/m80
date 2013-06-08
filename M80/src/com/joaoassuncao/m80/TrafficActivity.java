/**
 * 
 */
package com.joaoassuncao.m80;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jassuncao
 * 
 */
public class TrafficActivity extends ExpandableListActivity {

	private static final String TAG = "M80";
	private static URL feedUrl;
	
	static {
		try {
			feedUrl = new URL("http://m80.clix.pt/info/transito.html");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private TrafficInformationAdapter adapter;
	private Timer timer;
	private FrameLayout frameLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traffic_info_view);
		adapter = new TrafficInformationAdapter(this);
		setListAdapter(adapter);	
		frameLayout = (FrameLayout) findViewById(R.id.traffic_info_view);
		frameLayout.getForeground().setAlpha(0);
	}	

	@Override
	protected void onStart() {
		super.onStart();
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					retrieveTrafficInfo();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}, 500, 60000);
		Log.d(TAG, "Started");
	}	

	@Override
	protected void onStop() {
		super.onStop();
		timer.cancel();
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {		
		TrafficInformationItem item = adapter.getChild(groupPosition,childPosition);
		if(item!=null)
			showPopup(item);
		return true;		
	}
	
	private void showPopup(TrafficInformationItem item){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(item.getLocale());
		builder.setMessage(item.getMessage());
		builder.setIcon(item.getLight().getResource());		
		builder.setPositiveButton("OK",
		  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {		    
		   }
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	protected void retrieveTrafficInfo() {
    	Log.d(TAG,"retrieveTrafficInfo");
    	RetrieveTrafficInfoTask task = new RetrieveTrafficInfoTask(adapter);
    	task.execute();
    	/*
    	TrafficInformationParser parser = new TrafficInformationParser("http://m80.clix.pt/info/transito.html");    	    	
		try {
			List<TrafficInformationGroup> info = parser.parse();
			adapter.setData(info);
		} catch (Throwable e) {
			Log.e(TAG,"Failed to retrieve traffic info",e);       	
			String errorMsg = getResources().getString(R.string.trafficLoadingError);
			Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
		}
		*/
	}	
			

	private class RetrieveTrafficInfoTask extends AsyncTask<Void, Void, List<TrafficInformationGroup>> {

		private final TrafficInformationAdapter adapter;

		public RetrieveTrafficInfoTask(TrafficInformationAdapter adapter) {
			this.adapter = adapter;
		}

		@Override
		protected List<TrafficInformationGroup> doInBackground(Void... params) {
			try {
				
				TrafficInformationParser parser = new TrafficInformationParser(feedUrl);
				return parser.parse();
			} catch (Throwable e) {
				Log.e(TAG,"Failed to retrieve traffic info",e);       	
				String errorMsg = getResources().getString(R.string.trafficLoadingError);
				//Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
			}
			return Collections.emptyList();

		}

		protected void onPostExecute(List<TrafficInformationGroup> result) {
			adapter.setData(result);
		};
	}

}
