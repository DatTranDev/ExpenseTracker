package com.example.expensetracker.view.addTransaction;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class ThousandSeparatorTextWatcher implements TextWatcher {

    private final EditText editText;
    private String current = "";

    public ThousandSeparatorTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Không cần xử lý gì trước khi văn bản thay đổi
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Không cần xử lý gì trong khi văn bản thay đổi
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!s.toString().equals(current)) {
            editText.removeTextChangedListener(this);

            String cleanString = s.toString().replaceAll("[,]", "");

            try {
                double parsed = Double.parseDouble(cleanString);
//                if(parsed>999999999)
//                {
//                    parsed=999999999;
//                }
                String formatted = NumberFormat.getInstance(Locale.US).format(parsed);
                current = formatted;
                editText.setText(formatted);
                editText.setSelection(formatted.length());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            editText.addTextChangedListener(this);
        }
    }
}

