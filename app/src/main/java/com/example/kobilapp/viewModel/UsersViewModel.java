package com.example.kobilapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kobilapp.db.AppDatabase;
import com.example.kobilapp.model.Status;
import com.example.kobilapp.model.Users;

import java.util.List;

public class UsersViewModel extends AndroidViewModel {

    MutableLiveData<List<Users>> usersList = new MutableLiveData<>();
    MutableLiveData<List<String>> user = new MutableLiveData<>();

    public UsersViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Users>> getUsersList() {
        try {
            AppDatabase db = AppDatabase.getDbInstance(getApplication());
            List<Users> users = db.userDao().getAllUsers();
            usersList.setValue(users);
        } catch (Exception e) {
            Log.e("Error=> ", e.getMessage());
        }
        return usersList;
    }

    public LiveData<List<String>> getUser() {

        List<String> value = Status.getInstance().getList();
        user.setValue(value);
        return user;
    }
}