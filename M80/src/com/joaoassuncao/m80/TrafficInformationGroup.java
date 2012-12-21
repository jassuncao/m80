/**
 * 
 */
package com.joaoassuncao.m80;

import java.util.List;

/**
 * @author jassuncao
 *
 */
public class TrafficInformationGroup {
	
	private final String groupName;
	private final List<TrafficInformationItem> items;
	
	public TrafficInformationGroup(String groupName, List<TrafficInformationItem> items) {
		this.groupName = groupName;
		this.items =  items;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public TrafficInformationItem getItem(int index) {
		return items.get(index);
	}

	public int itemsCount() {		
		return items.size();
	}
	
	@Override
	public String toString() {
		return groupName;
	}

}
