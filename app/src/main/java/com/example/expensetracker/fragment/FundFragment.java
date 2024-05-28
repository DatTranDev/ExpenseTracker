package com.example.expensetracker.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import com.example.expensetracker.Fund.FundAddFund;
import com.example.expensetracker.Fund.FundAddMember;
import com.example.expensetracker.Fund.FundInformationFund;
import com.example.expensetracker.Fund.FundInformationTransaction;
import com.example.expensetracker.Fund.FundWithdrawFund;
import com.example.expensetracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FundFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FundFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FundFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FundFragment newInstance(String param1, String param2) {
        FundFragment fragment = new FundFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fund, container, false);

        EditText txtHienThiQuy = view.findViewById(R.id.ptxtHienThiQuy);
        txtHienThiQuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FundInformationFund.class);
                startActivity(intent);
            }
        });

        EditText txtHienThiGiaoDich = view.findViewById(R.id.ptxtHienThiGiaoDich);
        txtHienThiGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FundInformationTransaction.class);
                startActivity(intent);
            }
        });

        TableLayout tableThemQuy = view.findViewById(R.id.TableLayoutQuy3);
        tableThemQuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FundAddFund.class);
                startActivity(intent);
            }
        });

        TableLayout tableThemThanhVien = view.findViewById(R.id.TableLayoutThanhVien3);
        tableThemThanhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FundAddMember.class);
                startActivity(intent);
            }
        });

        Button btnRutQuy = view.findViewById(R.id.buttonRutQuy);
        btnRutQuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FundWithdrawFund.class);
                startActivity(intent);
            }
        });

        return view;
    }
}