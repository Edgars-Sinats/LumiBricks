package com.lumibricks.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lumibricks.model.BrickModel;
import com.lumibricks.model.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private ArrayList<User> mUserList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onEditClick(int position);

    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


}
