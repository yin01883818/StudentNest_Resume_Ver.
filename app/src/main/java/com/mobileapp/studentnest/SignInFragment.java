package com.mobileapp.studentnest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInFragment extends Fragment {
    private GameViewModel gameViewModel;
    private int toastRequest = 0;
    private Handler toastHandler = new Handler(Looper.getMainLooper());
    private static final int MAX_TOAST_REQ = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        Button submitButton = view.findViewById(R.id.continueButton);
        EditText emailInput = view.findViewById(R.id.accountNameInput);
        EditText passwordInput = view.findViewById(R.id.passwordInput);


        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                gameViewModel.getUser(s.toString(), user -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (user == null) {
                           submitButton.getText();
                           submitButton.setText("Create Account");
                        } else {
                            submitButton.getText();
                            submitButton.setText("Log In");
                        }
                    });
                });
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setEnabled(false);
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Make sure the user typed something
                if (email.isEmpty()) {
                    emailInput.setError("Email cannot be empty");
                    return;
                }

                // Make sure they typed a valid email address
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Please enter a valid email address");
                    return;
                }

                // Make sure they typed a password
                if (password.isEmpty()) {
                    passwordInput.setError("Please enter a password");
                    return;
                }

                // Make sure password is long enough
                if (password.length() < 8) {
                    passwordInput.setError("Password must be at least 8 characters long");
                    return;
                }

                // Make sure password contains an uppercase letter
                if (!password.matches(".*[A-Z].*")) {  // Check for at least one uppercase letter
                    passwordInput.setError("Password must contain at least one uppercase letter");
                    return;
                }

                // Make sure password contains a number
                if (!password.matches(".*[0-9].*")) {  // Check for at least one digit
                    passwordInput.setError("Password must contain at least one number");
                    return;
                }

                // Make sure password has a special character
                if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {  // Check for at least one special character
                    passwordInput.setError("Password must contain at least one special character");
                    return;
                }
                gameViewModel.setEmail(emailInput.getText().toString());
                String salt = gameViewModel.getSalt();
                if (salt == null || salt.isEmpty()) {
                    salt = gameViewModel.generateSalt();
                    gameViewModel.setSalt(salt);
                }
                String hashedPassword = gameViewModel.hashPassword(password, salt);
                gameViewModel.setPassword(hashedPassword); // Hash password with salt

                //handles user validation
                gameViewModel.getUser(email, user -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (user != null) {
                            gameViewModel.setStudentName(user.getStudentName());
                            gameViewModel.setSchool(user.getSchool());
                            gameViewModel.setMailingAddress(user.getMailingAddress());
                            gameViewModel.setPhoneNumber(user.getPhoneNumber());

                            String inputPassword = password;
                            String storedUserSalt = user.getSalt();
                            String storedUserHashedPassword = user.getPassword();
                            String tmpHashedPassword = gameViewModel.hashPassword(inputPassword, storedUserSalt);
                            if (tmpHashedPassword.equals(storedUserHashedPassword)) {
                                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_mainPageFragment2);
                            } else {
                                    String warning = "Wrong password, Please try again.";
                                    toastHandlerPopUp(getContext(), warning); // handles our pop-up warning to end-user. also handles repeats
                            }
                        } else {
                            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_inputInfoFragment);
                        }
                    });
                });
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    submitButton.setEnabled(true);
                }, 500);
            }
        });
        return view;
    }



    private void toastHandlerPopUp(Context context, String message) {
        if (toastRequest >= MAX_TOAST_REQ) {
            Log.d("toastReq", "No Longer Displaying Toast");
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        toastRequest++;
        toastHandler.postDelayed(() ->{
            toastRequest = Math.max(0, toastRequest - 1);
        }, 1000);
    }
}
