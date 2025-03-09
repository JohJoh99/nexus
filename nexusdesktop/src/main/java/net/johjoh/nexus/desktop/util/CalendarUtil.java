package net.johjoh.nexus.desktop.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javafx.application.Platform;
import net.johjoh.nexus.cloud.api.packet.data.PacketClientDataUpdate;
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

		/*Platform.runLater(() -> {
	    	NexusDesktop.getCalendarPane().getCalendarSources().addAll(own, family);
		});*/
	}
	
	public static void loadCalendarFromNode(JsonNode jsonNode) {
		
    	JsonNode head = jsonNode.get("headcontent");
    	JsonNode lines = jsonNode.get("linecontent");
    	
    	int headId = head.get("id").asInt();
    	String calendarTitle = head.get("title").asText();
    	int ownerId = head.get("ownerId").asInt();

		Calendar<String> calendar = new Calendar<String>(calendarTitle);
		
        /*calendar.addEventHandler(CalendarEvent.ENTRY_CHANGED, event -> {
            Entry<?> entry = event.getEntry();
            saveEntry(entry);
        });*/
		
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
	
    private static void saveEntry(Entry<?> entry) {

    	ObjectNode rootNode = null;
    	
        try {
            // Erstelle einen ObjectMapper
            ObjectMapper mapper = new ObjectMapper();

            // Erstelle das root ObjectNode
            rootNode = mapper.createObjectNode();
            rootNode.put("table", "calendar");
            rootNode.put("type", "line");

            // Erstelle das content ObjectNode
            ObjectNode contentNode = mapper.createObjectNode();
            contentNode.put("id", Integer.valueOf(entry.getId().split("_")[1]));
            contentNode.put("name", entry.getTitle());
            contentNode.put("place", entry.getLocation());
            contentNode.put("all_day", entry.isFullDay());

            // Konvertiere LocalDateTime in das gewünschte Format
            LocalDateTime startDateTime = LocalDateTime.of(2025, 2, 28, 14, 54, 59);
            LocalDateTime endDateTime = LocalDateTime.of(2025, 2, 28, 14, 54, 59);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            contentNode.put("start_datetime", startDateTime.format(formatter));
            contentNode.put("end_datetime", endDateTime.format(formatter));

            // Füge das content ObjectNode zum rootNode hinzu
            rootNode.set("content", contentNode);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
    	PacketClientDataUpdate pcdu = new PacketClientDataUpdate(NexusDesktop.getCloudClient().getSessionId(), rootNode.toString());
    	NexusDesktop.getCloudClient().sendPacket(pcdu);
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
