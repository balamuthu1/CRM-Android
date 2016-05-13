package fr.pds.isintheair.crmtab;

import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.pds.isintheair.crmtab.controller.message.CalendarMessageController;
import fr.pds.isintheair.crmtab.model.dao.EventDAO;
import fr.pds.isintheair.crmtab.model.entity.CalendarMessage;
import fr.pds.isintheair.crmtab.model.entity.Event;
import fr.pds.isintheair.crmtab.model.entity.MessageInfo;
import fr.pds.isintheair.crmtab.model.entity.MessageType;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/23/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class CalendarMessageControllerTest {
    @Test
    public synchronized void testIfDatabaseIsFilledCorrectly() {
        CalendarMessage calendarMessage = Mockito.mock(CalendarMessage.class);
        MessageInfo     messageInfo     = Mockito.mock(MessageInfo.class);
        List<Event>     events          = new ArrayList<>();

        Event goodResult = new Event();

        goodResult.setTitle("OK");
        goodResult.setStartTime(Calendar.getInstance());
        goodResult.setEndTime(Calendar.getInstance());

        events.add(goodResult);

        Event badResult = new Event();

        badResult.setTitle("KO");
        badResult.setStartTime(Calendar.getInstance());
        badResult.setEndTime(Calendar.getInstance());

        badResult.save();

        Mockito.doReturn(MessageType.CALENDAR_FULL_SYNC).when(messageInfo).getMessageType();
        Mockito.doReturn(messageInfo).when(calendarMessage).getMessageInfo();
        Mockito.doReturn(events).when(calendarMessage).getEvents();

        CalendarMessageController.handleMessage(calendarMessage);

        List<Event> eventsInDatabase = EventDAO.getAll();

        Assert.assertTrue(eventsInDatabase.size() != 0);
        Assert.assertEquals(eventsInDatabase.get(0).getTitle(), "OK");
    }

    @After
    public void tearDown() {
        FlowManager.destroy();
    }
}
