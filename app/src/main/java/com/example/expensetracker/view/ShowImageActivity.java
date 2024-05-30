package com.example.expensetracker.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.expensetracker.R;

public class ShowImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ImageView photoView = findViewById(R.id.image_view);
        ImageButton btnBack = findViewById(R.id.btnBack);
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("image_url");

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().error(R.drawable.error))
                .into(photoView);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
