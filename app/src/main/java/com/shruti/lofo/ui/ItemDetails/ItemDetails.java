package com.shruti.lofo.ui.ItemDetails;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.se.omapi.Session;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.shruti.lofo.R;
import com.shruti.lofo.databinding.ItemDetailsCardBinding;
import com.shruti.lofo.utils.SessionManager;

public class ItemDetails extends AppCompatActivity {

    private ItemDetailsViewModel viewModel;
    private ItemDetailsCardBinding binding;
    private String phoneNumber;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ItemDetailsCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        int itemId = getIntent().getIntExtra("itemId", -1);


        setupObserver(itemId);


        /// BUTTONS ON DETAILS PAGE



        binding.backBtn.setOnClickListener(v -> finish());
        binding.copyMailBtn.setOnClickListener(v -> copyEmail());
        binding.call.setOnClickListener(v -> makeCall());
        binding.sms.setOnClickListener(v -> sendSMS());

    }




    /// +===========================+
    /// |       FUNCTIONS           |
    /// +===========================+


    private void setupObserver(int itemId) {
        /// +===========================+
        /// |   INITIALIZE VIEW MODEL   |
        /// +===========================+
        viewModel = new ViewModelProvider(this).get(ItemDetailsViewModel.class);

        /// +===========================+
        /// |   OBSERVE LIVE DATA       |
        /// +===========================+
        viewModel.getItemLiveData().observe(this, item -> {
            if(item != null) {
                binding.title.setText(item.getItem_name());
                binding.address.setText(item.getLocation());

                binding.itemType.setText(item.getType());
                binding.itemType.setTextColor(ContextCompat.getColor(ItemDetails.this, item.getType().equalsIgnoreCase("Found") ? R.color.found_green :  R.color.lost_red));

                binding.description.setText(item.getDescription());
                binding.category.setText(item.getCategory());

                binding.dateDetails.setText(item.getDate());
                binding.timeDetails.setText(item.getTime());

                binding.mail.setText(item.getEmail());
                binding.reportedBy.setText(item.getFull_name());

                phoneNumber = item.getContact_phone();
                checkOwner(phoneNumber);

                Glide.with(ItemDetails.this)
                        .load(item.getImage_url())
                        .placeholder(R.drawable.dashboard_img1)
                        .error(R.drawable.dashboard_img1)
                        .into(binding.img);
            }
        });


        /// +===========================+
        /// |  OBSERVE ERROR LIVE DATA  |
        /// +===========================+
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(ItemDetails.this, errorMessage, Toast.LENGTH_SHORT).show();
            finish();
        });


        /// CLAUSE TO CHECK IF ITEM ID IS PASSED
        if(itemId != -1) {
            viewModel.fetchItemDetails(itemId);
        } else {
            Toast.makeText(this, "Error: Item ID missing.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void checkOwner(String number) {
        if(sessionManager.getPhone().equals(number)){
            binding.call.setEnabled(true);
            binding.call.setClickable(false);
            binding.call.setAlpha(0.5f);
            binding.sms.setEnabled(true);
            binding.sms.setClickable(false);
            binding.sms.setAlpha(0.5f);
            binding.copyMailBtn.setEnabled(true);
            binding.copyMailBtn.setClickable(false);
            binding.copyMailBtn.setAlpha(0.5f);
        }
    }



    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }



    private void sendSMS(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + phoneNumber));
        intent.putExtra("sms_body", "Hello, I want to inquire about the item.");
        startActivity(intent);
    }

    public void copyEmail(){
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("email", binding.mail.getText());
        clipboard.setPrimaryClip(clip);
        binding.copyMailBtn.setText("Copied!");
        binding.copyMailBtn.postDelayed(
                () -> binding.copyMailBtn.setText("Copy"), 2000
        );
    }
}
