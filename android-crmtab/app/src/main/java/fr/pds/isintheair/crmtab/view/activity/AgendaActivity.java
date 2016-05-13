package fr.pds.isintheair.crmtab.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.squareup.otto.Subscribe;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.bus.BusHandlerSingleton;
import fr.pds.isintheair.crmtab.controller.bus.event.CalendarFullSyncEvent;
import fr.pds.isintheair.crmtab.controller.message.CalendarMessageController;
import fr.pds.isintheair.crmtab.helper.CalendarHelper;
import fr.pds.isintheair.crmtab.model.dao.EventDAO;
import fr.pds.isintheair.crmtab.model.entity.Event;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class AgendaActivity extends AppCompatActivity implements CalendarPickerController {
    @Bind(R.id.agenda_calendar_view)
    AgendaCalendarView agendaCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        ButterKnife.bind(this);
        BusHandlerSingleton.getInstance().getBus().register(this);

        List<Event> events = EventDAO.getAll();

        refreshCalendar(events);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_agenda_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            CalendarMessageController.sendFullSyncMessage();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshCalendar(List<Event> events) {
        List<CalendarEvent> baseCalendarEvents = CalendarHelper.createBaseEventList(events);
        Calendar            minDate            = Calendar.getInstance();
        Calendar            maxDate            = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        agendaCalendarView.init(baseCalendarEvents, minDate, maxDate, Locale.getDefault(), this);
    }

    @Subscribe
    public void onFullSyncEvent(CalendarFullSyncEvent calendarFullSyncEvent) {
        refreshCalendar(calendarFullSyncEvent.getEvents());
    }

    @Override
    public void onDaySelected(DayItem dayItem) {
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
    }
}
