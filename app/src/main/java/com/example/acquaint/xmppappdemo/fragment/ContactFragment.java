package com.example.acquaint.xmppappdemo.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.acquaint.xmppappdemo.R;
import com.example.acquaint.xmppappdemo.adapter.ContactAdapter;
import com.example.acquaint.xmppappdemo.model.User;
import com.example.acquaint.xmppappdemo.model.UserResponse;
import com.example.acquaint.xmppappdemo.networking_setup.ApiClass;
import com.example.acquaint.xmppappdemo.networking_setup.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactFragment extends Fragment {

    private ContactAdapter mContactAdapter;
    private List<User> mListOfUser = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        initialiseView(view);
        getListOfUser();
        return view;
    }

    private void getListOfUser() {
        ApiInterface apiInterface = ApiClass.INSTANCE.getRetrofitObject();
        Call<UserResponse> call = apiInterface.getContactList();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    mListOfUser.addAll(userResponse.getUsers());
                    mContactAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {

            }
        });
    }

    private void initialiseView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_contact_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactAdapter = new ContactAdapter(getContext(), mListOfUser);
        recyclerView.setAdapter(mContactAdapter);
    }
}
