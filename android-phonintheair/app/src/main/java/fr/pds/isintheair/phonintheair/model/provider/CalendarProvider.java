package fr.pds.isintheair.phonintheair.model.provider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.pds.isintheair.phonintheair.model.entity.Agenda;
import fr.pds.isintheair.phonintheair.model.entity.Event;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CalendarProvider {
    public static final String[] AGENDA_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.OWNER_ACCOUNT
    };

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
    };

    private static final int AGENDA_PROJECTION_ID_INDEX            = 0;
    private static final int AGENDA_PROJECTION_DISPLAY_NAME_INDEX  = 1;
    private static final int AGENDA_PROJECTION_OWNER_ACCOUNT_INDEX = 2;

    private static final int EVENT_PROJECTION_TITLE_INDEX  = 0;
    private static final int EVENT_PROJECTION_DSTART_INDEX = 1;
    private static final int EVENT_PROJECTION_DTEND_INDEX  = 2;

    private Context context;

    public CalendarProvider(Context context) {
        this.context = context;
    }

    public List<Agenda> getAgendas(String account) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            List<Agenda> agendas = new ArrayList<>();
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            Cursor cursor = contentResolver.query(uri, AGENDA_PROJECTION, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Agenda agenda = new Agenda();

                    agenda.setId(cursor.getLong(AGENDA_PROJECTION_ID_INDEX));
                    agenda.setName(cursor.getString(AGENDA_PROJECTION_DISPLAY_NAME_INDEX));
                    agenda.setOwner(cursor.getString(AGENDA_PROJECTION_OWNER_ACCOUNT_INDEX));

                    agendas.add(agenda);
                }

                cursor.close();
            }

            return agendas;
        }

        return null;
    }

    public Agenda getAgendaById(Long id) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Agenda agenda = new Agenda();
            ContentResolver contentResolver = context.getContentResolver();
            String selection = "( " + CalendarContract.Calendars._ID + " = " + " ? )";
            String[] selectionArgs = new String[]{String.valueOf(id)};
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            Cursor cursor = contentResolver.query(uri, AGENDA_PROJECTION, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                agenda.setId(cursor.getLong(AGENDA_PROJECTION_ID_INDEX));
                agenda.setName(cursor.getString(AGENDA_PROJECTION_DISPLAY_NAME_INDEX));
                agenda.setOwner(cursor.getString(AGENDA_PROJECTION_OWNER_ACCOUNT_INDEX));

                cursor.close();
            }

            return agenda;
        }

        return null;
    }

    public List<Event> getEvents(Long agendaId) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            List<Event> events = new ArrayList<>();
            String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + " ? ) AND (" + CalendarContract.Events.CALENDAR_ID + " = ?))";
            String[] selectionArgs = new String[]{String.valueOf(Calendar.getInstance().getTimeInMillis()), String.valueOf(agendaId)};
            Uri uri = CalendarContract.Events.CONTENT_URI;
            Cursor cursor = context.getContentResolver().query(uri, EVENT_PROJECTION, selection, selectionArgs, CalendarContract.Events.DTSTART + " ASC");

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String startTimeData = cursor.getString(EVENT_PROJECTION_DSTART_INDEX);
                    String endTimeData = cursor.getString(EVENT_PROJECTION_DTEND_INDEX);

                    if (startTimeData != null && endTimeData != null) {
                        Event event = new Event();

                        event.setTitle(cursor.getString(EVENT_PROJECTION_TITLE_INDEX));

                        Calendar start = Calendar.getInstance();
                        Calendar end = Calendar.getInstance();

                        start.setTimeInMillis(Long.parseLong(startTimeData));
                        end.setTimeInMillis(Long.parseLong(endTimeData));

                        event.setStartTime(start);
                        event.setEndTime(end);

                        events.add(event);
                    }
                }

                cursor.close();
            }

            return events;
        }

        return null;
    }
}
