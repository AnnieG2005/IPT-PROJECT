package com.shruti.lofo.ui.MyProfile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shruti.lofo.R;
import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.LoginResponse;
import com.shruti.lofo.data.model.ProfileUpdateRequest;
import com.shruti.lofo.databinding.ActivityEditProfileBinding;
import com.shruti.lofo.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {


    private ActivityEditProfileBinding binding;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        binding.editName.setText(sessionManager.getName());
        binding.editPhone.setText(sessionManager.getPhone());


        binding.saveBtn.setOnClickListener(v -> saveChanges());
        binding.cancelBtn.setOnClickListener(v -> finish());
    }



    private void saveChanges() {
        String newName = binding.editName.getText().toString().trim();
        String newPhone = binding.editPhone.getText().toString().trim();

        if(newName.isEmpty() || newPhone.isEmpty()){
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }


        binding.saveBtn.setText("UPDATING...");
        binding.saveBtn.setEnabled(false);


        ProfileUpdateRequest request = new ProfileUpdateRequest(newName, newPhone);

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<LoginResponse.User> call = apiService.updateProfile(request);

        call.enqueue(new Callback<LoginResponse.User>() {

            @Override
            public void onResponse(Call<LoginResponse.User> call, retrofit2.Response<LoginResponse.User> response) {
                if(response.isSuccessful() && response.body() != null) {
                    LoginResponse.User updatedUser = response.body();
                    sessionManager.updateProfileSession(newName, newPhone);
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    binding.saveBtn.setText("SAVE CHANGES");
                    binding.saveBtn.setEnabled(true);
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    Log.e("EditProfileActivity", "Error updating profile: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse.User> call, Throwable t) {
                binding.saveBtn.setText("SAVE CHANGES");
                binding.saveBtn.setEnabled(true);
                Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                Log.e("EditProfileActivity", "Error updating profile: " + t.getMessage(), t);
            }
        });
    }



}
