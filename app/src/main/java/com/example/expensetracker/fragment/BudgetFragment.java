package com.example.expensetracker.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.expensetracker.R;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.view.addTransaction.ChooseCategoryActivity;
import com.example.expensetracker.view.addTransaction.mainAddActivity;
import com.example.expensetracker.view.budget.AddBudgetActivity;


public class BudgetFragment extends Fragment {

   private Button btnAddBudget;
   Intent intent;
   Context context;
   private ImageView test;


    public BudgetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

         View view= inflater.inflate(R.layout.fragment_budget, container, false);
         context= getContext();
         btnAddBudget= view.findViewById(R.id.buttonAddBudget);
         btnAddBudget.setOnClickListener(v -> {
             intent= new Intent(getActivity(), AddBudgetActivity.class);
//             intent.putExtra("idWallet",typeTransaction);
//
             startActivity(intent);

         });
         test=view.findViewById(R.id.image_test);
         String name="ic_bill";
         int id = getResources().getIdentifier(name,"drawable",getActivity().getPackageName());
         test.setImageResource(id);

        return view;
    }
}