package com.mobileapp.studentnest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.auth.User;

// Creates Entity for table set-up, and this configuration handles linking of
// User Entity and this current entity
@Entity(foreignKeys = @ForeignKey(
        entity = UserEntity.class,
        parentColumns = "user_ID",
        childColumns = "userOwner_ID",
        onDelete = ForeignKey.CASCADE
), indices ={@Index("userOwner_ID")})

public class LoanEntity {
    @PrimaryKey(autoGenerate = true)
    public int loan_ID;

    public int userOwner_ID;

    @ColumnInfo(name = "loanName")
    public String loanName;

    @ColumnInfo(name = "totalLoanAmount")
    public double totalLoanAmount;

    @ColumnInfo(name = "totalInterestAmount")
    public double totalInterestAmount;

    @ColumnInfo(name = "totalLoanTime")
    public int totalLoanTime;

    @ColumnInfo(name = "monthlyLoanPayment")
    public double monthlyLoanPayment;

    @ColumnInfo(name = "paidOffPercentage")
    public Double paidOffPercentage;

    @ColumnInfo(name = "timeRemaining")
    public int timeRemaining;


    public void setUserOwner_ID(int userOwner_ID) {
        this.userOwner_ID = userOwner_ID;
    }

    public int getUserOwner_ID() {
        return userOwner_ID;
    }

    public void setLoanName(String loanName) {this.loanName = loanName;}
    public String getLoanName() {return loanName;}

    public void setTotalLoanAmount(double totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;}
    public double getTotalLoanAmount() {return totalLoanAmount;}

    public void setTotalInterestAmount(double totalInterestAmount) {
        this.totalInterestAmount = totalInterestAmount;
    }
    public double getTotalInterestAmount() {return totalInterestAmount;}

    public void setTotalLoanTime(int totalLoanTime) {
        this.totalLoanTime = totalLoanTime;
    }
    public int getTotalLoanTime() {return totalLoanTime;}

    public void setMonthlyLoanPayment(double monthlyLoanPayment) {
        this.monthlyLoanPayment = monthlyLoanPayment;
    }
    public double getMonthlyLoanPayment() {return monthlyLoanPayment;}

    public void setPaidOffPercentage(double paidOffPercentage) {
        this.paidOffPercentage = paidOffPercentage;
    }
    public double getPaidOffPercentage() {return paidOffPercentage;}

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
    public int getTimeRemaining() {return timeRemaining;}
}
