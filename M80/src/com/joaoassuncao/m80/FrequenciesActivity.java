/**
 * 
 */
package com.joaoassuncao.m80;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jassuncao
 *
 */
public class FrequenciesActivity extends ListActivity implements LocationListener{
	
	private enum SortingOrder {Alphabetically, Distance};
	
	private static final String TAG = "M80";
	private static final String FREQUENCIES_ORDER_KEY = "frequencies_order";
	private List<Frequency> frequencies = new ArrayList<Frequency>();
	private ArrayAdapter<Frequency> listAdapter;
	private SortingOrder sortingOrder = SortingOrder.Distance;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        if(savedInstanceState!=null){
        	try{
        		sortingOrder = SortingOrder.valueOf(savedInstanceState.getString(FREQUENCIES_ORDER_KEY));
        	}
        	catch(IllegalArgumentException e){
        	}
        }
        setContentView(R.layout.frequencies);  
        try {
        	frequencies = loadFrequencies();
		} catch (Throwable e) {
			Log.e(TAG,"Unable to load frequencies file",e);       	
			String errorMsg = getResources().getString(R.string.frequenciesLoadingError);
			Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
		}
        listAdapter = new FrequenciesListAdapter(this, android.R.layout.simple_list_item_1,frequencies);
        setListAdapter(listAdapter);       
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10000, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);   
        if(location!=null){
        	updateDistances(location);
        } 
        sortItems();
    }
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);		
		Log.i(TAG,"onSaveInstanceState");
		outState.putString(FREQUENCIES_ORDER_KEY,sortingOrder.name());
	};
	
	public void sortItems(){
		switch(sortingOrder){
		case Alphabetically:
			Collections.sort(frequencies,new DefaultFrequencyComparator());
			break;
		default:
			Collections.sort(frequencies,new DistanceFrequencyComparator());
			break;
		}
		listAdapter.notifyDataSetChanged();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {    	
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.frequencies_menu, menu);
	    return true;    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_sort_by_distance: 
            	if(sortingOrder!=SortingOrder.Distance){
            		sortingOrder=SortingOrder.Distance;
            		sortItems();
            	}            	
                return true;          
            case R.id.menu_sort_by_name:
            	if(sortingOrder!=SortingOrder.Alphabetically){
            		sortingOrder=SortingOrder.Alphabetically;
            		sortItems();
            	} 
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	private void updateDistances(Location location) {
		double lat = location.getLatitude();
		double lon = location.getLongitude();
		for (Frequency it : frequencies){
			double distance = calcDistance(lat,lon, it.getLatitude(), it.getLongitude());
			it.setDistance(distance);
		}				
	}	
	
	private static double calcDistance(double originLat, double originLogt, double otherLat, double otherLongt){		
		double dx = originLat-otherLat;
		double dy = originLogt-otherLongt;
		return Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
	}
	
	private List<Frequency> loadFrequencies() throws NumberFormatException, XmlPullParserException, IOException{
		ArrayList<Frequency> items = new ArrayList<Frequency>();
		XmlPullParser xpp=getResources().getXml(R.xml.frequencies);
	      
	      while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
	        if (xpp.getEventType()==XmlPullParser.START_TAG) {
	          if (xpp.getName().equals("frequency")) {
	        	  String value = xpp.getAttributeValue(null,"value");
	        	  String name = xpp.getAttributeValue(null,"name");
	        	  String coordinates = xpp.getAttributeValue(null,"coordinates");
	        	  int sepIdx = coordinates.indexOf(',');
	        	  String latitudeStr = coordinates.substring(0,sepIdx).trim();
	        	  String longitudeStr = coordinates.substring(sepIdx+1).trim();
	        	  float latitude = Float.parseFloat(latitudeStr);
	        	  float longitude = Float.parseFloat(longitudeStr);
	        	  Frequency item = new Frequency(value, name, latitude, longitude);
	        	  items.add(item);
	          }
	        }	        
	        xpp.next();
	      }
	      return items;
	}
	
    @Override
    protected void onStart() {  
    	super.onStart();
    }

	public void onLocationChanged(Location location) {
		if(location!=null){
			Log.i(TAG,"Location changed");
			updateDistances(location);
		}			
	}

	public void onProviderDisabled(String provider) {				
	}

	public void onProviderEnabled(String provider) {		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {		
	}			
	
	/**
	 * Compares alphabetically the name of the frequency
	 * @author jassuncao
	 *
	 */
	private static class DefaultFrequencyComparator implements Comparator<Frequency>{
		private Collator collator;
		
		public DefaultFrequencyComparator() {
			collator = Collator.getInstance(new Locale("pt_PT"));
			collator.setStrength(Collator.SECONDARY);
		}		
		
		public int compare(Frequency lhs, Frequency rhs) {
			return collator.compare(lhs.getName(), rhs.getName());
		}		
	}
	
	private static class DistanceFrequencyComparator extends DefaultFrequencyComparator{

		public int compare(Frequency lhs, Frequency rhs) {
			int aux = Double.compare(lhs.getDistance(), rhs.getDistance()); 
			if(aux==0)
				return super.compare(lhs,rhs);				
			return aux;
		}		
	}
	
	public class FrequenciesListAdapter extends ArrayAdapter<Frequency> {
		
		public FrequenciesListAdapter(Context context, int textViewResourceId, List<Frequency> objects) {
			super(context, textViewResourceId, objects);	
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.frequency_view, null);                
            }            
            Frequency item = getItem(position);
            if (item != null) {
                    TextView nameView = (TextView) v.findViewById(R.id.frequencyView_name);
                    TextView valueView = (TextView) v.findViewById(R.id.frequencyView_value);                   
                    if (nameView != null) {
                    	nameView.setText(item.getName());
                    }
                    if(valueView!=null){
                    	valueView.setText(item.getValue());
                    }
            }
            return v;
		}
		
	}

}
