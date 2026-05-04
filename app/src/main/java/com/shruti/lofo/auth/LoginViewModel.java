package com.shruti.lofo.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.LoginRequest;
import com.shruti.lofo.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {


    /// +===========================+
    /// |  LIVE DATA DECLARATION    |
    /// +===========================+
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<LoginResponse> loginSuccessData = new MutableLiveData<>();

    private final MutableLiveData<String> loginErrorMessage = new MutableLiveData<>();



    /// +===========================+
    /// |         CONSTRUCTOR       |
    /// +===========================+
    public LoginViewModel(@NonNull Application application) { super(application);}



    /// +===========================+
    /// |     LIVE DATA GETTERS     |
    /// +===========================+
    public LiveData<Boolean> getIsLoading () { return isLoading; }
    public LiveData<LoginResponse> getLoginSuccessData() { return loginSuccessData; }
    public LiveData<String> getLoginErrorMessage() { return loginErrorMessage; }



    /// +===========================+
    /// |     FETCH ITEM DETAILS    |
    /// +===========================+
    public void attemptLogin(String email, String password) {
        isLoading.setValue(true);

        LoginRequest request = new LoginRequest(email, password);

        ApiService apiService = RetrofitClient.getApiService(getApplication());
        Call<LoginResponse> call = apiService.loginUser(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                isLoading.setValue(false);

                if(response.isSuccessful() && response.body() != null) {
                    loginSuccessData.setValue(response.body());
                } else {
                    loginErrorMessage.setValue("Invalid email or password.");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                isLoading.setValue(false);
                loginErrorMessage.setValue("Network error: " + t.getMessage());
            }
        });

    }
}
