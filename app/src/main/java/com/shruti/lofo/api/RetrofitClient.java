package com.shruti.lofo.api;

import android.content.Context;


import com.shruti.lofo.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    /// +===================================================================================+
    //    For Local Host:
    //    Use 10.0.2.2 for the Emulator, or your IPv4 for a physical phone!

    //    For Deployment (sample)
    //    private static final String BASE_URL = "https://lofo-backend-xyz.onrender.com";
    /// +===================================================================================+


    /// +===================================================================================+
//    private static final String BASE_URL = "http://10.0.2.2:3000";
    // public static final String BASE_URL = "http://192.168.42.245:3000/";
    /// +===================================================================================+
    private static Retrofit retrofit = null;

    public static ApiService getApiService(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}