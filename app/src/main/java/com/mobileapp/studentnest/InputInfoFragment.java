package com.mobileapp.studentnest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.Executors;

public class InputInfoFragment extends Fragment {
    private GameViewModel gameViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input_info, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        Button submitButton = view.findViewById(R.id.submitButton);
        EditText studentName = view.findViewById(R.id.studentNameInput);
        EditText schoolName = view.findViewById(R.id.schoolInput);
        EditText mailingAddress = view.findViewById(R.id.mailingAddressInput);
        EditText phoneNumber = view.findViewById(R.id.phoneNumberInput);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate name input
                if (studentName.getText().toString().isEmpty()) {
                    studentName.setError("Please enter your name");
                    return;
                }

                // We'll make the other inputs optional

                gameViewModel.setStudentName(studentName.getText().toString());
                gameViewModel.setSchool(schoolName.getText().toString());
                gameViewModel.setMailingAddress(mailingAddress.getText().toString());
                gameViewModel.setPhoneNumber(phoneNumber.getText().toString());

                // TODO: Store information in database

                UserEntity user = new UserEntity();
                user.setEmail(gameViewModel.getEmail());
                user.setPassword(gameViewModel.getPassword());
                user.setSalt(gameViewModel.getSalt());
                user.setStudentName(gameViewModel.getStudentName());
                user.setSchool(gameViewModel.getSchool());
                user.setMailingAddress(gameViewModel.getMailingAddress());
                user.setPhoneNumber(gameViewModel.getPhoneNumber());

                gameViewModel.insertUser(user);

                Navigation.findNavController(view).navigate(R.id.action_inputInfo_to_mainPage);
            }
        });

        return view;
    }
}