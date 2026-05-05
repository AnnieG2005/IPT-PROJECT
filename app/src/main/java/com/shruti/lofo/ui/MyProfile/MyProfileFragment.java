package com.shruti.lofo.ui.MyProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shruti.lofo.databinding.FragmentMyProfileBinding;
import com.shruti.lofo.utils.SessionManager;

public class MyProfileFragment extends Fragment {
    private SessionManager sessionManager;
    private FragmentMyProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        sessionManager = new SessionManager(requireContext());

        binding.profileName.setText(sessionManager.getName());
        binding.profileEmail.setText(sessionManager.getEmail());
        binding.profilephone.setText(sessionManager.getPhone());


        binding.editProfileButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        });

        return root;
    }



    @Override
    public void onResume() {
        super.onResume();
        binding.profileName.setText(sessionManager.getName());
        binding.profileEmail.setText(sessionManager.getEmail());
        binding.profilephone.setText(sessionManager.getPhone());
    }



    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
