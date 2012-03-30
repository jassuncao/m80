/**
 * 
 */
package com.joaoassuncao.m80;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * @author jassuncao
 *
 */
public class MainActivity extends TabActivity{
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    Resources res = getResources(); 
	    TabHost tabHost = getTabHost(); 
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, NowPlayingActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("nowPlaying").setIndicator(res.getText(R.string.nowPlayingLabel),res.getDrawable(R.drawable.tab_now_playing))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, TrafficActivity.class);
	    spec = tabHost.newTabSpec("traffic").setIndicator(res.getText(R.string.traffic_label),res.getDrawable(R.drawable.tab_traffic))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, FrequenciesActivity.class);
	    spec = tabHost.newTabSpec("frequencies").setIndicator(res.getText(R.string.frequencies_label),res.getDrawable(R.drawable.tab_frequencies))
	                  .setContent(intent);
	    tabHost.addTab(spec);	

	    tabHost.setCurrentTab(0);
	}

}
