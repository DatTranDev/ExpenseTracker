package com.example.expensetracker.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensetracker.enums.Type;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartViewModel extends ViewModel {
    private MutableLiveData<BigDecimal[]> weeklyOutcomes = new MutableLiveData<>();
    private MutableLiveData<BigDecimal[]> monthlyOutcomes = new MutableLiveData<>();

    public LiveData<BigDecimal[]> getWeeklyOutcomes() {
        return weeklyOutcomes;
    }

    public LiveData<BigDecimal[]> getMonthlyOutcomes() {
        return monthlyOutcomes;
    }

    public void updateWeeklyOutcomes(List<TransactionExp> transactions) {
        BigDecimal[] outcomes = new BigDecimal[2];
        outcomes[0] = getOutcomeForPeriod(transactions, getLastWeekPeriod());
        outcomes[1] = getOutcomeForPeriod(transactions, getCurrentWeekPeriod());
        weeklyOutcomes.setValue(outcomes);
    }

    public void updateMonthlyOutcomes(List<TransactionExp> transactions) {
        BigDecimal[] outcomes = new BigDecimal[2];
        outcomes[0] = getOutcomeForPeriod(transactions, getLastMonthPeriod());
        outcomes[1] = getOutcomeForPeriod(transactions, getCurrentMonthPeriod());
        monthlyOutcomes.setValue(outcomes);
    }

    private BigDecimal getOutcomeForPeriod(List<TransactionExp> transactions, Date[] period) {
        BigDecimal outcome = BigDecimal.ZERO;
        for (TransactionExp transaction : transactions) {
            Date transactionDate = transaction.getCreatedAt();
            if (!isIncomeTransaction(transaction) && transactionDate.after(period[0]) && transactionDate.before(period[1])) {
                outcome = outcome.add(transaction.getSpend());
            }
        }
        return outcome;
    }

    private Date[] getCurrentWeekPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date start = cal.getTime();
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date end = cal.getTime();

        start = Helper.normalizeDate(start, true);
        end = Helper.normalizeDate(end, false);
        return new Date[]{start, end};
    }

    private Date[] getLastWeekPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        Date start = cal.getTime();
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date end = cal.getTime();

        start = Helper.normalizeDate(start, true);
        end = Helper.normalizeDate(end, false);
        return new Date[]{start, end};
    }

    private Date[] getCurrentMonthPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date end = cal.getTime();

        start = Helper.normalizeDate(start, true);
        end = Helper.normalizeDate(end, false);
        return new Date[]{start, end};
    }

    private Date[] getLastMonthPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date end = cal.getTime();

        start = Helper.normalizeDate(start, true);
        end = Helper.normalizeDate(end, false);
        return new Date[]{start, end};
    }

    private boolean isIncomeTransaction(TransactionExp transaction) {
        List<String> incomeCategories = Arrays.asList(Type.KHOAN_THU.getDisplayName(), Type.THU_NO.getDisplayName(), Type.DI_VAY.getDisplayName());
        return incomeCategories.contains(transaction.getCategory().getType());
    }
}
