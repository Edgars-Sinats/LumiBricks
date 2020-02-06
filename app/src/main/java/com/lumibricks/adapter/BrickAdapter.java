package com.lumibricks.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


//import com.google.firebase.firestore.DocumentSnapshot;
//import com.lumibricks.FilterDialogFragment;
//import com.lumibricks.model.BrickModel;
//import com.lumibricks.model.Manufacture;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lumibricks.MainActivity;
import com.lumibricks.R;
import com.lumibricks.model.BrickModel;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.lumibricks.FilterDialogFragment.TAG;
import static java.lang.Integer.numberOfLeadingZeros;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.remainderUnsigned;

//extend BrickModel
public class BrickAdapter extends RecyclerView.Adapter<BrickAdapter.BrickViewHolder> {
    private ArrayList<BrickModel> mBrickList;
    private OnItemclickListener mListener;
    private int expandedPosition = -1;

//    private HashMap<Integer, BrickModel> mFireBrickList;

    public interface OnItemclickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onSellClick(int position, int amount);
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

        public BrickViewHolder(View itemView, final OnItemclickListener listener) {
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
                    Log.d(TAG, "onClick: succesful deleted items:? " );


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
                            if (expandedPosition >= 0) {
                                int prev = expandedPosition;
                                notifyItemChanged(prev);
                            }
                            // Set the current position to "expanded"
                            expandedPosition = position;
                            notifyItemChanged(expandedPosition);

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
                            Log.d(TAG, "onClick: ItemView:" + position);
                            listener.onItemClick(position);
                            notifyItemChanged(expandedPosition);
                            //Make sure that edit Layout collapse
                            expandedPosition = -1;

                        }
                    }
                }
            });

        }
        void checkListener(OnItemclickListener listener1, String image){
            if(listener1 != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(expandedPosition);
//                    checkListener(listener1);
                    checkIfNotEmpty(image, position);
                }
            }
        }

        void checkIfNotEmpty(String image, int pos){
            if(mAmountEditText.getText().toString().equals("")){
                Log.d(TAG, "onClick: EditText(null or empty): " + mAmountEditText.getText().toString());
                //TODO notified a user about empty field
            }else {
                Log.d(TAG, "onClick: Good job EditText .equal: " + mAmountEditText.getText());
                firebaseAdapter(image, pos);

            }
        }

        void firebaseAdapter(String image, int position){
            //Text view brick amount
            String editTextAmount = mAmountEditText.getText().toString();
            int editableAmount = Integer.parseInt(editTextAmount);

            //Adapter brick amount
            BrickModel currentBrick = mBrickList.get(position);
            int currentBrickAmount = Integer.parseInt(currentBrick.getAmount());

            //Compare brick amounts(to avoid nul point exceptions)
            boolean inputSmaller = editableAmount <= currentBrickAmount;

            Log.d(TAG, "firebaseAdapter: currentAmount: " + currentBrickAmount);

            if (image.equals("mDestroy") & inputSmaller){
                Log.d(TAG, "firebaseAdapter: mDestroy: " + editTextAmount);

                int newBrickAmount = currentBrickAmount - editableAmount;
                writeNewBrickAmount(newBrickAmount, currentBrick);
//                String newBrickAmountValue = String.valueOf(newBrickAmount);
//                Log.d(TAG, "mBrick new value: " + newBrickAmountValue);
//                currentBrick.setAmount(newBrickAmountValue);

            }else if(image.equals("mSeal") && inputSmaller){
                Log.d(TAG, "firebaseAdapter: mSeal: " + editTextAmount);

                int newBrickAmount = currentBrickAmount - editableAmount;
                writeNewBrickAmount(newBrickAmount, currentBrick);

//                String newBrickAmountValue = String.valueOf(newBrickAmount);
//                Log.d(TAG, "mBrick new value: " + newBrickAmountValue);
//                currentBrick.setAmount(newBrickAmountValue);

            }else if (image.equals("mProduce")){
                Log.d(TAG, "firebaseAdapter: mProduce: " + editTextAmount);

                int newBrickAmount = currentBrickAmount + editableAmount;
                writeNewBrickAmount(newBrickAmount, currentBrick);

//                String newBrickAmountValue = String.valueOf(newBrickAmount);
//                Log.d(TAG, "mDestroy new value: " + newBrickAmountValue);
//                currentBrick.setAmount(newBrickAmountValue);
            } else if (image.equals("m2Class")){
                Log.d(TAG, "firebaseAdapter: m2Class: " + editTextAmount);

                int newBrickAmount = currentBrickAmount - editableAmount;
                writeNewBrickAmount(newBrickAmount, currentBrick);

//                String newBrickAmountValue = String.valueOf(newBrickAmount);
//                Log.d(TAG, "mBrick new value: " + newBrickAmountValue);
//                currentBrick.setAmount(newBrickAmountValue);
            } else {
                Log.wtf(TAG, "firebaseAdapter: Something went wrong!\n");
            }



//            //TODO make if else statement with boolean (inputSmaller)
//            switch (image){
//                case ("mDestroy"):
//                    Log.d(TAG, "firebaseAdapter: Destroy: " + editTextAmount);
//
//                    int newBrickAmount = currentBrickAmount-editableAmount;
//                    String newBrickAmountValue = String.valueOf(newBrickAmount);
//                    Log.d(TAG, "mDestroy new value: " + newBrickAmountValue);
//                    currentBrick.setAmount(newBrickAmountValue);
//
//                    break;
//                case ("mSeal"):
//                    Log.d(TAG, "firebaseAdapter: Seal: " + editTextAmount);
//                    break;
//                case ("mProduce"):
//                    Log.d(TAG, "firebaseAdapter: Produce: " + editTextAmount);
//                    break;
//                case ("m2Class"):
//                    Log.d(TAG, "firebaseAdapter: 2.Class: " + editTextAmount);
//                    break;
//
//                default:
//                    Log.d(TAG, "firebaseAdapter: Error!: " + editTextAmount);
//                    break;
//            }
            notifyItemChanged(position);

            itemView.clearFocus();
            InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(itemView.getWindowToken(), 0);

//            InputMethodManager inputManager = (InputMethodManager) INPUT_METHOD_SERVICE;
//            Android.Views.InputMethods;
//            InputMethodManager imm = getSystemService(INPUT_METHOD_SERVICE);
//            InputMethodManager.HIDE_IMPLICIT_ONLY;
//            mAmountEditText.getText().clear();
        }

        void writeNewBrickAmount(int newBrickAmount, BrickModel currentBrick){
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

//            holder.mSeelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "onClick: ohohoho");
//
//                    if(! holder.mAmountEditText.getText().equals(null)){
//                        Log.d(TAG, "onBindViewHolder: job done");
//                        Log.d(TAG, "onBindViewHolder: aceptable: " +R.string.brick_insert_amount + " is just like: " +  holder.mAmountEditText.getText().toString());
//                    }else {
//                        Log.d(TAG, "onBindViewHolder: fail: " +R.string.brick_insert_amount + "is not as: " +  holder.mAmountEditText.getText().toString());
//                    }
//                }
//            });

        } else {
            holder.mRelLayEdit.setVisibility(View.GONE);
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
