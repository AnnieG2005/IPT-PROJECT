package com.shruti.lofo.ui.Found;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoundViewModel extends AndroidViewModel {



    /// +===========================+
    /// |  LIVE DATA DECLARATION    |
    /// +===========================+
    private final MutableLiveData<List<Item>> foundItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    /// +===========================+
    /// |         CONSTRUCTOR       |
    /// +===========================+
    public FoundViewModel(@NonNull Application application) {
        super(application);
    }



    /// +===========================+
    /// |     LIVE DATA GETTERS     |
    /// +===========================+
    public LiveData<List<Item>> getFoundItems () { return foundItemLiveData; }

    public LiveData<String> getError () { return errorLiveData; }
    public LiveData<Boolean> getIsLoading () { return isLoading; }

    /// +===========================+
    /// |     FETCH ITEM DETAILS    |
    /// +===========================+
    public void fetchFoundItems (boolean forceRefresh) {
        if(!forceRefresh && foundItemLiveData.getValue() != null){
            isLoading.setValue(false);
            return;
        }

        if(!forceRefresh) {
            isLoading.setValue(true);
        }

        ApiService apiService = RetrofitClient.getApiService(getApplication());
        Call<List<Item>> call = apiService.getItems("Found");

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                isLoading.setValue(false);

                if(response.isSuccessful() && response.body() != null){
                    foundItemLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Failed to load Found items.");
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("FoundViewModel", "API Error " + t.getMessage());
                errorLiveData.setValue("Network error while loading feed");
            }
        });
    }
}
