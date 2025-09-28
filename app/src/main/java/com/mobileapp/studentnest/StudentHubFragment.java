package com.mobileapp.studentnest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class StudentHubFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_hub, container, false);
        Spinner spinner = view.findViewById(R.id.semesterSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.terms_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button loansButton = view.findViewById(R.id.loansInfo);
        Button addLoansButton = view.findViewById(R.id.addLoansButton);
        Button billingInfoButton = view.findViewById(R.id.billingInfoButton);

        loansButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_studentHubFragment_to_mainPageFragment);
           }
        });

        addLoansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_studentHubFragment_to_addLoanFragment);
            }
        });

        billingInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_studentHubFragment_to_accountInfoFragment);
            }
        });

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_student_hub, container, false);
        return view;
    }
}