package com.mobileapp.studentnest;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class, LoanEntity.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public static volatile  UserDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract LoanDao loanDao();

    public static UserDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "user_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
