package com.shruti.lofo.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.shruti.lofo.R;
import com.shruti.lofo.databinding.ActivityLoginBinding;
import com.shruti.lofo.ui.Navigation.BindingNavigation;
import com.shruti.lofo.utils.SessionManager;

public class Login extends AppCompatActivity {


    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        /// |   MODEL INITIALIZATION     |
        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        /// | CHECK IF USER IS LOGGED IN |
        if(sessionManager.isLoggedIn()){

            startActivity(new Intent(this, BindingNavigation.class));
            finish();
        }


        /// |   FUNCTION CALL         |

        setupObserver();

        /// _____________


        /// | CLICK LISTENER  |
        /// | LOGIN BUTTON    |
        binding.loginButton.setOnClickListener(v -> {

            loginUser();
        });


        /// | REGISTER BUTTON |
        binding.createAccountLink.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Register.class));
            overridePendingTransition(R.anim.slide_up, R.anim.stay_still);
        });

    }



    /// +===========================+
    /// |       FUNCTIONS           |
    /// +===========================+


    private void setupObserver() {
        /// +===========================+
        /// |   VIEW MODEL LOADING      |
        /// +===========================+
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.loadingOverlay.setVisibility(View.VISIBLE);
                binding.loginButton.setEnabled(false);
            } else {
                binding.loadingOverlay.setVisibility(View.GONE);
                binding.loginButton.setEnabled(true);
            }
        });



        /// +===========================+
        /// |   OBSERVE LOGIN DATA      |
        /// +===========================+
        viewModel.getLoginSuccessData().observe(this , loginData -> {

            sessionManager.createLoginSession(
                    loginData.getUser().getId(),
                    loginData.getUser().getName(),
                    loginData.getUser().getEmail(),
                    loginData.getUser().getPhone(),
                    loginData.getToken()
            );

            startActivity(new Intent(Login.this, BindingNavigation.class));
            finish();
        });



        /// +===========================+
        /// |  OBSERVE ERROR LIVE DATA  |
        /// +===========================+
        viewModel.getLoginErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(Login.this, errorMessage, Toast.LENGTH_LONG).show();
        });
    }



    /// | LOGIN USER           |
    private void loginUser () {
        String email = binding.loginEmail.getText().toString().trim();
        String password = binding.loginPassword.getText().toString().trim();

        if(isInputValid(email, password) ) {
            viewModel.attemptLogin(email, password);
        } else {
            Toast.makeText(Login.this, "Enter valid data in all fields", Toast.LENGTH_SHORT).show();
        }
    }



    /// | VALIDATOR            |
    private boolean isInputValid(String email, String password) {
        boolean isValid = true;

        if(email.isEmpty()) {
            binding.loginEmail.setError("Email cannot be empty");
            isValid = false;
        }

        if(password.isEmpty()) {
            binding.loginPassword.setError("Password cannot be empty");
            isValid = false;
        }

        return isValid;
    }

}