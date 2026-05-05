package com.shruti.lofo.ui.MyProfile;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.LoginResponse;
import com.shruti.lofo.data.model.ProfileUpdateRequest;
import com.shruti.lofo.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileViewModel extends AndroidViewModel {

    private static final String TAG = "EditProfileViewModel";

    public enum UpdateState { IDLE, LOADING, SUCCESS, ERROR }


    private final MutableLiveData<UpdateState> _updateState = new MutableLiveData<>(UpdateState.IDLE);
    public final LiveData<UpdateState> updateState = _updateState;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public final LiveData<String> errorMessage = _errorMessage;


    private final SessionManager sessionManager;

    public EditProfileViewModel(@NonNull Application application) {
        super(application);
        sessionManager = new SessionManager(application);
    }

    public String getCurrentName() {
        return sessionManager.getName();
    }

    public String getCurrentPhone() {
        return sessionManager.getPhone();
    }


    public void updateProfile(String newName, String newPhone) {
        if (newName.isEmpty() || newPhone.isEmpty()) {
            _errorMessage.setValue("Fields cannot be empty.");
            return;
        }

        _updateState.setValue(UpdateState.LOADING);

        ProfileUpdateRequest request = new ProfileUpdateRequest(newName, newPhone);
        ApiService apiService = RetrofitClient.getApiService(getApplication());

        apiService.updateProfile(request).enqueue(new Callback<LoginResponse.User>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse.User> call,
                                   @NonNull Response<LoginResponse.User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.updateProfileSession(newName, newPhone);
                    _updateState.setValue(UpdateState.SUCCESS);
                } else {
                    Log.e(TAG, "Update failed: " + response.message());
                    _errorMessage.setValue("Failed to update profile.");
                    _updateState.setValue(UpdateState.ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse.User> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                _errorMessage.setValue("Network error. Please try again.");
                _updateState.setValue(UpdateState.ERROR);
            }
        });
    }
}