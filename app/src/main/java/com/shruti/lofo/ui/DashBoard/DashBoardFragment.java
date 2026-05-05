package com.shruti.lofo.ui.DashBoard;

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
import androidx.paging.PagingSource;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.shruti.lofo.R;
import com.shruti.lofo.data.model.Item;
import com.shruti.lofo.databinding.FragmentDashboardBinding;
import com.shruti.lofo.ui.ItemDetails.ItemDetails;
import com.shruti.lofo.utils.SessionManager;

import java.util.ArrayList;

public class DashBoardFragment extends Fragment {


    private FragmentDashboardBinding binding;
    private DashBoardViewModel dashBoardViewModel;
    private RecyclerRecentLoFoAdapter adapter;
    private ArrayList<Item> arr_recent_lofo;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /// |   MODEL INITIALIZATION     |
        dashBoardViewModel = new ViewModelProvider(this).get(DashBoardViewModel.class);


        /// |   FUNCTION CALL         |
        setupImageSlider();
        setupRecyclerView();
        setupObservers();

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            dashBoardViewModel.fetchFeed(true);

        });

        getParentFragmentManager().setFragmentResultListener("refresh_feed", getViewLifecycleOwner(), (requestKey, result) -> {
            dashBoardViewModel.fetchFeed(true);
        });

        dashBoardViewModel.fetchFeed(false);

        setupItemClickListener();
        fetchUserName();
        /// _____________
    }



    /// +===========================+
    /// |       FUNCTIONS           |
    /// +===========================+


    private void setupObservers() {

        dashBoardViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.lottieAnimation.setVisibility(View.VISIBLE);
            } else {
                binding.lottieAnimation.setVisibility(View.GONE);
            }
        });


        dashBoardViewModel.getRecentItem().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                arr_recent_lofo.clear();
                arr_recent_lofo.addAll(items);
                adapter.notifyDataSetChanged();

                if (binding.swipeRefreshLayout.isRefreshing()) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        dashBoardViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                 if (binding.swipeRefreshLayout.isRefreshing()) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }

                 Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }



    /// | IMAGE SLIDER         |
    private void setupImageSlider() {
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.dashboard_img1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.dashboard_img2, ScaleTypes.FIT));

        binding.imageSlider.setImageList(slideModels, ScaleTypes.FIT);
    }



    /// | RECYCLER VIEW        |
    private void setupRecyclerView() {
        arr_recent_lofo = new ArrayList<>();
        adapter = new RecyclerRecentLoFoAdapter(requireContext(), arr_recent_lofo);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false);

        binding.recentLostFoundList.setLayoutManager(gridLayoutManager);
        binding.recentLostFoundList.setAdapter(adapter);
    }



    /// | ITEM CLICK LISTENER   |
    private void setupItemClickListener() {
        adapter.setOnItemClickListener(item -> {
            Intent intent;
            intent = new Intent(requireContext(), ItemDetails.class);
            intent.putExtra("itemId", item.getItem_id());
            startActivity(intent);
        });
    }


    /// | FETCH USER NAME       |
    private void fetchUserName() {
        SessionManager sessionManager = new SessionManager(requireContext());
        binding.userName.setText(sessionManager.getName());
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}