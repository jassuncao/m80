/**
 * 
 */
package com.joaoassuncao.m80;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jassuncao
 *
 */
public class TrafficInformationAdapter extends BaseExpandableListAdapter {

	private List<TrafficInformationGroup> groups = Collections.emptyList();
	private final Context context;
	private final LayoutInflater inflater;

	public TrafficInformationAdapter(Context context) {
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public TrafficInformationItem getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getItem(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).itemsCount();
	}

	/*
	public TextView getGenericView() {
		// Layout parameters for the ExpandableListView
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);		
		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		// Center the text vertically
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		// Set the text starting position
		textView.setPadding(48, 0, 0, 0);
		return textView;
	}
	*/	

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {			
			//LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.traffic_info_view, null);
		}
		TrafficInformationItem item = getChild(groupPosition, childPosition);
		if (item != null) {
			ImageView iconView = (ImageView) v.findViewById(R.id.trafficInfoView_icon);
			TextView localeView = (TextView) v.findViewById(R.id.trafficInfoView_locale);			
			if (localeView != null) {
				localeView.setText(item.getLocale());
			}
			/*
			TextView messageView = (TextView) v.findViewById(R.id.trafficInfoView_message);
			if (messageView != null) {
				messageView.setText(item.getMessage());
			}
			*/
			if(iconView!=null){
				int resource;
				switch (item.getLight()) {
				case GreenLight:
					resource = R.drawable.green_light;
					break;
				case YellowLight:
					resource = R.drawable.yellow_light;
					break;
				case RedLight:
					resource = R.drawable.red_light;
					break;
				default:
					resource = R.drawable.gray_light;
					break;
				}				
				iconView.setImageResource(resource);
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
		TextView textView = (TextView)convertView;
		if(textView == null){
			textView=new TextView(context);
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);		
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			textView.setPadding(56, 0, 0, 0);
			textView.setTypeface(Typeface.DEFAULT_BOLD);
			textView.setTextSize(24);
		}
		//row.setTypeface(Typeface.DEFAULT_BOLD);
		//row.setTextSize(16);						
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
