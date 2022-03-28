package com.example.kobilapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.kobilapp.model.Users;

@Database(entities = {Users.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsersDao userDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "OFFLINE")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }
}
