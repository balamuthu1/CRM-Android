package fr.pds.isintheair.phonintheair.controller.bus.handler;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class BusHandlerSingleton {
    private static BusHandlerSingleton mInstance = new BusHandlerSingleton();
    private final Bus bus;

    private BusHandlerSingleton() {
        bus = new Bus(ThreadEnforcer.ANY);
    }

    public static synchronized BusHandlerSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new BusHandlerSingleton();
        }

        return mInstance;
    }

    public Bus getBus() {
        return bus;
    }
}
