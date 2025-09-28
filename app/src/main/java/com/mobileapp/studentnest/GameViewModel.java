package com.mobileapp.studentnest;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.Executors;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class GameViewModel extends AndroidViewModel {
    private String email;
    private String password;
    private String salt;
    private String studentName;
    private String school;
    private String mailingAddress;
    private String phoneNumber;
    private UserDao userDao;
    private int loanViewing = 0; // For displaying info for a loan

    private LoanDao loanDao;
    private String loanName;
    private double interestRate;
    private int timeInput;
    private double loanAmount;

    ArrayList<HashMap<Object, Object>> loansList = new ArrayList<>();

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setTimeInput(int timeInput) {
        this.timeInput = timeInput;
    }

    public int getTimeInput() {
        return timeInput;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String em) {
        email = em;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pw) {
        password = pw;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String slt) {
        salt = slt;
    }

    public String getStudentName() {return studentName;}

    public void setStudentName(String name) {
        studentName = name;
    }

    public String getSchool() {return school;}

    public void setSchool(String name) {
        school = name;
    }

    public String getMailingAddress() {return mailingAddress;}

    public void setMailingAddress(String address) {
        mailingAddress = address;
    }

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNum) {phoneNumber = phoneNum;}

    public int getLoanViewing() {
        return loanViewing;
    }

    public void setLoanViewing(int loanNum) {
        loanViewing = loanNum;
    }

    public GameViewModel(Application application) {
        super(application);
        UserDatabase db = UserDatabase.getInstance(application);
        userDao = db.userDao();
        loanDao = db.loanDao();
    }

    public void insertUser(UserEntity user) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userDao.insertUser(user);
        });
    }

    public void getUser(String email, GetUserCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity user = userDao.getUserByEmail(email);
            callback.onUser(user);
        });
    }

    public void updateStudentName(String email, String newStudent) {
        Executors.newSingleThreadExecutor().execute(() -> {
            int updatedStudentName;
            updatedStudentName = userDao.updateStudentName(email, newStudent);
            if (updatedStudentName > 0) {
                Log.d("Update", "Student name updated!");
            }
            else {
                Log.d("Update", "Failed, User not found!");
            }
        });
    }

    public void updateSchoolName(String email, String newSchool) {
        Executors.newSingleThreadExecutor().execute(() -> {
            int updatedSchoolName;
            updatedSchoolName = userDao.updateSchoolName(email, newSchool);
            if (updatedSchoolName > 0) {
                Log.d("Update", "School name updated!");
            }
            else {
                Log.d("Update", "Failed, User not found!");
            }
        });
    }

    public void updatePhoneNumber(String email, String newPhoneNumber) {
        Executors.newSingleThreadExecutor().execute(() -> {
            int updatedPhoneNum;
            updatedPhoneNum = userDao.updatePhoneNumber(email, newPhoneNumber);
            if (updatedPhoneNum > 0) {
                Log.d("Update", "Phone number updated!");
            }
            else {
                Log.d("Update", "Failed, User not found!");
            }
        });
    }

    public void updateMailingAddress(String email, String newAddress) {
        Executors.newSingleThreadExecutor().execute(() -> {
            int updatedMailingAddress;
            updatedMailingAddress = userDao.updateMailingAddress(email, newAddress);
            if (updatedMailingAddress > 0) {
                Log.d("Update", "Mailing address updated!");
            }
            else {
                Log.d("Update", "Failed, User not found!");
            }
        });
    }


    public void insertLoan(LoanEntity loan) {
        Executors.newSingleThreadExecutor().execute(() -> {
            loanDao.insertLoan(loan);
        });
    }


    ArrayList<HashMap<Object, Object>> getLoans() {
        return loansList;
    }

    public void addLoan(String name, double interestRate, int months, double loanAmount) {
        HashMap<Object, Object> newLoan = new HashMap<>();
        newLoan.put("name", name);
        newLoan.put("interestRate", interestRate);
        newLoan.put("loanTime", months);
        newLoan.put("amount", loanAmount);
        loansList.add(newLoan);
    }

    public double getTotalLoanAmount() {
        double totalAmount = 0;
        for (HashMap<Object, Object> loan : loansList) {
            Double amount = (Double) loan.get("amount");

            if (amount != null) {
                totalAmount += amount;
            }
        }
        return totalAmount;
    }

    // Gets total monthly payment
    public double getMonthlyLoanAmount() {
        double monthlyAmount = 0;

        for (HashMap<Object, Object> loan : loansList) {
            Double principal = (Double) loan.get("amount");
            Double annualRate = (Double) loan.get("interestRate");
            Integer months = (Integer) loan.get("loanTime");

            if (principal != null && annualRate != null && months != null) {
                double monthlyRate = annualRate / 1200; // Converts a number like 4.5% to 0.045, then divides by 12
                double calculation = (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1);
                monthlyAmount += calculation;
            }
        }

        return monthlyAmount;
    }

    // Gets monthly payment for a single loan
    public double getMonthlyPayment(double principal, double annualRate, int months) {
        double monthlyRate = annualRate / 1200; // Converts a number like 4.5% to 0.045, then divides by 12
        return (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) / (Math.pow(1 + monthlyRate, months) - 1);
    }

    public String hashPassword(String pw, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(pw.toCharArray(), salt.getBytes(), 100000, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException err) {
            throw new RuntimeException("Error hashing password", err);
        }
    }

    public String generateSalt() {
        byte[] slt = new byte[16];
        new SecureRandom().nextBytes(slt);
        return Base64.getEncoder().encodeToString(slt);
    }
}