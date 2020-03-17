package com.lumibricks.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


//import com.google.firebase.firestore.DocumentSnapshot;
//import com.lumibricks.FilterDialogFragment;
//import com.lumibricks.model.BrickModel;
//import com.lumibricks.model.Manufacture;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.lumibricks.MainActivity;
import com.lumibricks.MainActivity;
import com.lumibricks.R;
import com.lumibricks.model.BrickModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.lumibricks.FilterDialogFragment.TAG;
import static java.lang.Integer.numberOfLeadingZeros;
import static java.lang.Integer.parseInt;

//extend BrickModel
public class BrickAdapter extends RecyclerView.Adapter<BrickAdapter.BrickViewHolder> {
    private ArrayList<BrickModel> mBrickList;
    private OnItemclickListener mListener;
    private int expandedPosition = -1;
    private FloatingActionButton floatingActionButton;
    public boolean requestFireUpdate = false;

//    private HashMap<Integer, BrickModel> mFireBrickList;

    public interface OnItemclickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onChange(int change);

    }

    public void setOnItemClickListener(OnItemclickListener listener) {
        mListener = listener;
    }

    public class BrickViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageView mEditImage;
        public RelativeLayout mRelLayEdit;

        public ImageView mDeleteButton;
        public ImageView mSeelButton;
        public ImageView mCreateButton;
        public ImageView m2ClassButton;
        public EditText mAmountEditText;

        InputMethodManager imm;


        public BrickViewHolder(final View itemView, final OnItemclickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.brickItemImage);
            mTextView1 = itemView.findViewById(R.id.BrickItemName);
            mTextView2 = itemView.findViewById(R.id.brickItemAmountText);
            mEditImage = itemView.findViewById(R.id.brickItemEditImage);
            mRelLayEdit = itemView.findViewById(R.id.relLay_list_item_brick_edit);

            mDeleteButton = itemView.findViewById(R.id.brickItemImageDelete);
            mSeelButton = itemView.findViewById(R.id.brickItemImageSell);
            mCreateButton = itemView.findViewById(R.id.brickItemImageManufacture);
            m2ClassButton = itemView.findViewById(R.id.brickItemImage2_Quality);
            mAmountEditText = itemView.findViewById(R.id.brickEditTextQualityQuantityTransfer);

            itemView.clearFocus();
//            imm = (InputMethodManager) itemView. getSystemService(Context.INPUT_METHOD_SERVICE);
            imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

//            mEditImage.setOnClickListener(this);
//            itemView.setOnClickListener(this);
//            mDeleteButton.setOnClickListener(this);
//            mSeelButton.setOnClickListener(this);
//            mCreateButton.setOnClickListener(this);
//            m2ClassButton.setOnClickListener(this);
//            mAmountEditText.setOnClickListener(this);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkListener(listener, "mDestroy");
                }
            });

            mSeelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkListener(listener, "mSeal");
                }
            });

            mCreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkListener(listener, "mProduce");
                }
            });

            m2ClassButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkListener(listener, "m2Class");
                }
            });

            mEditImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( listener != null ) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            if (expandedPosition == position) {
                                Log.d(TAG, "onClick: same position");
                            }else if(expandedPosition >= 0){
                                int prev = expandedPosition;
                                notifyItemChanged(prev);
                                mAmountEditText.clearFocus();
                            }
                            // Set the current position to "expanded"
                            expandedPosition = position;
                            notifyItemChanged(expandedPosition);
                            mAmountEditText.setInputType(numberOfLeadingZeros(0));
//                            imm.showSoftInput(mAmountEditText, InputMethodManager.RESULT_SHOWN);
                            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
