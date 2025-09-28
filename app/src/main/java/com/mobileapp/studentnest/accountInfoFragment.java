package com.mobileapp.studentnest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class accountInfoFragment extends Fragment {
    GameViewModel gameViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        Button helpButton = view.findViewById(R.id.helpButton);

       // ImageButton editSchoolButton = view.findViewById(R.id.editSchoolButton);
        //ImageButton editAddressButton = view.findViewById(R.id.editMailingButton);
        EditText studentNameTextBox = view.findViewById(R.id.updateStudentName);
        studentNameTextBox.getText();
        studentNameTextBox.setText(gameViewModel.getStudentName());

        EditText schoolTextBox = view.findViewById(R.id.schoolName);
        schoolTextBox.getText();
        schoolTextBox.setText(gameViewModel.getSchool());

        EditText phoneNumberTextbox = view.findViewById(R.id.phoneNumber);
        phoneNumberTextbox.getText();
        phoneNumberTextbox.setText(phoneNumberFormatter());

        EditText mailingTextBox = view.findViewById(R.id.mailingAddress);
        mailingTextBox.getText();
        mailingTextBox.setText(gameViewModel.getMailingAddress());

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_accountInfoFragment_to_helpFragment);
            }
        });

        studentNameTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                gameViewModel.updateStudentName(gameViewModel.getEmail(), s.toString());
            }
        });

        schoolTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                gameViewModel.updateSchoolName(gameViewModel.getEmail(), s.toString());
            }
        });

        phoneNumberTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                gameViewModel.updatePhoneNumber(gameViewModel.getEmail(), s.toString());
            }
        });

        mailingTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                gameViewModel.updateMailingAddress(gameViewModel.getEmail(), s.toString());
            }
        });


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_account_info, container, false);
        return view;
    }

    public String phoneNumberFormatter() {
        String phoneNum = gameViewModel.getPhoneNumber();
        return phoneNum.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3");
    }
}