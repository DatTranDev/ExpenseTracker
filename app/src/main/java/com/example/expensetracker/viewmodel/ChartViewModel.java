package com.example.expensetracker.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.expensetracker.model.TransactionExp;
import java.math.BigDecimal;
import java.util.ArrayList;
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
            if (transactionDate.after(period[0]) && transactionDate.before(period[1])) {
                outcome = outcome.add(transaction.getSpend());
            }
        }
        return outcome;
    }

    private Date[] getCurrentWeekPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date start = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date end = cal.getTime();
        return new Date[]{start, end};
    }

    private Date[] getLastWeekPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        Date start = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date end = cal.getTime();
        return new Date[]{start, end};
    }

    private Date[] getCurrentMonthPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date end = cal.getTime();
        return new Date[]{start, end};
    }

    private Date[] getLastMonthPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date end = cal.getTime();
        return new Date[]{start, end};
    }
}
