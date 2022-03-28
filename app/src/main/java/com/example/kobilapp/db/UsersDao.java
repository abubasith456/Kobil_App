package com.example.kobilapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.kobilapp.model.Users;

import java.util.List;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM users")
    List<Users> getAllUsers();

    @Insert
    void insertUsers(Users... users);

    @Delete
    void deleteUser(Users users);
}
