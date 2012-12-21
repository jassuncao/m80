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

import android.app.ExpandableListActivity;
import android.content.Context;
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
import android.widget.ImageView;
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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new TrafficInformationAdapter(this);
		setListAdapter(adapter);
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
	
	
	/*
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
		private List<TrafficInformationGroup> groups = Collections.emptyList();

		public TrafficInformationItem getChild(int groupPosition, int childPosition) {
			return groups.get(groupPosition).getItem(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return groups.get(groupPosition).itemsCount();
		}

		public TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);

			TextView textView = new TextView(TrafficActivity.this);
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			textView.setPadding(36, 0, 0, 0);
			return textView;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.traffic_info_view, null);
			}
			TrafficInformationItem item = getChild(groupPosition, childPosition);
			if (item != null) {
				ImageView iconView = (ImageView) v
						.findViewById(R.id.trafficInfoView_icon);
				TextView localeView = (TextView) v
						.findViewById(R.id.trafficInfoView_locale);
				TextView messageView = (TextView) v
						.findViewById(R.id.trafficInfoView_message);
				if (localeView != null) {
					localeView.setText(item.getLocale());
				}
				if (messageView != null) {
					messageView.setText(item.getMessage());
				}
			}
			return v;
		}

		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		public int getGroupCount() {
			return groups.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

		public void setData(List<TrafficInformationGroup> data) {
			this.groups = data;
			notifyDataSetChanged();
		}
	}
	*/

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
