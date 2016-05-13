package fr.pds.isintheair.notifier.entity;

import java.util.List;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CalendarMessage extends Message {
    private List<Event> events;

    protected CalendarMessage (Builder builder) {
        super(builder);

        this.events = builder.events;
    }

    public List<Event> getEvents () {
        return events;
    }

    public static class Builder extends Message.Builder<Builder> {
        public List<Event> events;

        @Override
        public Builder getThis () {
            return this;
        }

        public Builder addEvents (List<Event> events) {
            this.events = events;

            return this;
        }

        public CalendarMessage build () {
            return new CalendarMessage(this);
        }
    }
}