package fr.pds.isintheair.crmtab.controller.bus.event;

import fr.pds.isintheair.crmtab.model.mock.Contact;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 01/26/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class PhoneCallCutEvent {
    private Contact contact;

    public PhoneCallCutEvent(Contact contact) {
        this.contact = contact;
    }
}
