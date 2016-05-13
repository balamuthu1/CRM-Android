package fr.pds.isintheair.crmtab.model.rest;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import fr.pds.isintheair.crmtab.model.rest.service.CustomerService;
import fr.pds.isintheair.crmtab.model.rest.service.PhoningCampaignService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by tlacouque on 27/03/2016.
 */
public class RESTPhoningCampaignHandlerSingleton {



    private static RESTPhoningCampaignHandlerSingleton mInstance;
    private final PhoningCampaignService phoningCampaignService;

    private RESTPhoningCampaignHandlerSingleton() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaredClass().equals(ModelAdapter.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(PhoningCampaignService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        phoningCampaignService = retrofit.create(PhoningCampaignService.class);
    }

    public static synchronized RESTPhoningCampaignHandlerSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new RESTPhoningCampaignHandlerSingleton();
        }

        return mInstance;
    }


    public PhoningCampaignService getPhoningCampaignService() {
        return phoningCampaignService;
    }
}
