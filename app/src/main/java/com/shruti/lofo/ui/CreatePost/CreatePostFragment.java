package com.shruti.lofo.ui.CreatePost;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.shruti.lofo.R;
import com.shruti.lofo.databinding.FragmentCreatePostBinding;

import java.util.Calendar;

public class CreatePostFragment extends DialogFragment {

    private FragmentCreatePostBinding binding;
    private CreatePostViewModel viewModel;

    private String itemType;
    private ActivityResultLauncher<Intent> imagePickerLauncher;



    /// |  STATIC FACTORY METHOD   |

    public static CreatePostFragment newInstance(String type) {
        CreatePostFragment fragment = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString("TYPE", type);
        fragment.setArguments(args);
        return fragment;
    }



    /// |  LIFECYCLE FUNCTIONS   |

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            itemType = getArguments().getString("TYPE", "Lost");
        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null) {
                        viewModel.setImageUri(result.getData().getData());
                        binding.uploadImageButton.setText("IMAGE SELECTED ✓");
                        binding.uploadImageButton.setBackgroundColor(0xFF4CAF50);
                    }
                }
        );
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);

        /// |  SETUP FUNCTIONS       |
        setupDynamicUI();
        setupSpinner();
        setupDateTimePickers();
        setupClickListeners();
        setupObservers();
    }



    /// | UI SETUP              |

    private void setupDynamicUI() {
        if (itemType.equals("Found")) {

            binding.headerTitle.setText("Report Found Item");
            binding.headerSubtitle.setText("Help someone find their lost belongings");
            binding.submitButton.setText("SUBMIT FOUND ITEM");

            binding.headerContainer.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_tag));
            binding.submitButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue_tag));
            binding.datePickerButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue_tag));
            binding.timePickerButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue_tag));
        } else {
            binding.headerTitle.setText("Report Lost Item");
            binding.headerSubtitle.setText("Let others know about your lost belongings");
            binding.submitButton.setText("SUBMIT LOST ITEM");

            binding.headerContainer.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.maroon_primary));
            binding.submitButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.orange_tag));
            binding.datePickerButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.orange_tag));
            binding.timePickerButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.orange_tag));
        }
    }



    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categories_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(adapter);

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setSelectedCategory(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    /// | DATE & TIME PICKERS   |
    private void setupDateTimePickers() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);

        binding.datePickerButton.setOnClickListener(v ->
                new DatePickerDialog(requireContext(), (datePicker, y, m, d) -> {
                    String date = y + "-"
                            + String.format("%02d", m + 1) + "-"
                            + String.format("%02d", d);
                    viewModel.setSelectedDate(date);
                    binding.selectedDateEditText.setText(date);
                }, year, month, day).show()
        );

        binding.timePickerButton.setOnClickListener(v ->
                new TimePickerDialog(requireContext(), (timePicker, h, minute) -> {
                    int hour12 = (h % 12 == 0) ? 12 : h % 12;
                    String amPm = (h < 12) ? "AM" : "PM";
                    String time = hour12 + ":" + String.format("%02d", minute) + " " + amPm;
                    viewModel.setSelectedTime(time);
                    binding.selectedTimeEditText.setText(time);
                }, hour, min, false).show()
        );
    }


    /// |  CLICK LISTENER       |
    private void setupClickListeners() {
        binding.uploadImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        binding.submitButton.setOnClickListener(v -> {
            String itemName = binding.itemNameEdittext.getText().toString().trim();
            String desc = binding.description.getText().toString().trim();
            String loc = binding.location.getText().toString().trim();

            if (viewModel.validate(itemName, desc, loc)) {
                viewModel.submitItem(itemName, desc, loc, itemType);
            }
        });

        binding.cancelButton.setOnClickListener(v -> dismiss());
        binding.closeButton.setOnClickListener(v -> dismiss());
    }




    /// | OBSERVERS             |

    private void setupObservers() {
        viewModel.submitState.observe(getViewLifecycleOwner(), state -> {
            switch (state) {
                case UPLOADING_IMAGE:
                    binding.submitButton.setEnabled(false);
                    binding.submitButton.setText("UPLOADING IMAGE…");
                    break;

                case SAVING:
                    binding.submitButton.setText("SAVING…");
                    break;

                case SUCCESS:
                    Toast.makeText(getContext(), "Item posted successfully!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().setFragmentResult("refresh_feed", new Bundle());
                    dismiss();
                    break;

                case ERROR:
                    binding.submitButton.setEnabled(true);
                    binding.submitButton.setText("SUBMIT " + itemType.toUpperCase() + " ITEM");
                    break;

                case IDLE:
                default:
                    break;
            }
        });

        viewModel.errorMessage.observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}