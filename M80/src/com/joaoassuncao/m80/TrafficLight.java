package com.joaoassuncao.m80;

public enum TrafficLight {		
	
	GreenLight(R.drawable.green_light), 
	YellowLight(R.drawable.yellow_light), 
	RedLight(R.drawable.red_light), 
	Unknown(R.drawable.gray_light);
	
	private final int resourceId;
	
	private TrafficLight(int resourceId){
		this.resourceId = resourceId;
	}
	
	public int getResource() { 
		return resourceId;
	} 
}
