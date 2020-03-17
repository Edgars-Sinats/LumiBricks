package com.lumibricks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.lumibricks.adapter.BrickAdapter;
import com.lumibricks.adapter.FirestoreAdapter;
import com.lumibricks.model.BrickModel;
import com.lumibricks.model.Manufacture;
import com.lumibricks.order_list.AddRemoveExpandableExampleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

//implements
//FilterDialogFragment.ManufactureListener,

public class MainActivity extends AppCompatActivity implements
                View.OnClickListener{

    ArrayList<BrickModel> brickArrayList;

    private static final String TAG = "MainActivity";

    private FirebaseFirestore mFirestore;
    private DocumentReference mWarehauseRef;
    private DocumentReference mWarehauseRefTest;
    private FloatingActionButton floatingUpdateButton;
    private Manufacture newManufactureObject;

    private BrickAdapter mBrickAdapter;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int RC_SIGN_IN = 9001;

    private static final int LIMIT = 20;
    private int imageInt;

    private Toolbar mToolbar;
//    private BottomNavigationView menuBrickAction;
    private RecyclerView mBricksRecycler;
    private ImageView mButtonRemove;
    private CardView mFilterBar;

    private FilterDialogFragment mDialogFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        mToolbar = findViewById(R.id.toolbar);
//        mFrameLayout
        mButtonRemove = findViewById(R.id.buttonClearFilter);
        mFilterBar = findViewById(R.id.filterBar);
        floatingUpdateButton = findViewById(R.id.floatingUpload);

//        mBricksRecycler.setLayoutManager(new LinearLayoutManager(this));


        findViewById(R.id.bottom_nav_view).setOnClickListener(this);
        findViewById(R.id.brick_navigation_produce).setOnClickListener(this);
        findViewById(R.id.brick_navigation_sold).setOnClickListener(this);
        findViewById(R.id.brick_navigation_order).setOnClickListener(this);

        mFirestore = FirebaseFirestore.getInstance();
        mWarehauseRef = mFirestore.collection("bricks").document("warehouse");
        mWarehauseRefTest = mFirestore.collection("bricks").document("test");

//        menuBrickAction = findViewById(R.id.bottom_nav_view);
//        menuBrickAction.getSelectedItemId();

        mDialogFragment = new FilterDialogFragment();

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: " + item);
                return false;
            }
        });

