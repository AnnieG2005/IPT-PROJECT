package com.shruti.lofo.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.shruti.lofo.utils.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private Context context;
    public AuthInterceptor(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getToken();

        Log.d("Interceptor", "Token found in wallet: " + token);

        if(token != null && !token.isEmpty()){
            requestBuilder.addHeader("Authorization", "Bearer " + token);

        }

        return chain.proceed(requestBuilder.build());
    }
}
