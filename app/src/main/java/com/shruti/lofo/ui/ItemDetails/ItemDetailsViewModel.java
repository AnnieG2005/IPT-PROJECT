package com.shruti.lofo.ui.ItemDetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.Item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsViewModel extends AndroidViewModel {

    /// +===========================+
    /// |  LIVE DATA DECLARATION    |
    /// +===========================+
    private MutableLiveData<Item> itemLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();



    /// +===========================+
    /// |         CONSTRUCTOR       |
    /// +===========================+
    public ItemDetailsViewModel(@NonNull Application application) {
        super(application);
    }



    /// +===========================+
    /// |     LIVE DATA GETTERS     |
    /// +===========================+
    public LiveData<Item> getItemLiveData() { return itemLiveData; }

    public LiveData<String> getErrorMessage() {return errorMessage; }



    /// +===========================+
    /// |     FETCH ITEM DETAILS    |
    /// +===========================+
    public void fetchItemDetails (int itemId) {
        ApiService apiService = RetrofitClient.getApiService(getApplication());
        Call<Item> call = apiService.getItemById(itemId);

        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(response.isSuccessful() && response.body() != null) {
                    itemLiveData.setValue(response.body());
                    return;
                }

                errorMessage.setValue("Failed to load item details.");
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }
}