//        checkIfUploadTrue();

        //TODO make floating button when brickArrayList has been changed.
        // FloatingButton pushToBricksTOFirestore;
        //Make insert in database with new brickAdapter
        mButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddRemoveExpandableExampleActivity.class);
                startActivity(intent);//                insertDocInFirestore("s");
                //TODO will create update whit new document
            }
        });

        floatingUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFirestore();
                floatingUpdateButton.hide();

            }
        });

    }

    private void updateFirestore() {
        Integer brickItemValue;
        String fullBrickName;
        String transformedFullBrickName;
        int brickListSize = brickArrayList.size();

        final Map<String, Object> brickW3 = new HashMap<>();

        for (int i =0; i<brickListSize; i++){
            fullBrickName = brickArrayList.get(i).getBrickName();
//            transformedFullBrickName = fullBrickName;
            transformedFullBrickName = fullBrickName.replace(" ", "_");
            brickItemValue = Integer.parseInt(brickArrayList.get(i).getAmount());

            //Do not push data if amount is 0 (continue)
            if (brickItemValue.equals(0)){
                Log.d(TAG, "updateFirestore: value0 true!!!\n\n:" + brickItemValue);
                continue;
            }
            Log.d(TAG, "updateFirestore: new Brick\n\n: " + brickItemValue);
            brickW3.put(transformedFullBrickName, brickItemValue);

        }



        mWarehauseRef.set(brickW3).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFail: e:\n" + e);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuc: brickW3 created 6.0:  " + brickW3);
            }
        });

    }

    public void insertDocInFirestore(String amount){
        Integer brickItemValue;
        String fullBrickName;
        int brickListSize = brickArrayList.size();

        final Map<String, Object> brickW2 = new HashMap<>();

        for (int i =0; i<brickListSize; i++){
            fullBrickName = brickArrayList.get(i).getBrickName();
            brickItemValue = Integer.parseInt(brickArrayList.get(i).getAmount());
            brickW2.put(fullBrickName, brickItemValue);
        }




        mWarehauseRefTest.set(brickW2).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFail: e:\n" + e);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuc: brickW2 created 5.0:  " + brickW2);
            }
        });
    }

    private void deleteRow(int position) {

        brickArrayList.remove(position);
        mBrickAdapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, int image) {
        brickArrayList.get(position).changeImage(image);
        mBrickAdapter.notifyItemChanged(position);
    }

    public void openItem(int position) {


    }




    public void onManufactureClicked(String buttonName) {
        Log.d(TAG, "onManufactureClicked: So far so good.");

        Bundle bundle = new Bundle();
        bundle.putString("buttonPressed", buttonName);
        mDialogFragment.setArguments(bundle);
        mDialogFragment.show((MainActivity.this).getSupportFragmentManager(), FilterDialogFragment.TAG);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

//          TODO check if this can make be used ?
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.brick_navigation_sold:
//
//                Log.d(TAG, "onOptionsItemSelected: 1");
//                break;
//            case R.id.brick_navigation_produce:
//                onManufactureClicked("brick_navigation_produce");
//                Log.d(TAG, "onOptionsItemSelected: 2");
//                break;
//            case R.id.brick_navigation_order:
//                onManufactureClicked("brick_navigation_order");
//                Log.d(TAG, "onOptionsItemSelected: 3");
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void getDocument(){
        mWarehauseRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot , @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    processDocument(snapshot);

                } else {
                    Log.d(TAG, "Current data: null");
                }

            }
        });
    }



    public void processDocument(DocumentSnapshot brickDoc){

        HashMap<Integer, BrickModel> brickList = new HashMap<>();
        brickArrayList = new ArrayList<>();

        String key;
        String value;
        int num = 0;

//        ArrayList<String> keyList = new ArrayList<>();
        //ToDO when project Run again, document has`t downloded new file from firestore. When delated row record from firestore,
        // its not showed in device.(Still old recodrs remaining, eaven when app is restardet and unistaled!)
        // Only when project is rebuilding after (5min) there are new changes shown  in a phone!
        Iterator myIter = brickDoc.getData().keySet().iterator();
        while (myIter.hasNext()){
            num = num+1;
            key=(String) myIter.next();
            value = brickDoc.getData().get(key).toString();

            BrickModel brickMode2 = new BrickModel();

            brickMode2.setAmount(value);

            //TODO transform key
            String[] trasformedKey;
            String newKey;

//            trasformedKey = key.split("\\_");
            //.testDoc
//            trasformedKey = key.split(Pattern.quote(" ")) ;
            trasformedKey = key.split(Pattern.quote("_")) ;
            Log.d(TAG, "TransformedKey: " + trasformedKey);

            newKey = trasformedKey[0] + " " + trasformedKey[1] + " " + trasformedKey[2] + " " + trasformedKey[3] + " " + trasformedKey[4];
            Log.d(TAG, "c: " + newKey);

            setBrickImage(trasformedKey[0], trasformedKey[3]);
            brickMode2.setImageResource(imageInt);

            brickMode2.setBrickName(newKey);
            brickList.put(num, brickMode2);
            brickArrayList.add(brickMode2);

        }

        Log.d(TAG, "processDocument: .doc:" + brickDoc);

        buildRecyclerView();

        Log.d(TAG, "processDocument: Brick Array List\n" + brickArrayList);
        Log.d(TAG, "processDocument: BrickHashMap\n" + brickList);

    }

    public void buildRecyclerView() {
        mBricksRecycler = findViewById(R.id.recyclerRestaurants);
        mBricksRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

//        mAdapter = new BrickAdapter(brickArrayList);
        mBrickAdapter = new BrickAdapter(brickArrayList);

        mBricksRecycler.setLayoutManager(mLayoutManager);

        mBricksRecycler.setAdapter(mBrickAdapter);


        mBrickAdapter.setOnItemClickListener(new BrickAdapter.OnItemclickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "onItemClick: ");

            }

            @Override
            public void onEditClick(int position) {
            }

            @Override
            public void onChange(int chan) {
                boolean a = mBrickAdapter.requestFireUpdate;
                if(a){
                    floatingUpdateButton.show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        brickArrayList.get(chan).

                    }

                }else {
                    floatingUpdateButton.hide();
                }
            }
        });





    }

    private void setBrickImage(String brickName, String lumi){
        String amTestArray[] = getResources().getStringArray(R.array.brick_type);

        if( brickName.equals(amTestArray[0]) && lumi.equals(getString(R.string.lumi_1)) ){
            imageInt = R.drawable.z_nastx_day;

        }else if(brickName.equals(amTestArray[0])){
            imageInt = R.drawable.z_nast_night;

        }else if(brickName.equals(amTestArray[1]) && lumi.equals(getString(R.string.lumi_1)) ){
            imageInt = R.drawable.z_prizmax_day;

        }else if(brickName.equals(amTestArray[1])){
            imageInt = R.drawable.z_prizmax_night;

        }else if(brickName.equals(amTestArray[2]) && lumi.equals(getString(R.string.lumi_1)) ){
            imageInt = R.drawable.z_asortix_day;

        }else if(brickName.equals(amTestArray[2])){
            imageInt = R.drawable.z_asortix_night;

        }else if(brickName.equals(amTestArray[3]) && lumi.equals(getString(R.string.lumi_1)) ){
            imageInt = R.drawable.z_plaksne_day;

        }else if(brickName.equals(amTestArray[3])){
            imageInt = R.drawable.z_plaksne_night;

        }else {
            imageInt = R.drawable.ic_brick_type_black_24dp;
        }

    }

    private class ProductViewHolder extends RecyclerView.ViewHolder {
        private View view;

        ProductViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setBrickName(String productName) {
            TextView textView = view.findViewById(R.id.BrickItemName);
            textView.setText(productName);
        }

        void setBrickAmount(Integer productAmount){
            TextView textView = view.findViewById(R.id.brickItemHeightText);
            textView.setText(productAmount);
        }
    }


    
    @Override
    public void onClick(View v) {

        int i = v.getId();

        if (i == R.id.brick_navigation_sold) {
            getDocument();
            Log.d(TAG, "onClick: brick_navigation_sold");
        }   else if (i == R.id.brick_navigation_order) {
            onManufactureClicked("brick_navigation_order");
            Log.d(TAG, "onClick: brick_navigation_order");
        }   else if (i == R.id.brick_navigation_produce) {
            onManufactureClicked("brick_navigation_produce");
            Log.d(TAG, "onClick: brick_navigation_produce");
        }

        else if (i == R.id.bottom_nav_view) {
//            onManufactureClicked();
            Log.d(TAG, "onClick: bottom_nav_view");
        }
//        switch (v.getId()) {
//            case R.id.buttonMainOrder:
//                onManufactureClicked();
//                Log.d(TAG, "onClick: brickTypeNavigation");
//                break;
//
//            case R.id.buttonMainManufacture:
//                onManufactureClicked();
//                Log.d(TAG, "onClick: Test testButton");
//                break;

//            case R.id.filterBar:
//                onManufactureClicked();
//                Log.d(TAG, "onClick: filterBar");
//                break;
//
//            case R.id.buttonFilter:
//                onManufactureClicked();
//                Log.d(TAG, "onClick: Test buttonFilter");
//                break;
//        }

    }

}