//                            imm.showSoftInput(mAmountEditText, InputMethodManager.SHOW_FORCED);
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( listener != null ) {
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION) {
                            if (expandedPosition == position) {
                                mRelLayEdit.setVisibility(View.GONE);

                                Log.d(TAG, "onClick: same position");
                            }else if(expandedPosition >= 0){
                                int prev = expandedPosition;
                                notifyItemChanged(prev);
                            }
                            Log.d(TAG, "onClick: ItemView:" + position);
                            listener.onItemClick(position);
                            mRelLayEdit.setVisibility(View.GONE);
                            expandedPosition = -1;
                            mAmountEditText.clearFocus();
                            imm.hideSoftInputFromWindow(itemView.getWindowToken(), 0);
//                            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            notifyItemChanged(expandedPosition);
                            //Make sure that edit Layout collapse


                        }
                    }
                }
            });

        }
        void checkListener(OnItemclickListener listener1, String image){
            if(listener1 != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
//                    notifyItemChanged(expandedPosition);
//                    checkListener(listener1);
                    checkIfNotEmpty(image, position);
                    listener1.onChange(position);
                }
            }
        }

        void checkIfNotEmpty(String image, int pos){
            if(mAmountEditText.getText().toString().equals("")){
                Log.d(TAG, "onClick: EditText(null or empty): " + mAmountEditText.getText().toString());

                //TODO notified a user about empty field
            }else {
                Log.d(TAG, "onClick: Good job EditText .equal: " + mAmountEditText.getText());
                firebaseExistingDocAdapter(image, pos);

            }
        }

        void firebaseExistingDocAdapter(String image, int position){
            //Text view brick amount
            String editTextAmount = mAmountEditText.getText().toString();
            int editableAmount = Integer.parseInt(editTextAmount);

            //Adapter brick amount
            BrickModel currentBrick = mBrickList.get(position);
            int currentBrickAmount = Integer.parseInt(currentBrick.getAmount());

            //Compare brick amounts(to avoid nul point exceptions)
            boolean inputSmaller = editableAmount <= currentBrickAmount;

            Log.d(TAG, "firebaseExistingDocAdapter: currentAmount: " + currentBrickAmount);

            //Make bool to show necesary update
            if (image.equals("mDestroy") & inputSmaller){
//                requestFireUpdate = true;
                //TODO should make graveyard? // make who put it there and why..?(*inManufacturing*atClient*wrongData*fallInStacking*...**)
                Log.d(TAG, "firebaseExistingDocAdapter: mDestroy: " + editTextAmount);

                int newBrickAmount = currentBrickAmount - editableAmount;
                writeNewBrickAmount(newBrickAmount, currentBrick);

            } else if(image.equals("mSeal") && inputSmaller){
//                requestFireUpdate = true;

                Log.d(TAG, "firebaseExistingDocAdapter: mSeal: " + editTextAmount);
                int newBrickAmount = currentBrickAmount - editableAmount;
                writeNewBrickAmount(newBrickAmount, currentBrick);

                //TODO make "create new purchase"


            } else if (image.equals("mProduce")){
//                requestFireUpdate = true;

                Log.d(TAG, "firebaseExistingDocAdapter: mProduce: " + editTextAmount);

                int newBrickAmount = currentBrickAmount + editableAmount;
                writeNewBrickAmount(newBrickAmount, currentBrick);


            } else if (image.equals("m2Class") && inputSmaller){
                if(currentBrick.getBrickName().endsWith("1")){
//                    requestFireUpdate = true;

                    Log.d(TAG, "firebaseExistingDocAdapter: m2Class: " + editTextAmount);

                    int newBrickAmount = currentBrickAmount - editableAmount;
                    writeNewBrickAmount(newBrickAmount, currentBrick);
                    writeNew2ClassBrick(editTextAmount, currentBrick);
                } else {
                    Log.d(TAG, "firebaseExistingDocAdapter: m2Class already exist");
                }


            } else {
                Log.wtf(TAG, "firebaseExistingDocAdapter: Something went wrong!\n" +
                        "(Most likely you inserted too big number)\n");

//                requestFireUpdate = false;

            }
            
            if(requestFireUpdate){
                //TODO create Show updateButton
                Log.d(TAG, "firebaseExistingDocAdapter: update button accessible");


            }

            Collections.sort(mBrickList, BrickModel.StuNameComparator);
            notifyDataSetChanged();
//            notifyItemChanged(position);

//            itemView.clearFocus();
//            InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);

//            imm.hideSoftInputFromWindow(itemView.getWindowToken(), 0);


        }

        private void writeNew2ClassBrick(String editableBrickAmount, BrickModel currentBrick) {
            int brickListSize = mBrickList.size();
            boolean mach2ClassBrick = false;
            int brick2ClassPosition = 0;

//            if(currentBrick.get)
            //brickName to comparison whit brickList if brick exist already in list
            StringBuilder new2BrickName3 = new StringBuilder(currentBrick.getBrickName());
            //Replace last char of Full brick name- of '1' to '2' (1. to 2. class)
            new2BrickName3.setCharAt(currentBrick.getBrickName().lastIndexOf(' ') +1, '2');
            String new2BrickName2 = new2BrickName3.toString();
//            String new2BrickName2 = currentBrick.getBrickName().substring(currentBrick.getBrickName().lastIndexOf(' ') +1, '2');
            for (int i = 0; i<brickListSize; i++){
                if (mBrickList.get(i).getBrickName().equals(new2BrickName2)){
                    mach2ClassBrick = true;
                    brick2ClassPosition = i;
                    //Stop a loop
                    break;
                }
            }
            if (mach2ClassBrick){
                //BrickModel exist, so update amount of brick...
                int updated2ClassAount = Integer.parseInt(mBrickList.get(brick2ClassPosition).getAmount()) + Integer.parseInt(editableBrickAmount);
                String brick2ClasNewAmount = String.valueOf(updated2ClassAount);
                mBrickList.get(brick2ClassPosition).setAmount(brick2ClasNewAmount);
                notifyDataSetChanged();
            } else {
                //Create a new brickModel (2.class)
                BrickModel new2ClassBrick = new BrickModel();
                new2ClassBrick.setAmount(editableBrickAmount);
                new2ClassBrick.setBrickName(new2BrickName2);
                new2ClassBrick.setImageResource(currentBrick.getImageResource());
                mBrickList.add(new2ClassBrick);
            }
            //Already defined..?
//            requestFireUpdate = true;
        }

        void writeNewBrickAmount(int newBrickAmount, BrickModel currentBrick){
            requestFireUpdate = true;
            String newBrickAmountValue = String.valueOf(newBrickAmount);
            Log.d(TAG, "mBrick new value: " + newBrickAmountValue);
            currentBrick.setAmount(newBrickAmountValue);

        }

        @Override
        public void onClick(View v) {
            switch (itemView.getId()){
                case  R.id.brickItemImageDelete:
//                    Log.d(TAG, "BrickViewHolder: " + "yoo1");
                    checkListener(mListener, "error");

                    break;

                case R.id.brickItemImageSell:
                    Log.d(TAG, "BrickViewHolder: " + "yoo2oo2");

                    break;
            }
        }
    }

    public BrickAdapter(ArrayList<BrickModel> brickModels){
        mBrickList = brickModels;
        Collections.sort(mBrickList, BrickModel.StuNameComparator);
    }

    @NonNull
    @Override
    public BrickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_brugis, parent, false);
        BrickViewHolder brickViewHolder = new BrickViewHolder(v, mListener);

        return brickViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BrickViewHolder holder, int position) {
        BrickModel cureentBrick = mBrickList.get(position);

        holder.mImageView.setImageResource(cureentBrick.getImageResource());
        holder.mTextView1.setText(cureentBrick.getBrickName());
        holder.mTextView2.setText(cureentBrick.getAmount());

        if(position == expandedPosition){
            holder.mRelLayEdit.setVisibility(View.VISIBLE);
        } else {
            holder.mRelLayEdit.setVisibility(View.GONE);
        }


        if(requestFireUpdate){
            holder.mImageView.setBackgroundColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return mBrickList.size();
    }

//    public BrickAdapter(HashMap<Integer, BrickModel> brickModels){
//        mFireBrickList = brickModels;
//    }


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
