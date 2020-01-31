package com.lumibricks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.google.firebase.firestore.DocumentSnapshot;
//import com.lumibricks.FilterDialogFragment;
//import com.lumibricks.R;
//import com.lumibricks.model.BrickModel;
//import com.lumibricks.model.Manufacture;
//
//import java.lang.annotation.Documented;
//import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lumibricks.R;
import com.lumibricks.model.BrickModel;

import java.util.ArrayList;
import java.util.HashMap;

//extend BrickModel
public class BrickAdapter extends RecyclerView.Adapter<BrickAdapter.BrickViewHolder> {
    private ArrayList<BrickModel> mBrickList;
    private HashMap<Integer, BrickModel> mFireBrickList;

    public static class BrickViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public BrickViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.brickItemImage);
            mTextView1 = itemView.findViewById(R.id.BrickItemName);
            mTextView2 = itemView.findViewById(R.id.brickItemSizeText);
        }
    }

    public BrickAdapter(HashMap<Integer, BrickModel> brickModels){
        mFireBrickList = brickModels;
    }
    public BrickAdapter(ArrayList<BrickModel> brickModels){
        mBrickList = brickModels;
    }

    @NonNull
    @Override
    public BrickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_brugis, parent, false);
        BrickViewHolder brickViewHolder = new BrickViewHolder(v);
        return brickViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BrickViewHolder holder, int position) {
        BrickModel cureentBrick = mBrickList.get(position);

        holder.mImageView.setImageResource(cureentBrick.getmImageResource());
        holder.mTextView1.setText(cureentBrick.getBrickName());
        holder.mTextView2.setText(cureentBrick.getAmount());

    }

    @Override
    public int getItemCount() {
        return mBrickList.size();
    }

//    private int mImgaeResorce;
//    private int amount;
//    private TextView brickFullName;
//
//    private HashMap<Integer, BrickModel> brickList;
//
//
//
//    void setBrickName(String productName) {
//        TextView textView = itemView.findViewById(R.id.BrickItemName);
//        textView.setText(productName);
//    }
//
//    void setBrickAmount(Integer productAmount){
//        TextView textView = itemView.findViewById(R.id.brickItemHeightText);
//        textView.setText(productAmount);
//    }
//
//    void BindView(int position, BrickModel brickModel){
//        setBrickName(brickModel.getBrickName());
//        setBrickAmount(brickModel.getAmount());
//    }
//
//
//
//    public interface onBrickSelectedListener{
//
//        void onBrickSelected(DocumentSnapshot brick);
//
//     }
//
//     public class viewHolder{
//
//     }



}
