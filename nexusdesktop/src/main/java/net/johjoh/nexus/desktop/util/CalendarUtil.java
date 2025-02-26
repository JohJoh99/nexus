package net.johjoh.nexus.desktop.util;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;

public class CalendarUtil {
	
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
	}

}
