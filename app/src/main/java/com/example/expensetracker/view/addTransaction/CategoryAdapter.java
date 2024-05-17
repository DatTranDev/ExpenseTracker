package com.example.expensetracker.view.addTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.expensetracker.R;
import com.example.expensetracker.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Category> data;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public CategoryAdapter(List<Category> list)
    {
        data=list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.layout_category, parent, false);
            return new ParentViewHolder(view,mListener);
        } else {
            View view = inflater.inflate(R.layout.layout_category_child, parent, false);
            return new ChildViewHolder(view,mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Category item = data.get(position);
        if (holder.getItemViewType() == 0) {
            ((ParentViewHolder) holder).bind(item);
        } else {
            ((ChildViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        return 0;
    }
    @Override
    public int getItemViewType(int position) {
        Category item = data.get(position);
        return item.getParentCategoryId() == null ? 0 : 1;
    }

    // ViewHolder cho mục cha
    public static class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public ParentViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.nameCategory);
            image=itemView.findViewById(R.id.imageCategory);
            //sự kiện click
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

        public void bind(Category item) {
            name.setText(item.getName());
            Glide.with(image.getContext())
                    .load(item.getIcon())
                    .apply(new RequestOptions()
//                            .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                            .error(R.drawable.error)) // Error image if loading fails
                    .into(image);
        }
    }

    // ViewHolder cho mục con
    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;


        public ChildViewHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.nameCategoryChild);
            image=itemView.findViewById(R.id.imageCategoryChild);
            //sự kiện click
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

        public void bind(Category item) {
            name.setText(item.getName());
            Glide.with(image.getContext())
                    .load(item.getIcon())
                    .apply(new RequestOptions()
//                            .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                            .error(R.drawable.error)) // Error image if loading fails
                    .into(image);
        }
    }
}

