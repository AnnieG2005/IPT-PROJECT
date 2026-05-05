package com.shruti.lofo.ui.Found;

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

public class FoundFragment extends Fragment {

    private FragmentItemListingBinding binding;
    private FoundViewModel foundViewModel;
    private RecyclerRecentLoFoAdapter adapter;

    private final ArrayList<Item> allFoundItems = new ArrayList<>();
    private final ArrayList<Item> displayList = new ArrayList<>();
    private final String ALL_CATEGORIES_TEXT = "All Categories";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentItemListingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);


        foundViewModel = new ViewModelProvider(requireActivity()).get(FoundViewModel.class);


        setupUI();
        setupRecyclerView();
        setupSpinner();
        setupObservers();



        getParentFragmentManager().setFragmentResultListener(
                "refresh_feed", getViewLifecycleOwner(),
                (requestKey, result) -> foundViewModel.fetchFoundItems(true)
        );



        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            foundViewModel.fetchFoundItems(true);
        });

        binding.addItem.setOnClickListener(v -> {
            CreatePostFragment dialog = CreatePostFragment.newInstance("Found");
            dialog.show(getParentFragmentManager(), "CreatePostDialog");
        });

        foundViewModel.fetchFoundItems(false);

        binding.addItem.setBackground(ContextCompat.getDrawable(requireContext(), R.color.blue_tag));
        binding.addItem.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue_tag));
    }



    private void setupUI() {

        binding.categoryAccent.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.blue_tag)
        );
        binding.categoryTitle.setText("Found Items");
        binding.emptyTitle.setText("No found items yet");
        binding.emptySubtitle.setText("Items reported as found will appear here");
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
        foundViewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            if(loading) {
                binding.lottieAnimation.setVisibility(View.VISIBLE);
            } else {
                binding.lottieAnimation.setVisibility(View.GONE);
            }
        });


        foundViewModel.getFoundItems().observe(getViewLifecycleOwner(), items -> {
            if(items != null) {
                allFoundItems.clear();
                allFoundItems.addAll(items);
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

        foundViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
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
            displayList.addAll(allFoundItems);
        } else {
            for (Item item : allFoundItems) {
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