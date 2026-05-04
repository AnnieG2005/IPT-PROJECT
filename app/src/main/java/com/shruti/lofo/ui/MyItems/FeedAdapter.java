package com.shruti.lofo.ui.MyItems;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shruti.lofo.R;
import com.shruti.lofo.api.ApiService;
import com.shruti.lofo.api.RetrofitClient;
import com.shruti.lofo.data.model.Item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ItemViewHolder> {

    /// +===========================+
    /// |   LISTENER INTERFACE      |
    /// +===========================+

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }



    private final Context context;
    private List<Item> itemList = new ArrayList<>();
    private final boolean showDeleteButton;
    private OnItemClickListener onItemClickListener;

    /// +===========================+
    /// |   CONSTRUCTOR             |
    /// +===========================+

    public FeedAdapter(Context context, boolean showDeleteButton) {
        this.context = context;
        this.showDeleteButton = showDeleteButton;
    }

    /// +===========================+
    /// |    SETTERS                |
    /// +===========================+

    public void setItems(List<Item> newItems) {
        this.itemList = newItems;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /// +===========================+
    /// |    RECYCLER VIEW         |
    /// +===========================+

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_item_lofo, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        bindBasicInfo(holder, item);
        bindDeleteButton(holder);
        bindStatusButton(holder, item);
        bindCardClick(holder, item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /// +===========================+
    /// |   BINDING FUNCTIONS       |
    /// +===========================+

    private void bindBasicInfo(ItemViewHolder holder, Item item) {
        holder.titleText.setText(item.getItem_name());
        holder.dateText.setText(item.getDate());

        Glide.with(context)
                .load(item.getImage_url())
                .centerCrop()
                .into(holder.itemImage);
    }

    private void bindCardClick(ItemViewHolder holder, Item item) {
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    private void bindDeleteButton(ItemViewHolder holder) {
        if (holder.deleteButton == null) return;

        holder.deleteButton.setVisibility(showDeleteButton ? View.VISIBLE : View.GONE);

        if (!showDeleteButton) return;

        holder.deleteButton.setOnClickListener(v -> {
            int position = holder.getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            deleteItemAt(position);
        });
    }

    private void bindStatusButton(ItemViewHolder holder, Item item) {
        if (holder.statusButton == null) return;

        boolean isSolved = "Solved".equals(item.getStatus());

        if (isSolved) {
            holder.statusButton.setText("Already Solved");
            holder.statusButton.setEnabled(false);
            holder.statusButton.setTextColor(Color.DKGRAY);
            holder.statusButton.setStrokeColor(
                    android.content.res.ColorStateList.valueOf(Color.DKGRAY)
            );
        } else {
            holder.statusButton.setText("Mark as Solved");
            holder.statusButton.setEnabled(true);
            holder.statusButton.setOnClickListener(v -> {
                int position = holder.getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                markAsSolved(position);
            });
        }
    }

    /// +===========================+
    /// |   CRUD CALLS              |
    /// +===========================+

    private void deleteItemAt(int position) {
        int itemId = itemList.get(position).getItem_id();
        ApiService apiService = RetrofitClient.getApiService(context);

        apiService.deleteItem(itemId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    itemList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, itemList.size());
                    Toast.makeText(context, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    logError("DeleteItem", response);
                    Toast.makeText(context, "Failed to delete item.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error. Could not delete.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markAsSolved(int position) {
        int itemId = itemList.get(position).getItem_id();
        String newStatus = "Solved";
        ApiService apiService = RetrofitClient.getApiService(context);

        apiService.updateItemStatus(itemId, newStatus).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    itemList.get(position).setStatus(newStatus);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Marked as Solved!", Toast.LENGTH_SHORT).show();
                } else {
                    logError("UpdateStatus", response);
                    Toast.makeText(context, "Failed to update status.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /// +===========================+
    /// |     UTILITY               |
    /// +===========================+

    private void logError(String tag, Response<?> response) {
        try {
            if (response.errorBody() != null) {
                Log.e(tag, "Server error " + response.code() + ": " + response.errorBody().string());
            }
        } catch (Exception e) {
            Log.e(tag, "Could not parse error body", e);
        }
    }


    /// +===========================+
    /// |  VIEW HOLDER CLASS        |
    /// +===========================+
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView titleText;
        final TextView dateText;
        final ImageView itemImage;
        final ImageButton deleteButton;
        final com.google.android.material.button.MaterialButton statusButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.itemTitle);
            dateText = itemView.findViewById(R.id.itemDate);
            itemImage = itemView.findViewById(R.id.itemImage);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            statusButton = itemView.findViewById(R.id.statusButton);
        }
    }
}