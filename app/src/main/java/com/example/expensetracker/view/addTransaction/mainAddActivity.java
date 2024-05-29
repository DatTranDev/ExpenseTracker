package com.example.expensetracker.view.addTransaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Upload.UploadResponse;
import com.example.expensetracker.databinding.ActivityAddTransactionBinding;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.UploadRepository;
import com.example.expensetracker.viewmodel.AddTransactionViewModel;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class mainAddActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private RecyclerView recyclerView;
    private CategoryAdapter myAdapter;

    private ImageButton btnSpend, btnRevenue, btnLoan;
    private String typeTransaction="spend";
    private  RelativeLayout layCategory, layTime, layNote, layWallet, layBorrower,layImage;
    LinearLayout viewImage;
    private ImageView btnBack, iconCategory, imageTransaction,btnDeleteImage;
    private TextView timeTran;
    private EditText money;
    View loading;
    Intent intent= getIntent();
    AddTransactionViewModel addTransactionViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        ActivityAddTransactionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_transaction);
        addTransactionViewModel = new AddTransactionViewModel(this);
        binding.setAddTransactionViewModel(addTransactionViewModel);
        ImageButton btnChoose;
        btnSpend = findViewById(R.id.btn_spend);
        btnRevenue = findViewById(R.id.btn_revenue);
        btnLoan = findViewById(R.id.btn_loan);
        layBorrower= findViewById(R.id.borrower);
        layCategory= findViewById(R.id.chooseCategory);
        layTime= findViewById(R.id.chooseTime);
        layNote= findViewById(R.id.chooseNote);
        layWallet= findViewById(R.id.chooseWallet);
        btnBack= findViewById(R.id.btnBack1);
        timeTran= findViewById(R.id.timeTran);
        iconCategory=findViewById(R.id.icon_category);
        money= findViewById(R.id.edit_text_money);
        layImage= findViewById(R.id.chooseImage);
        loading=findViewById(R.id.loading);
        imageTransaction= findViewById(R.id.imageTransaction);
        btnDeleteImage=findViewById(R.id.btnClose);
        viewImage= findViewById(R.id.layoutImage);
        money.setTextColor(getResources().getColor(R.color.red));
//        btnChoose=btnSpend;
//        btnChoose.setBackgroundResource(R.drawable.choose_type_button);
        btnSpend.setOnClickListener(v -> {
            typeTransaction="spend";
            btnSpend.setBackgroundResource(R.drawable.choose_type_button);
            btnRevenue.setBackgroundResource(R.drawable.default_type_button);
            btnLoan.setBackgroundResource(R.drawable.default_type_button);
            layBorrower.setVisibility(View.GONE);
            money.setTextColor(getResources().getColor(R.color.red));
            resetData();
        });
        btnLoan.setOnClickListener(v -> {
            typeTransaction="loan";
            btnLoan.setBackgroundResource(R.drawable.choose_type_button);
            btnRevenue.setBackgroundResource(R.drawable.default_type_button);
            btnSpend.setBackgroundResource(R.drawable.default_type_button);
            layBorrower.setVisibility(View.VISIBLE);
            money.setTextColor(getResources().getColor(R.color.orange));
            resetData();
        });
        btnRevenue.setOnClickListener(v -> {
            typeTransaction="revenue";
            btnRevenue.setBackgroundResource(R.drawable.choose_type_button);
            btnLoan.setBackgroundResource(R.drawable.default_type_button);
            btnSpend.setBackgroundResource(R.drawable.default_type_button);
            layBorrower.setVisibility(View.GONE);
            money.setTextColor(getResources().getColor(R.color.green));
            resetData();
        });
        layCategory.setOnClickListener(v -> {
            Intent intent2= new Intent(mainAddActivity.this,ChooseCategoryActivity.class);
            intent2.putExtra("typeTrans",typeTransaction);
            startActivityForResult(intent2,69);
        });
        layTime.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog= new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // Lấy dữ liệu ngày tháng năm mà người dùng đã chọn

                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    timeTran.setText(selectedDate);
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                    selectedCalendar.set(Calendar.MILLISECOND, 0);
                    addTransactionViewModel.timeTransaction.set(new Timestamp(selectedCalendar.getTimeInMillis()));
