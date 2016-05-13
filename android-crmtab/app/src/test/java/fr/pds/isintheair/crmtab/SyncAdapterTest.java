package fr.pds.isintheair.crmtab;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.ResponseBody;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import static org.junit.Assert.*;

import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.adapter.SyncAdapter;
import fr.pds.isintheair.crmtab.model.rest.service.ProspectRestConfig;
import fr.pds.isintheair.crmtab.model.rest.service.SyncRetrofitAPI;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Truong on 5/8/2016.
 */
public class SyncAdapterTest {
    SyncAdapter adapter;
    Context context = Mockito.mock(Context.class);

    @Before
    public void setUp() throws Exception {
        adapter = new SyncAdapter(context, true);
    }

    @Test
    public void testGetData() throws Exception {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ProspectRestConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        SyncRetrofitAPI api = retrofit.create(SyncRetrofitAPI.class);
        Call<ResponseBody> result = api.getSyncData();
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    assertTrue(response.isSuccess());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });

        String[] test = adapter.getData();
        assertNotNull(test);
        boolean isCorrect = false;
        if (test[0] == "ko") {
            isCorrect = true;
            Log.d("test", "testGetData: " + test);
        }
        assertFalse(isCorrect);
    }
}
