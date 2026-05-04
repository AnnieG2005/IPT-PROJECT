package com.shruti.lofo.ui.DashBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shruti.lofo.R;
import com.shruti.lofo.data.model.Item;

import java.util.ArrayList;

public class RecyclerRecentLoFoAdapter extends RecyclerView.Adapter<RecyclerRecentLoFoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Item> arr_recent_lofo;
    private OnItemClickListener onItemClickListener;



    public RecyclerRecentLoFoAdapter(Context context, ArrayList<Item> arr_recent_lofo) {
        this.arr_recent_lofo = arr_recent_lofo;
        this.context = context;
    }



    /// | interface for item click listener         |
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }



    /// | Set the click listener for this adapter   |
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }



    /// | OVERRIDDEN METHODS                       |
    /// | INFLATE THE LAYOUT FOR EACH ITEM         |
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_lofo, parent, false);
        return new ViewHolder(v);
    }


    /// | BIND THE DATA TO THE VIEWS               |
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item recentItem = arr_recent_lofo.get(position);

        Glide.with(context)
                .load(recentItem.getImage_url())
                .error(R.drawable.sample_img)
                .into(holder.imageURI);

        holder.description.setText(recentItem.getDescription());
        holder.date.setText(recentItem.getDate());
        holder.itemName.setText(recentItem.getItem_name());
        holder.ownerName.setText(recentItem.getFull_name());



        if (recentItem.getType().equalsIgnoreCase("Lost")) {

            holder.tag.setBackgroundColor(ContextCompat.getColor(context, R.color.orange_tag));
            holder.tag.setText("Lost");


        } else {
            holder.tag.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_tag));
            holder.tag.setText("Found");

        }

        /// | SET ON CLICK LISTENER FOR EACH ITEM           |
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(recentItem);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return arr_recent_lofo.size();
    }


    /// | VIEW HOLDER CLASS                         |
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, description, tag,date, ownerName;
        ImageView imageURI;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            ownerName = itemView.findViewById(R.id.ownerName);
            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
            imageURI = itemView.findViewById(R.id.img_lofo_recent);

            tag = itemView.findViewById(R.id.tag);

        }
    }
}
