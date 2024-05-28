package com.example.expensetracker.view.budget;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Helper;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DetailBudgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_DATE = 0;
    private static final int VIEW_TYPE_TRANSACTION = 1;
    private List<Object> items;
    public static   Context context;

    public DetailBudgetAdapter(List<Object> items, Context context) {
        this.items = items;
        this.context=context;
    }
    public void setData(List<Object> list)
    {
        this.items=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Timestamp) {
            return VIEW_TYPE_DATE;
        } else {
            return VIEW_TYPE_TRANSACTION;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_time_detail_budget, parent, false);
            return new DateViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transaction_detail_budget, parent, false);
            return new TransactionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_DATE) {
            DateViewHolder dateViewHolder = (DateViewHolder) holder;
            dateViewHolder.bind((Timestamp) items.get(position));
        } else {
            TransactionViewHolder transactionViewHolder = (TransactionViewHolder) holder;
            transactionViewHolder.bind((TransactionExp) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView date,weekdays,monthYear;

        public DateViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.text_ngay);
            weekdays= itemView.findViewById(R.id.text_thu);
            monthYear= itemView.findViewById(R.id.text_thang_nam);
        }

        public void bind(Timestamp time) {
            long timestamp= time.getTime();
            // Chuyển đổi timestamp thành LocalDateTime


            // Lấy các thành phần ngày tháng năm
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime dateTime = LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(timestamp),
                        ZoneId.systemDefault()
                );
                String thu = getDayOfWeekInVietnamese(dateTime.getDayOfWeek().toString());
                String ngay = dateTime.format(DateTimeFormatter.ofPattern("dd"));
                String thang = dateTime.format(DateTimeFormatter.ofPattern("MM"));
                String nam= dateTime.format(DateTimeFormatter.ofPattern("yyyy"));
                date.setText(ngay);
                weekdays.setText(thu);
                monthYear.setText("Tháng "+thang+", "+nam);
            }

        }
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView name,price;
        ImageView icon;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.nameCategory);
            price=itemView.findViewById(R.id.text_price);
            icon=itemView.findViewById(R.id.imageCategory);
        }

        public void bind(TransactionExp item) {
            name.setText(item.getCategory().getName());
            String iconName= item.getCategory().getIcon().getLinking();
            int resourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            if (resourceId != 0) {
                icon.setImageResource(resourceId);
            } else {
                // Xử lý trường hợp icon không tồn tại
                icon.setImageResource(R.drawable.error); // Một icon mặc định
            }
            price.setText("- "+Helper.formatMoney(item.getSpend()));
        }
    }
    public static String getDayOfWeekInVietnamese(String dayOfWeek) {
        switch (dayOfWeek) {
            case "MONDAY": return "Thứ Hai";
            case "TUESDAY": return "Thứ Ba";
            case "WEDNESDAY": return "Thứ Tư";
            case "THURSDAY": return "Thứ Năm";
            case "FRIDAY": return "Thứ Sáu";
            case "SATURDAY": return "Thứ Bảy";
            case "SUNDAY": return "Chủ Nhật";
            default: return "";
        }
    }
}
