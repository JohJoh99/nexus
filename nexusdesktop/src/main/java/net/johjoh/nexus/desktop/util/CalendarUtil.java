package net.johjoh.nexus.desktop.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.fasterxml.jackson.databind.JsonNode;

import net.johjoh.nexus.desktop.NexusDesktop;

public class CalendarUtil {
	
	private static HashMap<Integer, CalendarSource> calendarSources;
	
	public static Collection<CalendarSource> getCalendarSources() {
		return calendarSources.values();
	}
	
	public static void initCalendars() {
		calendarSources = new HashMap<Integer, CalendarSource>();
    	
    	CalendarSource own = new CalendarSource("Eigene Kalender");
    	CalendarSource family = new CalendarSource("Familienkalender");
    	calendarSources.put(0, own);
    	calendarSources.put(1, family);
    	
    	//NexusDesktop.getCalendarPane().getCalendarSources().addAll(own, family);
	}
	
	public static void loadCalendarFromNode(JsonNode jsonNode) {
		
    	JsonNode head = jsonNode.get("headcontent");
    	JsonNode lines = jsonNode.get("linecontent");
    	
    	int headId = head.get("id").asInt();
    	String calendarTitle = head.get("title").asText();
    	int ownerId = head.get("ownerId").asInt();

		Calendar<String> calendar = new Calendar<String>(calendarTitle);
		
        if (lines.isArray()) {
            for (JsonNode node : lines) {
                int id = node.get("id").asInt();
                String name = node.get("name").asText();
                boolean allDay = node.get("all_day").asBoolean();
                
                Entry<String> entry = new Entry<String>(name);
                entry.setFullDay(allDay);
                entry.setId(headId + "_" + id);
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                LocalDateTime startDateTime = LocalDateTime.parse(node.get("start_datetime").asText(), formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(node.get("end_datetime").asText(), formatter);
                
                entry.changeStartDate(startDateTime.toLocalDate());
                entry.changeStartTime(startDateTime.toLocalTime());
                entry.changeEndDate(endDateTime.toLocalDate());
                entry.changeEndTime(endDateTime.toLocalTime());
                
                //	TODO: Wiederholung hinzufügen
                
                calendar.addEntries(entry);
            }
        }
    	
    	if(ownerId == NexusDesktop.USERID) {
    		calendarSources.get(0).getCalendars().add(calendar);
    	}
    	else {
    		calendarSources.get(1).getCalendars().add(calendar);
    	}
    	

        
	}
	
	
	/*CalendarSource myCalendarSource = new CalendarSource("My Calendars");
	
	public static CalendarSource loadCalendarSource() {
		Calendar<String> birthdays = new Calendar("Geburtstage");
		Calendar trash = new Calendar("Mülltermine");
		
		birthdays.setStyle(Style.STYLE1);
		trash.setStyle(Style.STYLE2);
		
		Entry<String> e = new Entry<String>("JOSF");
		e.setFullDay(true);
		
		birthdays.addEntries(e);
		
		CalendarSource myCalendarSource = new CalendarSource("Meine Kalender");
        myCalendarSource.getCalendars().addAll(birthdays, trash);
        
        return null;
	}*/

}
