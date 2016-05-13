package fr.pds.isintheair.crmtab.model.rest.service;


import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Truong on 5/5/2016.
 */
public interface SyncRetrofitAPI {

    @GET("sync/demo")
    Call<ResponseBody> getSyncData();
}
