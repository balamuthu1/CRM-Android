package fr.pds.isintheair.crmtab.model.dao;


import com.raizlabs.android.dbflow.sql.language.Select;

import fr.pds.isintheair.crmtab.model.entity.User;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 01/24/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class UserDAO {
    public static User getCurrentUser() {
        return new Select().from(User.class).querySingle();
    }

}
