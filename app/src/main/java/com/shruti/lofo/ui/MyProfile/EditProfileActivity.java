package com.shruti.lofo.ui.MyProfile;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.shruti.lofo.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private EditProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        populateFields();
        setupClickListeners();
        setupObservers();
    }


    private void populateFields() {
        String name  = viewModel.getCurrentName();
        String phone = viewModel.getCurrentPhone();

        binding.editName.setText(name);
        binding.editPhone.setText(phone);

        if (name != null && !name.isEmpty()) {
            binding.avatarInitial.setText(String.valueOf(name.charAt(0)).toUpperCase());
            binding.avatarName.setText(name);
        }
    }

    private void setupClickListeners() {
        binding.backArrow.setOnClickListener(v -> finish());
        binding.cancelBtn.setOnClickListener(v -> finish());

        binding.saveBtn.setOnClickListener(v -> {
            String newName = binding.editName.getText() != null
                    ? binding.editName.getText().toString().trim() : "";
            String newPhone = binding.editPhone.getText() != null
                    ? binding.editPhone.getText().toString().trim() : "";

            viewModel.updateProfile(newName, newPhone);
        });
    }

    private void setupObservers() {
        viewModel.updateState.observe(this, state -> {
            switch (state) {
                case LOADING:
                    setLoading(true);
                    break;

                case SUCCESS:
                    Toast.makeText(this,
                            "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                case ERROR:
                    setLoading(false);
                    break;

                case IDLE:
                default:
                    break;
            }
        });

        viewModel.errorMessage.observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setLoading(boolean isLoading) {
        binding.loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.saveBtn.setEnabled(!isLoading);
        binding.cancelBtn.setEnabled(!isLoading);
        binding.backArrow.setEnabled(!isLoading);
    }
}