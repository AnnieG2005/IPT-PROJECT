package com.shruti.lofo.ui.MyItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.shruti.lofo.databinding.FragmentMyItemsBinding;
import com.shruti.lofo.data.model.Item;
import com.shruti.lofo.ui.ItemDetails.ItemDetails;
import com.shruti.lofo.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MyItemsFragment extends Fragment {

    private FragmentMyItemsBinding binding;
    private MyItemsViewModel viewModel;
    private FeedAdapter feedAdapter;

    private List<Item> lostItemsList  = new ArrayList<>();
    private List<Item> foundItemsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyItemsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager sessionManager = new SessionManager(requireContext());

        setupRecyclerView();

        viewModel = new ViewModelProvider(this).get(MyItemsViewModel.class);

        setupTabListener();

        if (sessionManager.isLoggedIn()) {
            observeViewModel();
            viewModel.fetchItems();
        } else {
            binding.emptyStateText.setText("Please log in to view your items.");
            binding.emptyStateText.setVisibility(View.VISIBLE);
        }

    }

    /// SETUP FUNCTIONS

    private void setupRecyclerView() {
        binding.masterRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        feedAdapter = new FeedAdapter(requireContext(), true);
        binding.masterRecyclerView.setAdapter(feedAdapter);

        feedAdapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(requireContext(), ItemDetails.class);
            intent.putExtra("itemId", item.getItem_id());
            startActivity(intent);
        });
    }

    private void setupTabListener() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    updateUI(lostItemsList, "You haven't posted any lost items yet.");
                } else {
                    updateUI(foundItemsList, "You haven't posted any found items yet.");
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    /// VIEWMODEL OBSERVERS

    private void observeViewModel() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.lottieAnimation.setVisibility(View.VISIBLE);
                binding.masterRecyclerView.setVisibility(View.GONE);
                binding.emptyStateContainer.setVisibility(View.GONE);
            } else {
                binding.lottieAnimation.setVisibility(View.GONE);
            }
        });


        viewModel.getMyLostItems().observe(getViewLifecycleOwner(), items -> {
            if (items == null) return;
            lostItemsList = items;
            if (binding.tabLayout.getSelectedTabPosition() == 0) {
                updateUI(lostItemsList, "You haven't posted any lost items yet.");
            }
        });

        viewModel.getMyFoundItems().observe(getViewLifecycleOwner(), items -> {
            if (items == null) return;
            foundItemsList = items;
            if (binding.tabLayout.getSelectedTabPosition() == 1) {
                updateUI(foundItemsList, "You haven't posted any found items yet.");
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }



    /// UPDATE UI
    private void updateUI(List<Item> items, String emptyMessage) {
        boolean hasItems = items != null && !items.isEmpty();

        feedAdapter.setItems(hasItems ? items : new ArrayList<>());

        binding.masterRecyclerView.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        binding.emptyStateContainer.setVisibility(hasItems ? View.GONE : View.VISIBLE);

        if (!hasItems) {
            binding.emptyStateText.setText(emptyMessage);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}