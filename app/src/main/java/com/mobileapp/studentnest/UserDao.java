package com.mobileapp.studentnest;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);

    @Query("SELECT * FROM User WHERE email = :email LIMIT 1")
    UserEntity getUserByEmail(String email);

    @Query("UPDATE User SET password = :newPassword, salt = :newSalt WHERE email = :email")
    int updatePassword(String email, String newPassword, String newSalt);

    @Query("UPDATE User SET studentName = :newStudentName WHERE email = :email")
    int updateStudentName(String email, String newStudentName);

    @Query("UPDATE User SET school = :newSchoolName WHERE email = :email")
    int updateSchoolName(String email, String newSchoolName);

    @Query("UPDATE User SET mailingAddress = :newAddress WHERE email = :email")
    int updateMailingAddress(String email, String newAddress);

    @Query("UPDATE User SET phoneNumber = :newPhoneNumber WHERE email = :email")
    int updatePhoneNumber(String email, String newPhoneNumber);



}
