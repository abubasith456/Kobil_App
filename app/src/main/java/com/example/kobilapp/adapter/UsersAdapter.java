package com.example.kobilapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kobilapp.R;
import com.example.kobilapp.databinding.ListViewUsersBinding;
import com.example.kobilapp.fragment.LoginFragment;
import com.example.kobilapp.model.Users;
import com.example.kobilapp.utils.SharedPreference;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private Activity activity;
    private List<Users> usersList;
    private FragmentActivity fragmentActivity;

    public UsersAdapter(Activity activity, List<Users> usersList) {
        this.activity = activity;
        this.usersList = usersList;
    }

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
        return usersList.size();
    }

    public void getFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
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
                    SharedPreference.getInstance().saveValue(activity, "userId", usersList.get(getAdapterPosition()).user_id);
                    FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayoutLoginFragmentContainer, loginFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    SharedPreference.getInstance().saveValue(fragmentActivity, "from", "UsersFragment");
                }
            });
        }
    }
}
