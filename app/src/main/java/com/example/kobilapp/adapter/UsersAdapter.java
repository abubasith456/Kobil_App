package com.example.kobilapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kobilapp.R;
import com.example.kobilapp.SdkListener;
import com.example.kobilapp.databinding.ListViewUsersBinding;
import com.example.kobilapp.db.AppDatabase;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.fragment.UsersFragment;
import com.example.kobilapp.model.Users;
import com.example.kobilapp.utils.SharedPreference;
import com.kobil.midapp.ast.api.AstSdk;
import com.kobil.midapp.ast.sdk.AstSdkFactory;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private UsersFragment usersFragment;
    private List<Users> usersList;

    @NonNull
    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListViewUsersBinding listViewUsersBinding = ListViewUsersBinding.inflate(layoutInflater);
        return new UserViewHolder(listViewUsersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UserViewHolder holder, int position) {
        final Users users = usersList.get(position);
        holder.listViewUsersBinding.setUsers(users);
        holder.listViewUsersBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (usersList.size() != 0) {
            return usersList.size();
        } else {
            return 0;
        }
    }

    public void getUsers(List<Users> users) {
        this.usersList = users;
    }

    public void getFragment(UsersFragment usersFragment) {
        this.usersFragment = usersFragment;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        ListViewUsersBinding listViewUsersBinding;

        public UserViewHolder(@NonNull ListViewUsersBinding listViewUsersBinding) {
            super(listViewUsersBinding.getRoot());
            this.listViewUsersBinding = listViewUsersBinding;

            listViewUsersBinding.cardViewUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginFragment loginFragment = new LoginFragment();
                    SharedPreference.getInstance().saveValue(usersFragment.getContext(), "userId", usersList.get(getAdapterPosition()).user_id);
                    FragmentTransaction transaction = usersFragment.getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayoutLoginFragmentContainer, loginFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    SharedPreference.getInstance().saveValue(usersFragment.getContext(), "from", "UsersFragment");
                }
            });

            listViewUsersBinding.linearLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("linearLayoutRemove:", "Pressed");
                    AlertDialog.Builder alert = new AlertDialog.Builder(usersFragment.getContext());
                    alert.setCancelable(true);
                    alert.setMessage("Are you sure do you want to delete " + usersList.get(getAdapterPosition()).user_id + " user?");
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteUser(usersList.get(getAdapterPosition()).user_id);
                            AppDatabase appDatabase = AppDatabase.getDbInstance(usersFragment.getContext());
                            Users users = new Users();
                            users.id = usersList.get(getAdapterPosition()).id;
                            appDatabase.userDao().deleteUser(users);
                            usersList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            Log.e("Print db size:",appDatabase.userDao().getAllUsers().toString());
                        }

                    }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            });
        }
    }
    private void deleteUser(String user_id) {
        SdkListener listener = new SdkListener();
        AstSdk sdk = AstSdkFactory.getSdk(usersFragment.getContext(), listener);
        sdk.doDeactivate(user_id);
    }
}
