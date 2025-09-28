package com.mobileapp.studentnest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class mainPageFragment extends Fragment implements RecyclerViewInterface{
    private GameViewModel gameViewModel;
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        TextView totalLoanAmountText = view.findViewById(R.id.TotalLoanAmountTextView);
        TextView monthlyLoanAmountText = view.findViewById(R.id.MonthlyLoanPaymentTextView);
        ImageButton addLoanButton = view.findViewById(R.id.AddLoanImageButton);
       // ImageButton accountInfoButton = view.findViewById(R.id.AccountInfoImageButton);

        // recyclerview holding cards to display loans
        RecyclerView recyclerView = view.findViewById(R.id.loanRecyclerView);
        ArrayList<HashMap<Object, Object>> loanNames = new ArrayList<>();
        for(int i = 0; i < gameViewModel.loansList.size(); i++) {
            loanNames.add(gameViewModel.loansList.get(i));
        }
        LoanRecyclerViewAdapter adapter = new LoanRecyclerViewAdapter(this.getContext(), loanNames, gameViewModel,
                this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // TODO: Fetch values from database if values exist


        if(gameViewModel.getTotalLoanAmount() == 0) {
            totalLoanAmountText.setText(getString(R.string.main_totalLoanAmount) + ": $" + new DecimalFormat("#,###.##").format(gameViewModel.getTotalLoanAmount()));
            monthlyLoanAmountText.setText(getString(R.string.main_monthlyLoanPayment) + ": $" + new DecimalFormat("#,###.##").format(gameViewModel.getMonthlyLoanAmount()));
        }else {
            totalLoanAmountText.setText(getString(R.string.main_totalLoanAmount) + ": $" + new DecimalFormat("#,###.00").format(gameViewModel.getTotalLoanAmount()));
            monthlyLoanAmountText.setText(getString(R.string.main_monthlyLoanPayment) + ": $" + new DecimalFormat("#,###.00").format(gameViewModel.getMonthlyLoanAmount()));
        }

        askNotificationPermission();

        addLoanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_addLoanFragment);
            }
        });

        gameViewModel.getUser(gameViewModel.getEmail(), user -> {
            if (user != null) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        Log.d("UserTest", "Fetched User: " + user.getEmail() + ", ID: " + user.user_ID + ", Pass: " + user.password + ", Salt: "
                                + user.salt + ", StudentName " + user.studentName + ", MailingAddress: " + user.mailingAddress + ", PhoneNumber: " +user.phoneNumber);
                    });
                }, 1000);
            }
        });

        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.RED);
        colorList.add(Color.BLUE);
        colorList.add(Color.GREEN);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.YELLOW);
        colorList.add(Color.CYAN);
        colorList.add(Color.LTGRAY);

        double totalLoanPayment = gameViewModel.getTotalLoanAmount();

        PieChart pieChart = view.findViewById(R.id.loanPieChart);
        pieChart.setUsePercentValues(true);
        List<PieEntry> entries = new ArrayList<>();

        for (HashMap<Object, Object> loan : gameViewModel.loansList) {
            String loanName = (String) loan.get("name");
            Double amount = (Double) loan.get("amount");

            if (amount != null) {
                float percentage = (float)(amount / totalLoanPayment * 100f);
                entries.add(new PieEntry(percentage, loanName));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Loans");

        for (int i = colorList.size(); i < entries.size(); i++) {
            colorList.add(colorList.get(i % colorList.size()));
        }

        dataSet.setColors(colorList);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(16f);

        dataSet.setValueFormatter(new ValueFormatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f%%", value); // No decimals, add "%"
            }
        });

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Loans");
        pieChart.animateY(1000);
        pieChart.invalidate(); // Refresh the chart

        return view;
    }
    // used to have a prompt pop up if the user wants to enable notifications
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }});

    // ask the user for notification permission.
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    // used to keep track of which view was clicked on the recyclerview.
    @Override
    public void onItemClick(int position, HashMap<Object, Object> specificLoan, View view) {
        Bundle bundleWithLoan = new Bundle();
        bundleWithLoan.putInt("index", position);
        bundleWithLoan.putString("name", (String)specificLoan.get("name"));
        bundleWithLoan.putDouble("interestRate", (Double)specificLoan.get("interestRate"));
        bundleWithLoan.putInt("loanTime", (Integer)specificLoan.get("loanTime"));
        bundleWithLoan.putDouble("amount", (Double)specificLoan.get("amount"));
        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_loanInfoFragment, bundleWithLoan);
    }
}