package com.lumibricks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.lumibricks.model.Manufacture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "ManufactureDialog";

    private FirebaseFirestore mFirestore;
    private CollectionReference mBrickRef;
    private DocumentReference mTestDoc;
    private DocumentReference mWarehauseRef;
    private DocumentReference mOrderRef;
    private Manufacture newManufactureObject;

    String buttonActivityPressed;
    Boolean abooleanOrder;


    interface ManufactureListener {

        void onManufacture(Manufacture manufacture);

    }

    private View mRootView;

    private Spinner mBrickTypeSpinner;
    private Spinner mBrickHeightSpinner;
    public Spinner mBrickColorSpinner;
    private Spinner mBrickLumiSpinner;
    private Switch  mBrickQualitySwitch;
    private ImageView mBrickQualityImage;
    private EditText mBrickCountEditext;
    Integer updatedAmount;
    private List<String> listColors, listType, listHight, listLumi = new ArrayList<String>();
    String publickBrickName = null;


    private ManufactureListener manufactureListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_filters, container, false);
        if(getArguments() != null){
            buttonActivityPressed = getArguments().getString("buttonPressed");
        }else {
            //If not button type, close fragment.
            dismiss();
        }

        mBrickTypeSpinner = mRootView.findViewById(R.id.spinnerBrickItemType);
        mBrickHeightSpinner = mRootView.findViewById(R.id.spinnerBrickItemHeight);
        mBrickColorSpinner = mRootView.findViewById(R.id.spinnerBrickItemColor);
        mBrickLumiSpinner = mRootView.findViewById(R.id.spinnerBrickItemLumi);
        mBrickQualitySwitch = mRootView.findViewById(R.id.switchBrickItemQuality);
        mBrickQualityImage = mRootView.findViewById(R.id.iconBrickItemQuality);
        mBrickCountEditext = mRootView.findViewById(R.id.editTextBrickItemCount);

        if(buttonActivityPressed.equals( "brick_navigation_produce" )) {
            mRootView.findViewById(R.id.buttonManufacture).setOnClickListener(this);
            mRootView.findViewById(R.id.buttonOrder).setVisibility(View.INVISIBLE);
            abooleanOrder = false;

        }else if(buttonActivityPressed.equals( "brick_navigation_order" )) {
            mRootView.findViewById(R.id.buttonOrder).setOnClickListener(this);
            mRootView.findViewById(R.id.buttonManufacture).setVisibility(View.INVISIBLE);
            abooleanOrder = true;

        }else {
            dismiss();
        }
        mRootView.findViewById(R.id.buttonCancel).setOnClickListener(this);

        //Firebase
        mFirestore = FirebaseFirestore.getInstance();
        mBrickRef = mFirestore.collection("bricks");
        mTestDoc = mBrickRef.document("test");
        mWarehauseRef = mBrickRef.document("warehouse");
        //TODO add user
        mOrderRef = mBrickRef.document("orders").collection("users").document("user_231");


        if (mBrickQualitySwitch.isChecked()){
            mBrickQualitySwitch.setText(R.string.brick_2Class);
            mBrickQualityImage.setImageResource(R.drawable.ic_2_class_black_24dp);
        }else {
            mBrickQualitySwitch.setText(R.string.brick_1Class);
            mBrickQualityImage.setImageResource(R.drawable.ic_1_class_black_24dp);
        }

        List<String> lines2 = Arrays.asList(getResources().getStringArray(R.array.brick_color));

        return mRootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ManufactureListener) {
            manufactureListener = (ManufactureListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public void onManufactureClicked() {

            if (manufactureListener == null) {
            Log.d(TAG, "onManufactureClicked: getManufacture");
//            manufactureListener.onManufacture(getManufacture());

            getBrickName();
            updatedAmount = Integer.parseInt(Objects.requireNonNull(getSelectedBrickAmount()));
            Log.d(TAG, "Set brick amount:(1.0) " + updatedAmount);

            if(abooleanOrder){
                //TODO check if that order brick already exist! If it does, fdo a Query, just like a queryExistingDatabase()
                onFirestoreInsert(publickBrickName, mOrderRef);
            }else{
                queryExistingDatabase();
            }

                // would like to put updateAmount as String, because String use (Number of UTF-8 encoded bytes  + 1), but Integer use 8bytes to store them in Firestore.
                //But we need in function, to search for number if they exist. (//TODO Maybe we delete if there is 0 ?)

        }else {
            Log.d(TAG, "onManufactureClicked: Failure" + getManufacture());
        }

        dismiss();
    }

    private String getBrickName(){
        newManufactureObject = new Manufacture();
        newManufactureObject.setBrick(getManufacture().getBrick());
        newManufactureObject.setHeight(getSelectedBrickHeight());
        newManufactureObject.setColor(getSelectedBricColor());
        newManufactureObject.setLumi(getSelectedBrickLumi());
        newManufactureObject.setQuality(getSelectedBrickQuality());
        newManufactureObject.setAmount(getSelectedBrickAmount());

        publickBrickName =
                newManufactureObject.getBrick()+    "_"+
                        newManufactureObject.getHeight()+   "_" +
                        newManufactureObject.getColor()+    "_" +
                        newManufactureObject.getLumi()+    "_" +
                        newManufactureObject.getQuality() ;


        //TODO (9.0) Make a function that check if a brick can exist! (Height/type/lumi/
        String a = newManufactureObject.getBrick();
        if(a == null){
            publickBrickName = null;
        }

        if(publickBrickName != null){
            return publickBrickName;
        }else {
            //TODO Pop up, that say brick is invalid (9.1)
            Log.d(TAG, "getBrickName: Brick is invalid!");
            dismiss();
            return null;
        }

    }

    private void queryExistingDatabase(){

        Query query =  mBrickRef.whereGreaterThanOrEqualTo(publickBrickName,0);
        Log.wtf("Big challenge: ", publickBrickName);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.i(TAG, "onComplete: Task:" + task);

                if(task.isSuccessful()){
                    //Check if there is a brick.amount >= 0
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d(TAG, "onComplete: There exist a " + publickBrickName + " brick in a document");
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        Integer oldBrickAmount = Integer.parseInt( document.getData().get(publickBrickName).toString());
                        updatedAmount = updatedAmount + oldBrickAmount;
                        Log.d(TAG, "Set brick amount:(2.0) " + updatedAmount);

                        //TODO
                        //create new insert (sum);

                    }
                    //TODO
                    //Create a brick (new);
                    Log.d(TAG, "onFailure: There is no brick,. so create a new one!");
                    Log.d(TAG, "Set brick amount:(1.1)(Brick amount wont change): " + updatedAmount);


                }else {
                    Log.d(TAG, "Error getting documents: Should not happen. 1.0", task.getException());

                }

                onFirestoreInsert(publickBrickName, mWarehauseRef);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Should not happen 1.1 \n" + e);

            }
        });

    }


    private void onFirestoreInsert(String fireBrickName, DocumentReference documentReference){
        Log.d(TAG, "This should be 3.0");
        final Map<String, Object> brickW2 = new HashMap<>();
        brickW2.put(fireBrickName, updatedAmount);

        //If document may don`t exist(Only order case-> DocRef: order)
        if(abooleanOrder){
            documentReference.set(brickW2, SetOptions.merge());
        }

        documentReference.update(brickW2).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFail: e:\n" + e);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuc: brickW2 updated 4.0:  " + brickW2);
            }
        });
    }

    private void onCancelClicked(View view) {
        dismiss();
    }


    private String getSelectedBrickType() {
        String selected = (String) mBrickTypeSpinner.getSelectedItem();

        if (getString(R.string.value_brick_types).equals(selected)){
            return null;
        } else {
            return selected;
        }
    }

    private String getSelectedBricColor() {
        String selected = (String) mBrickColorSpinner.getSelectedItem();
        if (getString(R.string.value_brick_color).equals(selected)){
            return null;
        } else {
            return selected;
        }
    }

    private String getSelectedBrickHeight() {
        String selected = (String) mBrickHeightSpinner.getSelectedItem();
        if (getString(R.string.value_brick_height).equals(selected)){
            return null;
        } else {
            return selected;
        }
    }

    private String getSelectedBrickLumi() {
        String selected = (String) mBrickLumiSpinner.getSelectedItem();
        if (getString(R.string.lumi_1).equals(selected)){
            return getString(R.string.lumi_1);
        } else if(selected.equals(getString(R.string.lumi_2))) {
            return getString(R.string.lumi_2);
        } else if(selected.equals(getString(R.string.lumi_3))) {
            return getString(R.string.lumi_3);
        } else {
            return null;
        }
    }
    //TODO check function

    private String getSelectedBrickQuality() {

        Boolean selected = mBrickQualitySwitch.isChecked();
        if (selected.equals(true)){
            //2.class if pressed
            return "2";
        } else {
            //1. normal
            return "1";
        }
    }

    private String getSelectedBrickAmount() {
        String selected = mBrickCountEditext.getText().toString();
        if (getString(R.string.brick_insert_amount).equals(selected)){
            return null;
        } else {
            return selected;
        }
    }

    public Manufacture getManufacture() {
        Manufacture manufacture = new Manufacture();

        if(mRootView != null){

            manufacture.setBrick(getSelectedBrickType());
            manufacture.setHeight(getSelectedBrickHeight());
            manufacture.setColor(getSelectedBricColor());
            manufacture.setLumi(getSelectedBrickLumi());
            manufacture.setAmount(getSelectedBrickAmount());
        }

        return manufacture;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonManufacture:
                Log.d(TAG, "onClick: buttonManufacture");
                onManufactureClicked();
                break;
            case R.id.buttonOrder:
                onManufactureClicked();
                break;
            case R.id.buttonCancel:
                onCancelClicked(v);
                break;
        }
    }
}
