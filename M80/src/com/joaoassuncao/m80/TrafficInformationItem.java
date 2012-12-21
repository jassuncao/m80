/**
 * 
 */
package com.joaoassuncao.m80;

import java.util.Date;

/**
 * @author jassuncao
 *
 */
public class TrafficInformationItem implements Comparable<TrafficInformationItem>{
	
	private final TrafficLight light;
	private final String locale;
	private final String message;
	private final Date dateTime;
	
	public TrafficInformationItem(TrafficLight light, String locale, String message, Date dateTime) {
		this.light = light;
		this.locale = locale;
		this.message = message;
		this.dateTime = dateTime;
		if(locale==null) throw new IllegalArgumentException("locale can not be null");
		if(dateTime==null) throw new IllegalArgumentException("dateTime can not be null");
	}

	public TrafficLight getLight() {
		return light;
	}

	public String getLocale() {
		return locale;
	}

	public String getMessage() {
		return message;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public int compareTo(TrafficInformationItem another) {
		int i = locale.compareTo(another.locale);
		if(i==0)
			return light.compareTo(another.light);
		return i;
	}
	
	

}
