package com.example.expensetracker.view.addTransaction;

import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Category> data;
    private OnItemClickListener mListener;
    public static Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void updateCategories(List<Category> newCategories) {
        data = sortCategories(newCategories);
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public CategoryAdapter(Context context, List<Category> list)
    {
        data=sortCategories(list);
        this.context = context;
    }
    public static List<Category> sortCategories(List<Category> categories) {
        List<Category> sortedCategories = new ArrayList<>();
        Map<String, Category> parentMap = new HashMap<>();
        Map<String, List<Category>> childMap = new HashMap<>();

        // Phân loại các category vào parentMap và childMap
        for (Category category : categories) {
            if (category.getParentCategoryId()==null) {
                parentMap.put(category.getId(), category);
            } else {
                childMap.computeIfAbsent(category.getParentCategoryId(), k -> new ArrayList<>()).add(category);
            }
        }

        // Sắp xếp các parent category và thêm vào sortedCategories
        for (Map.Entry<String, Category> entry : parentMap.entrySet()) {
            Category parent = entry.getValue();
            sortedCategories.add(parent);

            // Thêm các child category tương ứng nếu có
            List<Category> children = childMap.get(parent.getId());
            if (children != null) {
                sortedCategories.addAll(children);
            }
        }

        return sortedCategories;
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
            String iconName= item.getIcon().getLinking();
            int resourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            if (resourceId != 0) {
                image.setImageResource(resourceId);
            } else {
                // Xử lý trường hợp icon không tồn tại
                image.setImageResource(R.drawable.error); // Một icon mặc định
            }
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
            String iconName= item.getIcon().getLinking();
            int resourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            if (resourceId != 0) {
                image.setImageResource(resourceId);
            } else {
                // Xử lý trường hợp icon không tồn tại
                image.setImageResource(R.drawable.error); // Một icon mặc định
            }
        }
    }
}

