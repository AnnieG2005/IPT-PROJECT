package com.shruti.lofo.ui.Lost;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.shruti.lofo.R;
import com.shruti.lofo.data.model.Item;
import com.shruti.lofo.databinding.FragmentItemListingBinding;
import com.shruti.lofo.ui.CreatePost.CreatePostFragment;
import com.shruti.lofo.ui.DashBoard.RecyclerRecentLoFoAdapter;
import com.shruti.lofo.ui.ItemDetails.ItemDetails;
import com.shruti.lofo.utils.GridSpacingItemDecoration;

import java.util.ArrayList;

public class LostFragment extends Fragment {

    private FragmentItemListingBinding binding;
    private LostViewModel lostViewModel;
    private RecyclerRecentLoFoAdapter adapter;


    private final ArrayList<Item> allLostItems = new ArrayList<>();
    private final ArrayList<Item> displayList = new ArrayList<>();
    private final String ALL_CATEGORIES_TEXT = "All Categories";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentItemListingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        /// |   MODEL INITIALIZATION     |
        lostViewModel = new ViewModelProvider(requireActivity()).get(LostViewModel.class);


        /// |   FUNCTION CALL         |
        setupUI();
        setupRecyclerView();
        setupSpinner();
        setupObservers();

        getParentFragmentManager().setFragmentResultListener(
                "refresh_feed", getViewLifecycleOwner(),
                (requestKey, result) -> lostViewModel.fetchLostItems(true)
        );

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            lostViewModel.fetchLostItems(true);
        });

        lostViewModel.fetchLostItems(false);

        binding.addItem.setOnClickListener(v -> {
            CreatePostFragment dialog = CreatePostFragment.newInstance("Lost");
            dialog.show(getParentFragmentManager(), "CreatePostDialog");
        });

        binding.addItem.setBackground(ContextCompat.getDrawable(requireContext(), R.color.orange_tag));
        binding.addItem.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.orange_tag));
    }

    private void setupUI() {
        binding.categoryAccent.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.orange_tag)
        );
        binding.categoryTitle.setText("Lost Items");
        binding.emptyTitle.setText("No lost items yet");
        binding.emptySubtitle.setText("Items you report as lost will appear here");
    }

    private void setupRecyclerView() {
        int spacingInPixels = (int) (2 * getResources().getDisplayMetrics().density);

        binding.itemRecyclerView.setLayoutManager(
                new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        );


        binding.itemRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
        binding.itemRecyclerView.setItemAnimator(null);

        adapter = new RecyclerRecentLoFoAdapter(requireContext(), displayList);
        binding.itemRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(requireContext(), ItemDetails.class);
            intent.putExtra("itemId", item.getItem_id());
            startActivity(intent);
        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categories_array,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(spinnerAdapter);

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterByCategory(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setupObservers() {

        lostViewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            if(loading) {
                binding.lottieAnimation.setVisibility(View.VISIBLE);
//                binding.itemRecyclerView.setVisibility(View.GONE);
//                binding.emptyStateLayout.setVisibility(View.GONE);
            } else  {
                binding.lottieAnimation.setVisibility(View.GONE);
             }
        });


        lostViewModel.getLostItem().observe(getViewLifecycleOwner(), items -> {
            if(items != null) {
                allLostItems.clear();
                allLostItems.addAll(items);
                adapter.notifyDataSetChanged();

                String selectedCategory = binding.categorySpinner.getSelectedItem() != null
                        ? binding.categorySpinner.getSelectedItem().toString()
                        : ALL_CATEGORIES_TEXT;

                filterByCategory(selectedCategory);

                if(binding.swipeRefreshLayout.isRefreshing()) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        lostViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                if (binding.swipeRefreshLayout.isRefreshing()) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }

                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterByCategory(String category) {
        displayList.clear();

        if (category.equals(ALL_CATEGORIES_TEXT)) {
            displayList.addAll(allLostItems);
        } else {
            for (Item item : allLostItems) {
                if (item.getCategory() != null
                        && item.getCategory().equalsIgnoreCase(category)) {
                    displayList.add(item);
                }
            }
        }

        adapter.notifyDataSetChanged();
        toggleEmptyState(displayList.isEmpty());
    }

    private void toggleEmptyState(boolean isEmpty) {
        binding.emptyStateLayout.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.itemRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}