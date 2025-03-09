package net.johjoh.nexus.desktop.panes.calendar;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.view.CalendarView;

import javafx.application.Platform;
import net.johjoh.nexus.desktop.util.CalendarUtil;

public class CalendarPane extends CalendarView {
	
	public CalendarPane() {
		
		//getStylesheets().add(getClass().getResource("/darkmode.css").toExternalForm());
		//getStylesheets().add(getClass().getResource("/calendar_dark_mode.css").toExternalForm());

		//Calendar birthdays = new Calendar("Birthdays");
		//Calendar holidays = new Calendar("Holidays");

		//birthdays.setStyle(Style.STYLE1);
		//holidays.setStyle(Style.STYLE7);

		//CalendarSource myCalendarSource = new CalendarSource("My Calendars");
		//myCalendarSource.getCalendars().addAll(birthdays, holidays);

		getCalendarSources().clear();
		getCalendarSources().addAll(CalendarUtil.getCalendarSources());

		setRequestedTime(LocalTime.now());

		Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
						setToday(LocalDate.now());
						setTime(LocalTime.now());
					});

					try {
						// update every 10 seconds
						sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		};

		updateTimeThread.setPriority(Thread.MIN_PRIORITY);
		updateTimeThread.setDaemon(true);
		updateTimeThread.start();
	}

}
