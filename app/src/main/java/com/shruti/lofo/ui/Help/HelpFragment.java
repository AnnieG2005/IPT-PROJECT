package com.shruti.lofo.ui.Help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.shruti.lofo.databinding.FragmentHelpBinding;

public class HelpFragment extends Fragment {
    private FragmentHelpBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHelpBinding.inflate( getLayoutInflater());
        View root = binding.getRoot();

        binding.que1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFaq(binding.ans1, binding.divider1, binding.arrow1);
            }
        });

        binding.que2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFaq(binding.ans2, binding.divider2, binding.arrow2);
            }
        });

        binding.que3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFaq(binding.ans3, binding.divider3, binding.arrow3);
            }
        });


        binding.que4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFaq(binding.ans4, binding.divider4, binding.arrow4);
            }
        });


        return root;
    }


    private void toggleVisibility(TextView textView) {
        int currentVisibility = textView.getVisibility();
        textView.setVisibility(currentVisibility == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private void toggleFaq(View answerView, View divider, ImageView arrow) {
        boolean isExpanded = answerView.getVisibility() == View.VISIBLE;
        answerView.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        divider.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        arrow.animate()
                .rotation(isExpanded ? 0f : -90f)
                .setDuration(200)
                .start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
