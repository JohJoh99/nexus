package net.johjoh.nexus.desktop.panes.mainmenu;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.DayView;
import com.calendarfx.view.MonthView;

import javafx.application.Platform;

public class CalendarFrame extends MenuFrame {
	
	private MonthView dayView;
	
	public CalendarFrame() {
		super("Kalender", "a", 300);
		
		dayView = new MonthView();
		this.getChildren().add(dayView);
		


		Calendar birthdays = new Calendar("Birthdays");
		Calendar holidays = new Calendar("Holidays");

		//birthdays.setStyle(Style.STYLE1);
		//holidays.setStyle(Style.STYLE2);

		CalendarSource myCalendarSource = new CalendarSource("My Calendars");
		myCalendarSource.getCalendars().addAll(birthdays, holidays);

		dayView.getCalendarSources().addAll(myCalendarSource);

		dayView.setRequestedTime(LocalTime.now());

		Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
						dayView.setToday(LocalDate.now());
						dayView.setTime(LocalTime.now());
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
