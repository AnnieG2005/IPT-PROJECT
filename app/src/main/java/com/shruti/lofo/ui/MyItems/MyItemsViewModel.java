package com.shruti.lofo.ui.MyItems;

import android.app.Application;

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

public class MyItemsViewModel extends AndroidViewModel {

    /// +===========================+
    /// |  LIVE DATA DECLARATION    |
    /// +===========================+
    private final MutableLiveData<List<Item>> myLostItems = new MutableLiveData<>();
    private final MutableLiveData<List<Item>> myFoundItems = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);


    /// +===========================+
    /// |         CONSTRUCTOR       |
    /// +===========================+
    public MyItemsViewModel(@NonNull Application application) {
        super(application);
    }



    /// +===========================+
    /// |     LIVE DATA GETTERS     |
    /// +===========================+
    public LiveData<List<Item>> getMyLostItems() { return myLostItems; }
    public LiveData<List<Item>> getMyFoundItems() { return myFoundItems; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }



    /// +===========================+
    /// |     FETCH ITEM DETAILS    |
    /// +===========================+
    public void fetchItems () {
        isLoading.setValue(true);

        ApiService apiService = RetrofitClient.getApiService(getApplication());


        /// FETCH USER LOST ITEMS
        apiService.getUserItems("Lost").enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                isLoading.setValue(false);

                if(response.isSuccessful() && response.body() != null){
                    myLostItems.setValue(response.body());
                } else {
                    android.util.Log.e("AndroidApp", "Server returned error code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                isLoading.setValue(false);
                android.util.Log.e("AndroidApp", "Network crashed: " + t.getMessage());
                errorMessage.setValue("Failed to load Lost items.");
            }
        });


        ///  FETCH USER FOUND ITEMS
        apiService.getUserItems("Found").enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(response.isSuccessful() && response.body() != null){
                    myFoundItems.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                errorMessage.setValue("Failed to load Found items.");
            }
        });
    }

}
