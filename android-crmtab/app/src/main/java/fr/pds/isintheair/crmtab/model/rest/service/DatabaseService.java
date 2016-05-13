package fr.pds.isintheair.crmtab.model.rest.service;

import retrofit.Call;
import retrofit.http.GET;

/******************************************
 * Created by        : datour_j           *
 * Creation date     : 05/10/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public interface DatabaseService {
    @GET("database/dump")
    Call<String> dumpDatabase();

    @GET("database/restore")
    Call<String> restoreDatabase();
}
