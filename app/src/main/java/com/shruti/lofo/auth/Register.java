package com.shruti.lofo.auth;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.shruti.lofo.R;
import com.shruti.lofo.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /// |   INITIALIZE VIEW MODEL         |
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);


        /// |   FUNCTION CALL         |

        setupObserver();
        emailChecker();

        /// _____________


        /// | BUTTONS ON REGISTER PAGE |
        binding.signupButton.setOnClickListener(v -> registerUser());


        /// |  BACK BUTTON  |
        binding.loginLink.setOnClickListener(v -> {
                 finish();
                overridePendingTransition(R.anim.stay_still, R.anim.slide_down);

        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                  finish();
                 overridePendingTransition(R.anim.stay_still, R.anim.slide_down);


            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }




    /// +===========================+
    /// |       FUNCTIONS           |
    /// +===========================+


    private void setupObserver() {

        /// |   VIEW MODEL LOADING      |
        viewModel.getIsLoading().observe(this, loading -> {
            if (loading) {
                binding.loadingOverlay.setVisibility(View.VISIBLE);
                binding.signupButton.setEnabled(false);
            } else {
                binding.loadingOverlay.setVisibility(View.GONE);
                binding.signupButton.setEnabled(true);
            }
        });


        /// |   OBSERVE LOGIN DATA      |
        viewModel.getRegisterSuccessData().observe(this, registerData -> {
            Toast.makeText(Register.this, registerData.getMessage(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Register.this, Login.class));
            overridePendingTransition(R.anim.stay_still, R.anim.slide_down);
            finish();
        });


        /// |  OBSERVE ERROR LIVE DATA  |
        viewModel.getRegisterErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(Register.this, errorMessage, Toast.LENGTH_SHORT).show();
        });
    }



    /// | EMAIL CHECKER        |
    private void emailChecker() {
        binding.signupEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString();
                binding.signupEmail.setTextColor(email.endsWith("@hcdc.edu.ph") ? Color.BLACK : Color.RED);
            }
        });
    }




    /// | REGISTER USER        |
    private void registerUser() {
        String name = binding.signupName.getText().toString().trim();
        String email = binding.signupEmail.getText().toString().trim();
        String phone = binding.signupPhone.getText().toString().trim();
        String password = binding.signupPassword.getText().toString().trim();

        if (isValidInput(name, email, phone, password)) {
            viewModel.attemptRegister(email, password, name, phone);
        }
    }



    /// | VALIDATOR            |
    private boolean isValidInput(String name, String email, String phone, String password) {
        boolean isValid = true; // Assume it's true until we find an error


        /// | CHECK NAME            |
        if (name.isEmpty()) {
            binding.signupName.setError("Name cannot be empty");
            isValid = false;
        }


        /// | CHECK EMAIL           |
        if (email.isEmpty()) {
            binding.signupEmail.setError("Email cannot be empty");
            isValid = false;
        } else if (!email.endsWith("@hcdc.edu.ph")) {
            binding.signupEmail.setError("Must use an @hcdc.edu.ph email");
            isValid = false;
        }


        /// | CHECK PHONE           |
        if (phone.isEmpty()) {
            binding.signupPhone.setError("Phone cannot be empty");
            isValid = false;
        } else if (phone.length() != 11 || !phone.startsWith("09")) {
            binding.signupPhone.setError("Must be an 11-digit number starting with 09");
            isValid = false;
        }


        /// |  CHECK PASSWORD       |
        if (password.isEmpty()) {
            binding.signupPassword.setError("Password cannot be empty");
            isValid = false;
        } else if (password.length() < 6) {
            binding.signupPassword.setError("Must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }


}

