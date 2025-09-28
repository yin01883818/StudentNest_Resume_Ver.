package com.mobileapp.studentnest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class addLoanFragment extends Fragment {
    private GameViewModel gameViewModel;
    private UserEntity user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_loan, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        EditText loanNameInput = view.findViewById(R.id.LoanNameEditTextText);
        EditText interestRateInput = view.findViewById(R.id.InterestRateEditTextNumberDecimal);
        EditText timeInput = view.findViewById(R.id.TimeEditTextNumber);
        EditText loanAmountInput = view.findViewById(R.id.AmountBorrowedEditTextNumber);
        Button addLoanButton = view.findViewById(R.id.SaveAddButton);
        Button cancelButton = view.findViewById(R.id.CancelButton);

        addLoanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean invalid = false;

                // Validate inputs
                if (loanNameInput.getText().toString().isEmpty()) {
                    loanNameInput.setError("Please enter a loan name");
                    invalid = true;
                }

                if (interestRateInput.getText().toString().isEmpty()) {
                    interestRateInput.setError("Please enter the interest rate");
                    invalid = true;
                }

                if (timeInput.getText().toString().isEmpty()) {
                    timeInput.setError("Please enter the loan duration");
                    invalid = true;
                }

                if (loanAmountInput.getText().toString().isEmpty()) {
                    loanAmountInput.setError("Please enter the loan amount");
                    invalid = true;
                }

                // Return if at least one field is invalid
                if (invalid) {
                    return;
                }
                gameViewModel.setLoanName(loanNameInput.getText().toString());
                gameViewModel.setInterestRate(Double.parseDouble(interestRateInput.getText().toString()));
                gameViewModel.setTimeInput(Integer.parseInt(timeInput.getText().toString()));
                gameViewModel.setLoanAmount(Double.parseDouble(loanAmountInput.getText().toString()));

                gameViewModel.addLoan(
                        loanNameInput.getText().toString(),
                        Double.parseDouble(interestRateInput.getText().toString()),
                        Integer.parseInt(timeInput.getText().toString()),
                        Double.parseDouble(loanAmountInput.getText().toString())
                );

                gameViewModel.getUser(gameViewModel.getEmail(), user -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        LoanEntity loan = new LoanEntity();
                        loan.setUserOwner_ID(user.getUser_ID());
                        loan.setLoanName(gameViewModel.getLoanName());
                        loan.setTotalInterestAmount(gameViewModel.getInterestRate());
                        loan.setTotalLoanTime(gameViewModel.getTimeInput());
                        loan.setTotalLoanAmount(gameViewModel.getTotalLoanAmount());
                        double monthlyPayment = gameViewModel.getMonthlyPayment(gameViewModel.getTotalLoanAmount(),
                                gameViewModel.getInterestRate(), gameViewModel.getTimeInput());
                        double truncatedMonthlyPayment = Math.floor(monthlyPayment * 100) / 100.0;
                        loan.setMonthlyLoanPayment(truncatedMonthlyPayment);
                        loan.setTimeRemaining(gameViewModel.getTimeInput());
                        gameViewModel.insertLoan(loan);
                    });
                });
//                Navigation.findNavController(view).navigate(R.id.action_addLoanFragment_to_mainPageFragment);
                Navigation.findNavController(view).navigate(R.id.action_addLoanFragment_to_mainPageFragment);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_addLoanFragment_to_mainPageFragment);
            }
        });

        return view;
    }
}