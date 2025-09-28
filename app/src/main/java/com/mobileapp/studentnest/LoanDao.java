package com.mobileapp.studentnest;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.google.firebase.firestore.auth.User;

import java.util.List;

@Dao
public interface LoanDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    void insertLoan(LoanEntity loan);

    @Query("SELECT * FROM LoanEntity WHERE userOwner_ID = :user_ID")
    List<LoanEntity> getUserLoans(int user_ID);

    @Query("UPDATE LoanEntity SET loanName = :newLoanName WHERE userOwner_ID = :userOwner_ID")
    void updateLoanName(String newLoanName, int userOwner_ID);

    @Query("UPDATE LoanEntity SET totalLoanAmount = :newTotalLoanAmount WHERE userOwner_ID = :userOwner_ID")
    void updateTotalLoanAmount(double newTotalLoanAmount, int userOwner_ID);

    @Query("UPDATE LoanEntity SET totalInterestAmount = :newTotalInterestAmount WHERE userOwner_ID = :userOwner_ID")
    void updateTotalInterestAmount(double newTotalInterestAmount, int userOwner_ID);

    @Query("UPDATE LoanEntity SET totalLoanTime = :newTotalLoanTime WHERE userOwner_ID = :userOwner_ID")
    void updateTotalLoanTime(int newTotalLoanTime, int userOwner_ID);

    @Query("UPDATE LoanEntity SET monthlyLoanPayment = :newMonthlyLoanPayment WHERE userOwner_ID = :userOwner_ID")
    void updateMonthlyLoanPayment(double newMonthlyLoanPayment, int userOwner_ID);

}
