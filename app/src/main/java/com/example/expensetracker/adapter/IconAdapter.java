package com.example.expensetracker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensetracker.R;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.view.addTransaction.CategoryAdapter;

import java.util.List;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder> {
    private List<Icon> data;
    private OnItemClickListener mListener;
    private Context context;

    public IconAdapter(Context context, List<Icon> list) {
        this.context = context;
        this.data = list;
    }

    // Interface for handling item click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Method to set the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    // Method to update the icon list
    public void updateIcons(List<Icon> newIcons) {
        if (data != null) {
            data.clear();
            data.addAll(newIcons);
        } else {
            data = newIcons;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_item, parent, false);
        return new IconViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        Icon icon = data.get(position);
        if (icon != null) {
            String iconName = icon.getLinking();
            int iconId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            holder.iconImageView.setImageResource(iconId);
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class IconViewHolder extends RecyclerView.ViewHolder{
        private ImageView iconImageView;
        public IconViewHolder(@NonNull View itemView, final IconAdapter.OnItemClickListener listener) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
