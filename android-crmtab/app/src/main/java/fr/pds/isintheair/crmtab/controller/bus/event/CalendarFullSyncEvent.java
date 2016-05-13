package fr.pds.isintheair.crmtab.controller.bus.event;

import java.util.List;

import fr.pds.isintheair.crmtab.model.entity.Event;

/******************************************
 * Created by        :                    *
 * Creation date     : 03/20/16            *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CalendarFullSyncEvent {
    List<Event> events;

    public CalendarFullSyncEvent(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }
}
