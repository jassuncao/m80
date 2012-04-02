/**
 * 
 */
package com.joaoassuncao.m80;

/**
 * @author jassuncao
 *
 */
public class Frequency implements Comparable<Frequency>{
	private final String value;
	private final String name;
	private final float latitude;
	private final float longitude;
	private double distance;
	
	public Frequency(String value, String name, float latitude, float longitude) {
		this.value = value;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}	

	public int compareTo(Frequency another) {
		return name.compareTo(another.name);
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return name + " "+value;
	}
	
}