//                    System.out.println("Selected Timestamp: " + addTransactionViewModel.getTimeTransaction().toString());
                }
            }, year, month, day);

            datePickerDialog.show();
        });
        btnBack.setOnClickListener(v -> finish());
//        String a="ic_cho_vay";
//        int resourceId = getResources().getIdentifier(a, "drawable", getPackageName());
//        btnBack.setImageResource(resourceId);
        layWallet.setOnClickListener(v -> {
            ChooseWalletFragment chooseWalletFragment= new ChooseWalletFragment();
            chooseWalletFragment.show(getSupportFragmentManager(), "listWallet");
            chooseWalletFragment.setSend(new ChooseWalletFragment.sendWallet() {
                @Override
                public void selectedWallet(Wallet selected) {
                    addTransactionViewModel.wallet.set(selected);
                }
            });
        });
        layImage.setOnClickListener(v->{
            showPictureDialog();}
        );
        btnDeleteImage.setOnClickListener(v->{
            addTransactionViewModel.image.set("");
            viewImage.setVisibility(View.GONE);
            imageTransaction.setImageResource(R.drawable.background_border_white);
            layImage.setVisibility(View.VISIBLE);
        });

        money.addTextChangedListener(new ThousandSeparatorTextWatcher(money));
        addTransactionViewModel.message.observe(this, message -> {
            if (message != null) {
                if(message.equals("Start"))
                {
                    loading.setVisibility(View.VISIBLE);
                    return;
                }
                else
                {
                    loading.setVisibility(View.GONE);
                }

                // Hiển thị thông báo
                Toast.makeText(mainAddActivity.this, message, Toast.LENGTH_SHORT).show();
                if(message.equals("Thêm giao dịch thành công"))
                {
                    finish();
                }

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==69 && resultCode==1)
        {
            Gson gson= new Gson();
            Category selected= gson.fromJson(data.getStringExtra("selectedString"),Category.class);
            addTransactionViewModel.category.set(selected);
            int resourceId = getResources().getIdentifier(selected.getIcon().getLinking(), "drawable", getPackageName());
            iconCategory.setImageResource(resourceId);
        }
        if (resultCode == RESULT_OK) {
            if(loading!=null)
            {
                loading.setVisibility(View.VISIBLE);
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle the photo captured
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                try {
                    File imageFile=bitmapToFile(imageBitmap);
                    String realPath= imageFile.getAbsolutePath();
                    viewImage.setVisibility(View.VISIBLE);
                    layImage.setVisibility(View.GONE);
                    addTransactionViewModel.image.set(realPath);
                    Glide.with(mainAddActivity.this)
                            .load(realPath)
                            .into(imageTransaction);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if(loading!=null)
                {
                    loading.setVisibility(View.GONE);
                }

            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImageUri = data.getData();
                String realPath = getRealPathFromURI(selectedImageUri);
                addTransactionViewModel.image.set(realPath);
                viewImage.setVisibility(View.VISIBLE);
                layImage.setVisibility(View.GONE);
                Glide.with(mainAddActivity.this)
                        .load(realPath)
                        .into(imageTransaction);

//                uploadImage(realPath);
                if(loading!=null)
                {
                    loading.setVisibility(View.GONE);
                }
            }

        }
    }
    public void resetData()
    {
        addTransactionViewModel.category.set(null);
        addTransactionViewModel.borrower.set(null);
        addTransactionViewModel.node.set(null);
        iconCategory.setImageResource(R.drawable.ic_category);
    }
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Chọn hành động");
        String[] pictureDialogItems = {
                "Chụp ảnh",
                "Chọn ảnh từ thư viện"};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            takePhotoFromCamera();
                            break;
                        case 1:
                            choosePhotoFromGallery();
                            break;
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File bitmapToFile(Bitmap bitmap) throws IOException {
        // Create a file to write bitmap data
        File file = new File(getCacheDir(), "image.png");
        file.createNewFile();

        // Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapData = bos.toByteArray();

        // Write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();

        return file;
    }

    private void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String realPath = cursor.getString(columnIndex);
            cursor.close();
            return realPath;
        }
        return null;
    }






}
