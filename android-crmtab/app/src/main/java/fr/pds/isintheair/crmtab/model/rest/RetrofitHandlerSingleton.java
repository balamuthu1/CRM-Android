package fr.pds.isintheair.crmtab.model.rest;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import fr.pds.isintheair.crmtab.Constant;
import fr.pds.isintheair.crmtab.model.rest.service.DatabaseService;
import fr.pds.isintheair.crmtab.model.rest.service.LoginService;
import fr.pds.isintheair.crmtab.model.rest.service.NotifyPresenceRetrofitService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/******************************************
 * Created by        : datour_j           *
 * Creation date     : 03/20/16           *
 * Modified by       : datour_j           *
 * Modification date : 10/05/16           *
 ******************************************/

public class RetrofitHandlerSingleton {
    private static RetrofitHandlerSingleton mInstance = new RetrofitHandlerSingleton();

    private DatabaseService               databaseService;
    private LoginService                  loginService;
    private NotifyPresenceRetrofitService notifyPresenceRetrofitService;

    private RetrofitHandlerSingleton() {
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getDeclaredClass().equals(ModelAdapter.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.REST_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

        databaseService = retrofit.create(DatabaseService.class);
        loginService = retrofit.create(LoginService.class);
        notifyPresenceRetrofitService = retrofit.create(NotifyPresenceRetrofitService.class);
    }

    public static synchronized RetrofitHandlerSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitHandlerSingleton();
        }

        return mInstance;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public NotifyPresenceRetrofitService getNotifyPresenceService() {
        return notifyPresenceRetrofitService;
    }
}
