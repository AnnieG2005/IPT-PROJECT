package com.shruti.lofo.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.RegisterRequest;
import com.shruti.lofo.data.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends AndroidViewModel {

    /// +===========================+
    /// |  LIVE DATA DECLARATION    |
    /// +===========================+
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> registerSuccessData = new MutableLiveData<>();
    private final MutableLiveData<String> registerErrorMessage = new MutableLiveData<>();



    /// +===========================+
    /// |         CONSTRUCTOR       |
    /// +===========================+
    public RegisterViewModel(@NonNull Application application) { super(application);}



    /// +===========================+
    /// |     LIVE DATA GETTERS     |
    /// +===========================+
    public LiveData<Boolean> getIsLoading () { return isLoading; }
    public LiveData<RegisterResponse> getRegisterSuccessData() { return registerSuccessData; }
    public LiveData<String> getRegisterErrorMessage() { return registerErrorMessage; }



    /// +===========================+
    /// |     FETCH ITEM DETAILS    |
    /// +===========================+
    public void attemptRegister(String email, String password, String fullName, String phoneNumber) {
        isLoading.setValue(true);

        RegisterRequest request = new RegisterRequest(email, password, fullName, phoneNumber);

        ApiService apiService = RetrofitClient.getApiService(getApplication());
        Call<RegisterResponse> call = apiService.registerUser(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                isLoading.setValue(false);

                if(response.isSuccessful() && response.body() != null) {
                    registerSuccessData.setValue(response.body());
                } else {
                    registerErrorMessage.setValue("Registration failed.");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                isLoading.setValue(false);
                registerErrorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }
}
