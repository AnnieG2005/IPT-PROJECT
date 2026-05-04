package com.shruti.lofo.ui.Lost;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LostViewModel extends AndroidViewModel {


    /// +===========================+
    /// |  LIVE DATA DECLARATION    |
    /// +===========================+
    private final MutableLiveData<List<Item>> lostItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<String>errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    /// +===========================+
    /// |         CONSTRUCTOR       |
    /// +===========================+
    public LostViewModel(@NonNull Application application) {
        super(application);
    }


    /// +===========================+
    /// |     LIVE DATA GETTERS     |
    /// +===========================+
    public LiveData<List<Item>> getLostItem() {
        return lostItemLiveData;
    }

    public LiveData<String> getError(){
        return errorLiveData;
    }
    public LiveData<Boolean> getIsLoading () { return isLoading; }
    /// +===========================+
    /// |     FETCH ITEM DETAILS    |
    /// +===========================+
    public void fetchLostItems (boolean forceRefresh) {
        if (!forceRefresh && lostItemLiveData.getValue() != null) {
            isLoading.setValue(false);
            return;
        }

        if(!forceRefresh){
            isLoading.setValue(true);
        }

        ApiService apiService = RetrofitClient.getApiService(getApplication());
        Call<List<Item>> call = apiService.getItems("Lost");

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                isLoading.setValue(false);

                if(response.isSuccessful() && response.body() != null) {
                    lostItemLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Failed to load Lost items.");
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("LostViewModel", "API Error: " + t.getMessage());
                errorLiveData.setValue("Network error while loading feed.");
            }
        });
    }
}