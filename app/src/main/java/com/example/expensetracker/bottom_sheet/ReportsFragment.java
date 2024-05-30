package com.example.expensetracker.bottom_sheet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetracker.R;
import com.example.expensetracker.enums.Type;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.CustomDrawable;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportsFragment extends BottomSheetDialogFragment {
    private static final String KEY_TRANSACTION = "transaction_list";
    private List<TransactionExp> allTransactions;
    private TabLayout tabLayoutFilter;
    private TextView time;
    private ImageButton nextTime, previousTime;
    private ImageButton btnClose;
    private TextView openingBalance, endingBalance;
    private TextView netIncomeAmount, incomeAmount, outcomeAmount;
    private BarChart netIncomeChart;
    private PieChart incomeChart, outcomeChart;
    private TextView totalBalance;
    private WalletViewModel walletViewModel;

    public static ReportsFragment newInstance(List<TransactionExp> transactionList) {
        ReportsFragment reportsFragment = new ReportsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_TRANSACTION, new ArrayList<TransactionExp>(transactionList));
        reportsFragment.setArguments(bundle);
        return reportsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceiver = getArguments();
        if (bundleReceiver != null) {
            allTransactions = bundleReceiver.getParcelableArrayList(KEY_TRANSACTION);
        }
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_report, null);
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);
        adjustTimePeriod(0);

        walletViewModel.getWalletsLiveData().observe(this, new Observer<List<Wallet>>() {
            @Override
            public void onChanged(List<Wallet> wallets) {
                totalBalance.setText(Helper.formatCurrency(walletViewModel.getTotalBalance()));
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    int maxHeight = getResources().getDisplayMetrics().heightPixels;

                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.height = maxHeight;
                        bottomSheet.setLayoutParams(layoutParams);
                    }
                }
            }
        });

        nextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustTimePeriod(1);
            }
        });

        previousTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustTimePeriod(-1);
            }
        });

        tabLayoutFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateDateRange();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                updateDateRange();
            }
        });

        return bottomSheetDialog;
    }

    private void adjustTimePeriod(int increment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        String[] dateRange = time.getText().toString().split(" - ");
        try {
            Date startDate = dateFormat.parse(dateRange[0]);
            Date endDate = dateFormat.parse(dateRange[1]);

            Calendar calendarStart = Calendar.getInstance();
            Calendar calendarEnd = Calendar.getInstance();
            calendarStart.setTime(startDate);
            calendarEnd.setTime(endDate);

            switch (getFilter()) {
                case "Tuần":
                    calendarStart.add(Calendar.WEEK_OF_YEAR, increment);
                    calendarEnd.add(Calendar.WEEK_OF_YEAR, increment);
                    break;
                case "Tháng":
                    calendarStart.add(Calendar.MONTH, increment);
                    calendarEnd.add(Calendar.MONTH, increment);
                    break;
                case "Năm":
                    calendarStart.add(Calendar.YEAR, increment);
                    calendarEnd.add(Calendar.YEAR, increment);
                    break;
            }

            String newStartDate = Helper.formatDate(calendarStart.getTime());
            String newEndDate = Helper.formatDate(calendarEnd.getTime());
            time.setText(newStartDate + " - " + newEndDate);

            filterTransactions(calendarStart.getTime(), calendarEnd.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateDateRange() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        try {
            Date currentDate = dateFormat.parse(Helper.formatDate(new Date()));
            Calendar calendarStart = Calendar.getInstance(Locale.US);
            Calendar calendarEnd = Calendar.getInstance(Locale.US);
            calendarStart.setFirstDayOfWeek(Calendar.MONDAY);
            calendarEnd.setFirstDayOfWeek(Calendar.MONDAY);
            switch (getFilter()) {
                case "Tuần":
                    calendarStart.set(Calendar.DAY_OF_WEEK, calendarStart.getFirstDayOfWeek());
                    calendarEnd.set(Calendar.DAY_OF_WEEK, calendarEnd.getFirstDayOfWeek());
                    calendarEnd.add(Calendar.DAY_OF_WEEK, 6);
                    break;
                case "Tháng":
                    calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                    calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                    break;
                case "Năm":
                    calendarStart.set(Calendar.DAY_OF_YEAR, 1);
                    calendarEnd.set(Calendar.DAY_OF_YEAR, calendarEnd.getActualMaximum(Calendar.DAY_OF_YEAR));
                    break;
            }
            filterTransactions(calendarStart.getTime(), calendarEnd.getTime());
            time.setText(Helper.formatDate(calendarStart.getTime()) + " - " + Helper.formatDate(calendarEnd.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void filterTransactions(Date startDate, Date endDate) {
        EnumMap<Type, BigDecimal> typeAmounts = new EnumMap<>(Type.class);
        for (Type type : Type.values()) {
            Log.e("Type:", type.getDisplayName());
            typeAmounts.put(type, BigDecimal.ZERO);
        }

        BigDecimal openingBalanceAmount = calculateOpeningBalance(startDate);
        BigDecimal endingBalanceAmount = openingBalanceAmount;

        HashMap<Type, BigDecimal> incomeMap = new HashMap<>();
        HashMap<Type, BigDecimal> outcomeMap = new HashMap<>();


        LinkedHashMap<String, BigDecimal> periodAmountMap = new LinkedHashMap<>();

        startDate = Helper.normalizeDate(startDate, true);
        endDate = Helper.normalizeDate(endDate, false);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startDate);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endDate);

        switch (getFilter()) {
            case "Tuần":
                SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);
                while (calendarStart.before(calendarEnd) || calendarStart.equals(calendarEnd)) {
                    Date currentDay = calendarStart.getTime();
                    String period = dateFormat.format(currentDay);
                    BigDecimal netIncome = calculateNetIncomeForDay(currentDay);
                    periodAmountMap.put(period, netIncome);
                    endingBalanceAmount = endingBalanceAmount.add(netIncome);
                    calendarStart.add(Calendar.DAY_OF_YEAR, 1);
                }
                break;
            case "Tháng":
                while (calendarStart.before(calendarEnd) || calendarStart.equals(calendarEnd)) {
                    int weekStart = calendarStart.get(Calendar.DAY_OF_MONTH);
                    int maxDayOfMonth = calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH);
                    int weekEnd = Math.min(weekStart + 6, maxDayOfMonth);

                    String period = weekStart + "-" + weekEnd;

                    Calendar weekEndCalendar = (Calendar) calendarStart.clone();
                    weekEndCalendar.set(Calendar.DAY_OF_MONTH, weekEnd);

                    BigDecimal netIncome = calculateNetIncomeForWeek(calendarStart.getTime(), weekEndCalendar.getTime());
                    periodAmountMap.put(period, netIncome);
                    endingBalanceAmount = endingBalanceAmount.add(netIncome);
                    calendarStart.add(Calendar.DAY_OF_MONTH, 7);
                }
                break;
            case "Năm":
                while (calendarStart.before(calendarEnd) || calendarStart.equals(calendarEnd)) {
                    int month = calendarStart.get(Calendar.MONTH) + 1;
                    String period = "T"+ month;
                    calendarStart.set(Calendar.DAY_OF_MONTH, calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH));
                    BigDecimal netIncome = calculateNetIncomeForMonth(calendarStart.getTime());
                    periodAmountMap.put(period, netIncome);
                    endingBalanceAmount = endingBalanceAmount.add(netIncome);
                    calendarStart.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            default:
                break;
        }

        calculateAmountsForTypes(startDate, endDate, typeAmounts);

        for (Type type : Type.values()) {
            BigDecimal amount = typeAmounts.get(type);
            if (isIncomeType(type)) {
                incomeMap.put(type, amount);
            } else {
                outcomeMap.put(type, amount);
            }
        }

        updateIncomeChart(incomeMap);
        updateOutcomeChart(outcomeMap);
        updateNetIncomeChart(periodAmountMap);

        if (openingBalanceAmount.compareTo(new BigDecimal(0)) < 0) {
            openingBalance.setText(Helper.formatCurrency(openingBalanceAmount));
            openingBalance.setTextColor(Color.parseColor("#F48484"));
        } else {
            openingBalance.setText(Helper.formatCurrency(openingBalanceAmount));
            openingBalance.setTextColor(Color.parseColor("#00DDB0"));

        }

        if (endingBalanceAmount.compareTo(new BigDecimal(0)) < 0) {
            endingBalance.setText(Helper.formatCurrency(endingBalanceAmount));
            endingBalance.setTextColor(Color.parseColor("#F48484"));
        } else {
            endingBalance.setText(Helper.formatCurrency(endingBalanceAmount));
            endingBalance.setTextColor(Color.parseColor("#00DDB0"));

        }

    }

    private BigDecimal calculateOpeningBalance(Date startDate) {
        BigDecimal openingBalance = BigDecimal.ZERO;
        for (TransactionExp transaction : allTransactions) {
            Date transactionDate = transaction.getCreatedAt();
            if (transactionDate.before(startDate)) {
                BigDecimal transactionAmount = transaction.getSpend();
                if (!isIncomeTransaction(transaction)) {
                    transactionAmount = transactionAmount.negate();
                }
                openingBalance = openingBalance.add(transactionAmount);
            }
        }
        return openingBalance;
    }

    private boolean isIncomeType(Type type) {
        return type == Type.KHOAN_THU || type == Type.THU_NO || type == Type.DI_VAY;
    }

    private void calculateAmountsForTypes(Date startDate, Date endDate, EnumMap<Type, BigDecimal> typeAmounts) {
        for (TransactionExp transaction : allTransactions) {
            Date transactionDate = transaction.getCreatedAt();
            if (!transactionDate.before(startDate) && !transactionDate.after(endDate)) {
                Type transactionType = mapTransactionType(transaction.getCategory().getType());
                BigDecimal transactionAmount = transaction.getSpend();
                typeAmounts.put(transactionType, typeAmounts.get(transactionType).add(transactionAmount));
            }
        }
    }

    private Type mapTransactionType(String type) {
        switch (type) {
            case "Khoản thu":
                return Type.KHOAN_THU;
            case "Khoản chi":
                return Type.KHOAN_CHI;
            case "Cho vay":
                return Type.CHO_VAY;
            case "Đi vay":
                return Type.DI_VAY;
            case "Thu nợ":
                return Type.THU_NO;
            case "Trả nợ":
                return Type.TRA_NO;
            default:
                return null;
        }
    }

    private BigDecimal calculateNetIncomeForDay(Date date) {
        BigDecimal netIncome = BigDecimal.ZERO;

        for (TransactionExp transaction : allTransactions) {
            Date transactionDate = transaction.getCreatedAt();
            if (Helper.isSameDay(transactionDate, date)) {
                BigDecimal transactionAmount = transaction.getSpend();
                if (!isIncomeTransaction(transaction)) {
                    transactionAmount = transactionAmount.negate();
                }
                netIncome = netIncome.add(transactionAmount);
            }
        }
        return netIncome;
    }

    private BigDecimal calculateNetIncomeForWeek(Date startDate, Date endDate) {
        BigDecimal netIncome = BigDecimal.ZERO;


        startDate = Helper.normalizeDate(startDate, true);
        endDate = Helper.normalizeDate(endDate, false);

        for (TransactionExp transaction : allTransactions) {
            Date transactionDate = transaction.getCreatedAt();

            if ((transactionDate.equals(startDate) || transactionDate.after(startDate)) &&
                    (transactionDate.equals(endDate) || transactionDate.before(endDate))) {

                BigDecimal transactionAmount = transaction.getSpend();
                if (!isIncomeTransaction(transaction)) {
                    transactionAmount = transactionAmount.negate();
                }
                netIncome = netIncome.add(transactionAmount);
            }
        }
        return netIncome;
    }


    private BigDecimal calculateNetIncomeForMonth(Date periodDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(periodDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();
        return calculateNetIncomeForWeek(startDate, endDate);
    }

    private boolean isIncomeTransaction(TransactionExp transaction) {
        List<String> incomeCategories = Arrays.asList(Type.KHOAN_THU.getDisplayName(), Type.THU_NO.getDisplayName(), Type.DI_VAY.getDisplayName());
        return incomeCategories.contains(transaction.getCategory().getType());
    }

    private String getIconName(Type type) {
        switch (type) {
            case KHOAN_THU:
                return "ic_salary";
            case KHOAN_CHI:
                return "ic_bill";
            case CHO_VAY:
                return "ic_cho_vay";
            case DI_VAY:
                return "ic_di_vay";
            case THU_NO:
                return "ic_thu_no";
            case TRA_NO:
                return "ic_tra_no";
            default:
                return "ic_question";
        }
    }

    private void updateIncomeChart(Map<Type, BigDecimal> incomeMap) {
        BigDecimal totalIncomeAmount = BigDecimal.ZERO;
        for (BigDecimal amount : incomeMap.values()) {
            totalIncomeAmount = totalIncomeAmount.add(amount);
        }

        incomeAmount.setText(Helper.formatCurrency(totalIncomeAmount));
        incomeAmount.setTextColor(Color.parseColor("#00DDB0"));


        List<PieEntry> entries = new ArrayList<>();
        List<LegendEntry> legendEntries = new ArrayList<>();

        int[] colors;
        if (totalIncomeAmount.equals(BigDecimal.ZERO)) {
            colors = new int[] {Color.parseColor("#ECE9EA")};
            entries.add(new PieEntry(1f, ""));
        } else {
            colors = new int[]{Color.parseColor("#90BE6D"), Color.parseColor("#EF5DA8"), Color.parseColor("#5D5FEF")};
            List<String> legendName = new ArrayList<>();
            for (Map.Entry<Type, BigDecimal> entry : incomeMap.entrySet()) {
                if (!entry.getValue().equals(BigDecimal.ZERO)) {
                    legendName.add(entry.getKey().getDisplayName());
                    BigDecimal percentage = entry.getValue().multiply(BigDecimal.valueOf(100)).divide(totalIncomeAmount, 4, RoundingMode.HALF_UP);
                    String percentageText = percentage.toString() + "%";

                    int iconId = getContext().getResources().getIdentifier(getIconName(entry.getKey()), "drawable", getContext().getPackageName());
                    Drawable icon = ResourcesCompat.getDrawable(getResources(), iconId, null);
                    int iconWidth = 150;
                    int iconHeight = 150;
                    CustomDrawable customIcon = new CustomDrawable(icon, iconWidth, iconHeight);
                    entries.add(new PieEntry(entry.getValue().floatValue(), percentageText, customIcon));
                }
            }

            for (int j = 0; j < legendName.size(); j++) {
                LegendEntry legendEntry = new LegendEntry();
                legendEntry.formColor = colors[j];
                legendEntry.label = String.valueOf(legendName.get(j));
                legendEntries.add(legendEntry);
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setIconsOffset(new MPPointF(0, 60));
        dataSet.setColors(colors);
        dataSet.setSliceSpace(1f);
        dataSet.setDrawValues(false);

        dataSet.setValueFormatter(new ValueFormatter() {
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                PieEntry pieEntry = (PieEntry) entry;
                return pieEntry.getLabel();
            }
        });

        PieData pieData = new PieData(dataSet);
        incomeChart.setData(pieData);

        incomeChart.setHoleRadius(60f);
        incomeChart.setTransparentCircleRadius(65f);
        incomeChart.setHoleColor(Color.TRANSPARENT);
        incomeChart.setTransparentCircleColor(Color.parseColor("#2B2B2B"));

        if (!totalIncomeAmount.equals(BigDecimal.ZERO)) {
            Legend legend = incomeChart.getLegend();
            legend.setCustom(legendEntries);
            legend.setXEntrySpace(40);
        }

        Description description = new Description();
        description.setText("");
        incomeChart.setDescription(description);

        incomeChart.setExtraOffsets(30, 30, 30, 30);
        incomeChart.invalidate();
    }

    private void updateOutcomeChart(Map<Type, BigDecimal> outcomeMap) {
        BigDecimal totalOutcomeAmount = BigDecimal.ZERO;
        for (BigDecimal amount : outcomeMap.values()) {
            totalOutcomeAmount = totalOutcomeAmount.add(amount);
        }

        outcomeAmount.setText(Helper.formatCurrency(totalOutcomeAmount));
        outcomeAmount.setTextColor(Color.parseColor("#F48484"));

        List<PieEntry> entries = new ArrayList<>();
        List<LegendEntry> legendEntries = new ArrayList<>();

        int[] colors;
        if (totalOutcomeAmount.equals(BigDecimal.ZERO)) {
            colors = new int[] {Color.parseColor("#ECE9EA")};
            entries.add(new PieEntry(1f, ""));
        } else {
            colors = new int[] { Color.parseColor("#2D9CDB"), Color.parseColor("#F8961E"), Color.parseColor("#F9C74F") };
            List<String> legendName = new ArrayList<>();
            for (Map.Entry<Type, BigDecimal> entry : outcomeMap.entrySet()) {
                if (!entry.getValue().equals(BigDecimal.ZERO)) {
                    legendName.add(entry.getKey().getDisplayName());
                    BigDecimal percentage = entry.getValue().multiply(BigDecimal.valueOf(100)).divide(totalOutcomeAmount, 4, RoundingMode.HALF_UP);
                    String percentageText = percentage.toString() + "%";

                    int iconId = getContext().getResources().getIdentifier(getIconName(entry.getKey()), "drawable", getContext().getPackageName());
                    Drawable icon = ResourcesCompat.getDrawable(getResources(), iconId, null);
                    int iconWidth = 150;
                    int iconHeight = 150;
                    CustomDrawable customIcon = new CustomDrawable(icon, iconWidth, iconHeight);
                    entries.add(new PieEntry(entry.getValue().floatValue(), percentageText, customIcon));
                }
            }

            for (int j = 0; j < legendName.size(); j++) {
                LegendEntry legendEntry = new LegendEntry();
                legendEntry.formColor = colors[j];
                legendEntry.label = String.valueOf(legendName.get(j));
                legendEntries.add(legendEntry);
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setIconsOffset(new MPPointF(0, 60));
        dataSet.setColors(colors);
        dataSet.setSliceSpace(1f);
        dataSet.setDrawValues(false);

        dataSet.setValueFormatter(new ValueFormatter() {
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                PieEntry pieEntry = (PieEntry) entry;
                return pieEntry.getLabel();
            }
        });


        PieData pieData = new PieData(dataSet);
        outcomeChart.setData(pieData);


        outcomeChart.setHoleRadius(60f);
        outcomeChart.setTransparentCircleRadius(65f);
        outcomeChart.setHoleColor(Color.TRANSPARENT);
        outcomeChart.setTransparentCircleColor(Color.parseColor("#2B2B2B"));

        if (!totalOutcomeAmount.equals(BigDecimal.ZERO)) {
            Legend legend = outcomeChart.getLegend();
            legend.setCustom(legendEntries);
            legend.setXEntrySpace(40);
        }

        Description description = new Description();
        description.setText("");
        outcomeChart.setDescription(description);

        outcomeChart.setExtraOffsets(30, 30, 30, 30);
        outcomeChart.invalidate();
    }

    private void updateNetIncomeChart(LinkedHashMap<String, BigDecimal> periodAmountMap) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> periods = new ArrayList<>(periodAmountMap.keySet());

        List<String> xAxisLabels = new ArrayList<>();

        BigDecimal totalNetIncome = BigDecimal.ZERO;

        int index = 0;
        for (String period : periods) {
            BigDecimal netIncomeAmount = periodAmountMap.get(period);
            entries.add(new BarEntry(index, netIncomeAmount.floatValue()));
            totalNetIncome = totalNetIncome.add(netIncomeAmount);
            xAxisLabels.add(period);
            index++;
        }

        netIncomeAmount.setText(Helper.formatCurrency(totalNetIncome));

        BarDataSet dataSet = new BarDataSet(entries, "Net Income");
        dataSet.setColors(Color.parseColor("#00DDB0"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return Helper.formatCurrency(new BigDecimal(barEntry.getY())).replace("VND", "");
            }
        });

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.3f);
        netIncomeChart.setData(barData);


        XAxis xAxis = netIncomeChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.parseColor("#9AA0A6"));
        xAxis.setTextSize(12f);
        xAxis.setTypeface(ResourcesCompat.getFont(getContext(), R.font.montserrat));
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setLabelCount(xAxisLabels.size());


        netIncomeChart.getAxisLeft().setDrawGridLines(false);
        netIncomeChart.getAxisLeft().setDrawAxisLine(false);
        netIncomeChart.getAxisRight().setDrawGridLines(false);
        netIncomeChart.getAxisRight().setDrawAxisLine(false);

        netIncomeChart.getAxisLeft().setDrawLabels(false);
        netIncomeChart.getAxisRight().setDrawLabels(false);
        netIncomeChart.getAxisRight().setEnabled(false);


        netIncomeChart.getDescription().setEnabled(false);
        netIncomeChart.getLegend().setEnabled(false);

        netIncomeChart.setExtraOffsets(20, 10, 20, 10);
        netIncomeChart.setDrawValueAboveBar(true);
        netIncomeChart.setMaxVisibleValueCount(50);
        netIncomeChart.setDrawGridBackground(false);
        netIncomeChart.setDrawBarShadow(false);

        netIncomeChart.invalidate();
    }




    private String getFilter() {
        TabLayout.Tab tab = tabLayoutFilter.getTabAt(tabLayoutFilter.getSelectedTabPosition());
        String period = tab.getText().toString();
        return period;
    }

    private void initView(View view) {
        tabLayoutFilter = view.findViewById(R.id.filter);
        time = view.findViewById(R.id.time);
        nextTime = view.findViewById(R.id.next_time);
        openingBalance = view.findViewById(R.id.opening_balance);
        endingBalance = view.findViewById(R.id.ending_balance);
        previousTime = view.findViewById(R.id.previous_time);
        totalBalance = view.findViewById(R.id.total_balance);
        btnClose = view.findViewById(R.id.close_report);
        netIncomeAmount = view.findViewById(R.id.net_income_amount);
        incomeAmount = view.findViewById(R.id.income_amount);
        outcomeAmount = view.findViewById(R.id.outcome_amount);
        netIncomeChart = view.findViewById(R.id.net_income_chart);
        outcomeChart = view.findViewById(R.id.outcome_chart);
        incomeChart = view.findViewById(R.id.income_chart);
    }
}
