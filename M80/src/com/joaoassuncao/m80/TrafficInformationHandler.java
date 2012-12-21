/**
 * 
 */
package com.joaoassuncao.m80;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author jassuncao
 *
 */
public class TrafficInformationHandler extends DefaultHandler {
	
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	private enum CharacterCollectState {Ignore, Location, Locale, Message, Hour, Date}
	
	private CharacterCollectState characterCollectState = CharacterCollectState.Ignore;
	private StringBuilder charactersCollector = new StringBuilder();
	private ArrayList<TrafficInformationItem> items;// = new ArrayList<TrafficInformationItem>();
	private List<TrafficInformationGroup> groups = new ArrayList<TrafficInformationGroup>(16);
	private String groupName = null;
	private boolean itemActive = false;
	private TrafficLight trafficLight;
	private String message;
	private String locale;
	private String hour;
	private String date;
	
	
	@Override
	public void endDocument() throws SAXException {
		closeCurrentGroup();
	}
	
	private void closeCurrentGroup(){
		if(itemActive){
			storeCurrentItem();			
		}
		if(groupName!=null && items.size()>0){
			Collections.sort(items);
			TrafficInformationGroup group = new TrafficInformationGroup(groupName, items);			
			groups.add(group);			
		}
		groupName=null;		
		items=null;
	}
	
	private void storeCurrentItem(){
		if(!itemActive) return;
		Date dateTime;
		try {
			dateTime = dateTimeFormat.parse(date+" "+hour);
		} catch (ParseException e) {
			dateTime = new Date();
		}
		if(locale!=null)
			locale = trim(locale);
		TrafficInformationItem item = new TrafficInformationItem(trafficLight, locale, message, dateTime);
		items.add(item);
		itemActive = false;
	}
	
	private static String trim(String original){
		String trimmed = original.trim();
		int idx =trimmed.length();
		while(idx>0){
			if(Character.isLetterOrDigit(trimmed.charAt(idx-1))) break;			
			idx--;
		}
		if(idx==trimmed.length())
			return trimmed;
		return trimmed.substring(0,idx);
	}
	
	private void initNewItem(){
		trafficLight = TrafficLight.Unknown;
		itemActive = true;
	}
	
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
		if("div".equals(localName) || "span".equals(localName)){
			String classAttr = attributes.getValue("class");
			if(classAttr==null){ 
				return;
			}
			if("traffic-index-info".equals(classAttr)){
				storeCurrentItem();
				initNewItem();				
			}
			else if("trafficLocale".equals(classAttr)){
				characterCollectState = CharacterCollectState.Locale;
			}
			else if("trafficHour".equals(classAttr)){
				message = charactersCollector.toString();
				charactersCollector.setLength(0);
				characterCollectState = CharacterCollectState.Hour;
			}
			else if("trafficDate".equals(classAttr)){
				characterCollectState = CharacterCollectState.Date;
			}
			else if("trafficLight-green".equals(classAttr)){
				trafficLight = TrafficLight.GreenLight;				
			}
			else if("trafficLight-yellow".equals(classAttr)){
				trafficLight = TrafficLight.YellowLight;
			}
			else if("trafficLight-red".equals(classAttr)){
				trafficLight = TrafficLight.RedLight;
			}			
			else if("trafficLocation".equals(classAttr)){				
				processTrafficLocation();
			}
		}		
	};
	
	private void processTrafficLocation() {
		closeCurrentGroup();
		items = new ArrayList<TrafficInformationItem>();
		characterCollectState = CharacterCollectState.Location;
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {		
		if("span".equals(localName)){
			switch(characterCollectState){			
			case Locale:
				locale = charactersCollector.toString();
				characterCollectState=CharacterCollectState.Message;
				break;
			case Hour:
				hour = charactersCollector.toString();
				characterCollectState=CharacterCollectState.Ignore;
				break;
			case Date:
				date = charactersCollector.toString();
				characterCollectState=CharacterCollectState.Ignore;
				break;
			case Message:
				message = charactersCollector.toString();
				characterCollectState=CharacterCollectState.Ignore;
				break;
			}
			charactersCollector.setLength(0);
						
		}
		else if ("div".equals(localName)){
			if(characterCollectState == CharacterCollectState.Location){
				groupName = charactersCollector.toString();
				characterCollectState=CharacterCollectState.Ignore;
				charactersCollector.setLength(0);
			}
		}
	};
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(characterCollectState!=CharacterCollectState.Ignore){
			charactersCollector.append(ch,start,length);
		}
	}

	public List<TrafficInformationGroup> getResult() {
		return groups;
	};

}
