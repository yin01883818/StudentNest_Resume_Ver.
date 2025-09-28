package com.mobileapp.studentnest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int user_ID;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "salt")
    public String salt;

    @ColumnInfo(name = "studentName")
    public String studentName;

    @ColumnInfo(name = "school")
    public String school;

    @ColumnInfo(name = "mailingAddress")
    public String mailingAddress;

    @ColumnInfo(name = "phoneNumber")
    public String phoneNumber;

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }
    public int getUser_ID() {
        return user_ID;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getSalt() {
            return salt;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getStudentName() {
        return studentName;
    }

    public void setSchool(String school) {
        this.school = school;
    }
    public String getSchool() {
        return school;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
