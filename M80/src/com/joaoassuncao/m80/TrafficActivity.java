/**
 * 
 */
package com.joaoassuncao.m80;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author jassuncao
 *
 */
public class TrafficActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the TrafficActivity tab");
        setContentView(textview);
    }

}