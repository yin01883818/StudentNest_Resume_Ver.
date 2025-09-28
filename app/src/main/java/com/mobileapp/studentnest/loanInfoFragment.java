package com.mobileapp.studentnest;

import static androidx.core.content.ContextCompat.getSystemService;
import static java.sql.Types.NULL;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class loanInfoFragment extends Fragment {
    private GameViewModel gameViewModel;
    int loanViewingIndex = 0;
    private LoanEntity loan;
    // values that the user enters for notification
    private int userMonth, userDay, userYear = 0;
    private int userHour, userMinute = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loan_info, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        ArrayList<HashMap<Object, Object>> loansList = gameViewModel.getLoans();
        int loanViewing = gameViewModel.getLoanViewing();
        int count = -1;

        EditText loanName = view.findViewById(R.id.LoanTitleTextView);
        EditText originalAmount = view.findViewById(R.id.OriginalLoanTextView);
        EditText interestRate = view.findViewById(R.id.InterestRateTextView);
        TextView loanTime = view.findViewById(R.id.LoanTimeTextView);
        TextView monthlyPaymentAmount = view.findViewById(R.id.MonthlyPaymentTextView);
        TextView paidOffAmount = view.findViewById(R.id.PaidOffTextView);
        TextView timeRemainingLabel = view.findViewById(R.id.TimeRemainingTextView);

        ImageButton saveLoanButton = view.findViewById(R.id.SaveLoanChangesButton);
        ImageButton cancelLoanButton = view.findViewById(R.id.CancelLoanChangesButton);
        ImageButton deleteButton = view.findViewById(R.id.DeleteLoanButton);
        ImageButton notificationButton = view.findViewById(R.id.notificationButton);

        notificationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        cancelLoanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loanInfoFragment_to_mainPageFragment);
            }
        });

        saveLoanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Object, Object> loanIndexInfo = loansList.get(loanViewingIndex);

                if (loanIndexInfo != null) {
                    boolean invalid = false;

                    // Validate inputs
                    if (loanName.getText().toString().isEmpty()) {
                        loanName.setError("Please enter a loan name");
                        invalid = true;
                    }

                    if (originalAmount.getText().toString().isEmpty()) {
                        originalAmount.setError("Please enter the loan amount");
                        invalid = true;
                    }

                    if (interestRate.getText().toString().isEmpty()) {
                        interestRate.setError("Please enter the interest rate");
                        invalid = true;
                    }

                    if (loanTime.getText().toString().isEmpty()) {
                        loanTime.setError("Please enter the loan duration");
                        invalid = true;
                    }

                    /*
                    if (monthlyPaymentAmount.getText().toString().isEmpty()) {
                        monthlyPaymentAmount.setError("Please enter the interest rate");
                        invalid = true;
                    }

                    if (paidOffAmount.getText().toString().isEmpty()) {
                        paidOffAmount.setError("Please enter the interest rate");
                        invalid = true;
                    }

                    if (timeRemainingLabel.getText().toString().isEmpty()) {
                        timeRemainingLabel.setError("Please enter the interest rate");
                        invalid = true;
                    }
                    */

                    // Return if at least one field is invalid
                    if (invalid) {
                        return;
                    }

                    loanIndexInfo.put("name", loanName.getText().toString());
                    loanIndexInfo.put("amount", Double.parseDouble(originalAmount.getText().toString()));
                    loanIndexInfo.put("interestRate", Double.parseDouble(interestRate.getText().toString()));
                    loanIndexInfo.put("loanTime", Integer.parseInt(loanTime.getText().toString()));
                }

                Navigation.findNavController(view).navigate(R.id.action_loanInfoFragment_to_mainPageFragment);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Object, Object> loanIndexInfo = loansList.get(loanViewingIndex);

                if (loanIndexInfo != null) {
                    loansList.remove(loanViewingIndex);
                }

                Navigation.findNavController(view).navigate(R.id.action_loanInfoFragment_to_mainPageFragment);
            }
        });

        if (this.getArguments() != null) {
            loanViewingIndex = this.getArguments().getInt("index");
            Log.d("DEBUG", String.valueOf(loanViewingIndex));
            String nameValue = this.getArguments().getString("name");
            double interestRateValue = this.getArguments().getDouble("interestRate");
            int monthsValue = this.getArguments().getInt("loanTime");
            double loanAmountValue = this.getArguments().getDouble("amount");
            double monthlyPayment = gameViewModel.getMonthlyPayment(loanAmountValue, interestRateValue, monthsValue);
            double truncatedMonthlyPayment = Math.floor(monthlyPayment * 100) / 100.0;

            loanName.setText(nameValue);
            interestRate.setText(String.valueOf(interestRateValue));
            loanTime.setText(String.valueOf(monthsValue));
            originalAmount.setText(String.format("%.2f", loanAmountValue));
            monthlyPaymentAmount.setText(String.valueOf(truncatedMonthlyPayment));
            paidOffAmount.setText(String.valueOf(0));
            timeRemainingLabel.setText(String.valueOf(monthsValue));
        }

        return view;
    }

    private void openDialog() {

        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                userMonth = month;
                userDay = dayOfMonth;
                userYear = year;
                openTimeDialog();
            }
        }, 2025, 0, 15);

        dialog.show();
    }

    private void openTimeDialog() {
        TimePickerDialog dialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                userHour = hourOfDay;
                userMinute = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, userYear);
                calendar.set(Calendar.MONTH, userMonth);
                calendar.set(Calendar.DAY_OF_MONTH, userDay);

                calendar.set(Calendar.HOUR_OF_DAY, userHour);
                calendar.set(Calendar.MINUTE, userMinute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);


                long endDateMillis = calendar.getTimeInMillis();

                startDailyNotifications("Daily Reminder","'" + gameViewModel.getLoanName() + "'" + " loan is due in " +
                                String.valueOf(gameViewModel.getTimeInput()) + " months, with a total of $"
                                + String.valueOf(gameViewModel.getLoanAmount()), endDateMillis,
                        userHour, userMinute);
            }
        }, 15, 58, false);
        dialog.show();
    }

    private void startDailyNotifications(String title, String message, long endDateMillis, int userHour, int userMinute) {
        Calendar calendar = Calendar.getInstance();

        // Set today at 8:30 PM
        calendar.set(Calendar.HOUR_OF_DAY, userHour);
        calendar.set(Calendar.MINUTE, userMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long firstTriggerAtMillis = calendar.getTimeInMillis();

        // If today's 8:30PM has already passed, schedule for tomorrow
        if (firstTriggerAtMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
            firstTriggerAtMillis = calendar.getTimeInMillis();
        }

        // Now schedule the first notification
        Intent intent = new Intent(requireContext(), AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("endDateMillis", endDateMillis);
        intent.putExtra("userHour", userHour);
        intent.putExtra("userMinute", userMinute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                100, // just a unique number for the PendingIntent
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, firstTriggerAtMillis, pendingIntent);
                }
                else {
                    // Ask user to grant permission
                    Intent intent2 = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent2);
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, firstTriggerAtMillis, pendingIntent);
            }
        }
    }
}