package fr.pds.isintheair.notifier;

import fr.pds.isintheair.notifier.server.ProspectNotifEndPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.logging.Logger;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

/**
 * Created by Truong on 3/22/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ProspectNotifEndPoint.class, Logger.class})
public class ProspectNotifEndPointTest {

    @Test
    public void onOpen () throws Exception {
        PowerMockito.mockStatic(ProspectNotifEndPoint.class);
        PowerMockito.mockStatic(Logger.class);

        Logger logger = mock(Logger.class);
        doNothing().when(logger).info(anyString());
        PowerMockito.when(Logger.getLogger(anyString())).thenReturn(logger);

    }
}