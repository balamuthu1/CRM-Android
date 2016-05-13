package miage.pds.api.jdatour;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHandlerSingleton {
    private static RetrofitHandlerSingleton mInstance = new RetrofitHandlerSingleton();

    private DriveApi driveApi;

    private RetrofitHandlerSingleton () {
        Gson                   gson    = new GsonBuilder().create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        Retrofit     retrofit   = new Retrofit.Builder().baseUrl(DriveApi.BASE_URL).client(httpClient).addConverterFactory(GsonConverterFactory.create(gson)).build();

        driveApi = retrofit.create(DriveApi.class);
    }

    public static synchronized RetrofitHandlerSingleton getInstance () {
        if (mInstance == null) {
            mInstance = new RetrofitHandlerSingleton();
        }

        return mInstance;
    }

    public DriveApi getDriveApi () {
        return this.driveApi;
    }
}
