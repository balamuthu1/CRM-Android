package fr.pds.isintheair.crmtab.helper;

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.model.entity.Event;

/******************************************
 * Created by        :                    *
 * Creation date     : 03/20/16            *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CalendarHelper {
    public static List<CalendarEvent> createBaseEventList(List<Event> events) {
        List<CalendarEvent> calendarEvents = new ArrayList<>();

        for (Event event : events) {
            Integer color = ResourceHelper.getColor(R.color.blue_selected);
            Calendar endTime = event.getEndTime();
            Calendar startTime = event.getStartTime();
            String title = event.getTitle();
            BaseCalendarEvent baseCalendarEvent = new BaseCalendarEvent(title, "Descr", "Iceland", color, startTime, endTime, true);

            calendarEvents.add(baseCalendarEvent);
        }

        return calendarEvents;
    }
}
