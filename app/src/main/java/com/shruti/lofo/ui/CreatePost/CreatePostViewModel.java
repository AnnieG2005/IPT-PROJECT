package com.shruti.lofo.ui.CreatePost;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shruti.lofo.CloudinaryConfig;
import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.Item;
import com.shruti.lofo.data.model.ItemRequest;
import com.shruti.lofo.utils.SessionManager;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostViewModel extends AndroidViewModel {

    private static final String TAG = "CreatePostViewModel";



    /// | UI STATES             |
    public enum SubmitState { IDLE, UPLOADING_IMAGE, SAVING, SUCCESS, ERROR }

    private final MutableLiveData<SubmitState> _submitState = new MutableLiveData<>(SubmitState.IDLE);
    public LiveData<SubmitState> submitState = _submitState;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;



    /// | FORM STATE            |
    private Uri imageUri = null;
    private String selectedCategory = "Other";
    private String selectedDate = null;
    private String selectedTime = null;

    public CreatePostViewModel(@NonNull Application application) {
        super(application);
    }



    /// | SETTERS              |
    public void setImageUri(Uri uri) {
        this.imageUri = uri;
    }

    public void setSelectedCategory(String category) {
        this.selectedCategory = category;
    }

    public void setSelectedDate(String date) {
        this.selectedDate = date;
    }

    public void setSelectedTime(String time) {
        this.selectedTime = time;
    }



    /// | GETTERS               |
    public boolean hasImage() {
        return imageUri != null;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }



    /// | VALIDATION            |

    public boolean validate(String itemName, String desc, String loc) {
        if (itemName.isEmpty() || desc.isEmpty() || loc.isEmpty()) {
            _errorMessage.setValue("Please fill out all fields.");
            return false;
        }
        if (selectedDate == null || selectedTime == null) {
            _errorMessage.setValue("Please select a date and time.");
            return false;
        }
        if (imageUri == null) {
            _errorMessage.setValue("Please select an image.");
            return false;
        }
        return true;
    }



    /// | SUBMIT FLOWS          |

    public void submitItem(String itemName, String desc, String loc, String itemType) {
        _submitState.setValue(SubmitState.UPLOADING_IMAGE);
        uploadImageToCloudinary(itemName, desc, loc, itemType);
    }


    /// | CLOUDINARY FUNCTIONS   |
    private void uploadImageToCloudinary(String itemName, String desc, String loc, String itemType) {
        String uploadUrl = "https://api.cloudinary.com/v1_1/"
                + CloudinaryConfig.CLOUD_NAME + "/image/upload";

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        try {
            params.put("upload_preset", "dny4hxil");
            params.put(
                    "file",
                    getApplication().getContentResolver().openInputStream(imageUri),
                    "file.jpg"
            );
        } catch (Exception e) {
            _errorMessage.setValue("Error reading image file.");
            _submitState.setValue(SubmitState.ERROR);
            return;
        }

        client.post(uploadUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    String imageUrl = json.getString("secure_url");
                    saveToDatabase(itemName, desc, loc, itemType, imageUrl);
                } catch (Exception e) {
                    _errorMessage.setValue("Error reading upload response.");
                    _submitState.setValue(SubmitState.ERROR);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "Cloudinary upload failed: " + error.getMessage());
                _errorMessage.setValue("Image upload failed. Please try again.");
                _submitState.setValue(SubmitState.ERROR);
            }
        });
    }


    /// | SAVE TO DATABASE      |
    private void saveToDatabase(String itemName, String desc, String loc,
                                String itemType, String imageUrl) {
        _submitState.setValue(SubmitState.SAVING);

        SessionManager session = new SessionManager(getApplication());
        String userPhone = session.getPhone();

        ItemRequest request = new ItemRequest(
                itemName, desc, loc,
                selectedDate, selectedTime,
                selectedCategory, itemType,
                imageUrl, userPhone
        );

        ApiService apiService = RetrofitClient.getApiService(getApplication());
        apiService.createItem(request).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    _submitState.setValue(SubmitState.SUCCESS);
                } else {
                    try {
                        String errorBody = response.errorBody() != null
                                ? response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Server error " + response.code() + ": " + errorBody);
                        _errorMessage.setValue("Server error: " + response.code());
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error body", e);
                        _errorMessage.setValue("An unexpected error occurred.");
                    }
                    _submitState.setValue(SubmitState.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                _errorMessage.setValue("Network error. Check your connection.");
                _submitState.setValue(SubmitState.ERROR);
            }
        });
    }
}