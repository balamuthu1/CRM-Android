package fr.pds.isintheair.crmtab.model.dao;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import fr.pds.isintheair.crmtab.model.mock.Contact;
import fr.pds.isintheair.crmtab.model.mock.Contact_Table;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 01/23/2016         *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class ContactDAO {
    public static List<Contact> getAll() {
        return new Select().from(Contact.class).queryList();
    }

    public static void delete() {
        new Delete().from(Contact.class).query();
    }

    public static void delete(String num) {
        new Delete().from(Contact.class).where(Contact_Table.phoneNumber.eq(num)).query();
    }

    public static Contact getContactFromName(String name) {
        return new Select().from(Contact.class).where(Contact_Table.firstName.eq(name)).querySingle();
    }

    public static Contact getContactFromNumber(String number) {
        return new Select().from(Contact.class).where(Contact_Table.firstName.eq(number)).querySingle();
    }
}
