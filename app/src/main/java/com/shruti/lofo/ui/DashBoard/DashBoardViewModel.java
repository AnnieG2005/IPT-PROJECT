package com.shruti.lofo.ui.DashBoard;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.Item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardViewModel extends AndroidViewModel {


    /// +===========================+
    /// |  LIVE DATA DECLARATION    |
    /// +===========================+
    private final MutableLiveData<List<Item>> recentItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);


    /// +===========================+
    /// |         CONSTRUCTOR       |
    /// +===========================+
    public DashBoardViewModel(@NonNull Application application) {
        super(application);
    }



    /// +===========================+
    /// |     LIVE DATA GETTERS     |
    /// +===========================+
    public LiveData<List<Item>> getRecentItem() {
        return  recentItemLiveData;
    }

    public LiveData<String> getError() {
        return  errorLiveData;
    }
    public LiveData<Boolean> getIsLoading() { return isLoading; }


    /// +===========================+
    /// |     FETCH ITEM DETAILS    |
    /// +===========================+
    public void fetchFeed(boolean forceRefresh) {
        if(!forceRefresh && getRecentItem().getValue() != null){
            isLoading.setValue(false);
            return;
        }

        if(!forceRefresh){
            isLoading.setValue(true);
        }

        ApiService apiService = RetrofitClient.getApiService(getApplication());
        Call<List<Item>> call = apiService.getItems(null);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                isLoading.setValue(false);

                if(response.isSuccessful() && response.body() != null){
                    List<Item> fetchedItems = response.body();

                     // for top and to limit the front items
                    List<Item> topTenItem = new ArrayList<>();

                    int limit = Math.min(fetchedItems.size(), 10);
                    for (int i = 0; i < limit; i++){
                        topTenItem.add(fetchedItems.get(i));
                    }

                    recentItemLiveData.setValue(topTenItem);
                } else {
                    errorLiveData.setValue("Failed to load feed.");
                 }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("DashBoard", "API Error: " + t.getMessage());
                errorLiveData.setValue("Network error while loading feed.");
            }
        });
    }

}