package com.lumibricks;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.lumibricks.db.BrickDbHelper;
import com.lumibricks.model.Brick;
import com.lumibricks.model.Manufacture;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.DKGRAY;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.MAGENTA;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;
import static com.lumibricks.R.array.brick_lumi;
import static java.lang.Double.parseDouble;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "ManufactureDialog";

    private FirebaseFirestore mFirestore;
    private CollectionReference mBrickRef;
    private DocumentReference mTestDoc;
    private DocumentReference mWarehauseRef;
    private DocumentReference mOrderRef;
    private Manufacture newManufactureObject;

    private ArrayList<Brick> brickArrayList;
    private ArrayList<String> brickNames;
//    private ArrayList<String> brickColor;
//    private ArrayList<String> brickSize;
//    private ArrayList<String> brickName;


    String buttonActivityPressed;
    Boolean abooleanOrder;
    String brickLumiArrayList[];
    String brickColorArrayList[];

    /**
     *  Order details elements:
     */

    private double brickPrice;
    private double brickPriceTotal;
    public double orderPrice = 0.0;
    private double discount = 0;
    private double orderAmount = 1;
    private double lumiPrice = 0;

    private ManufactureListener mManufactureListener;

    public void setNewManufactureListener(ManufactureListener manufactureListener){
        mManufactureListener = manufactureListener;
    }

    public interface ManufactureListener {
        void onManufacture(Manufacture manufacture);
    }

    private View mRootView;

    private Spinner mBrickTypeSpinner;
//    private Spinner mBrickHeightSpinner;
    public Spinner mBrickColorSpinner;
    private Spinner mBrickLumiSpinner;
    private Switch  mBrickQualitySwitch;
    private ImageView mBrickQualityImage;
    private ImageView mBrickColorImage;
    private ImageView mBrickLimiImage;
    private TextView mPriceBrickText;
    private TextView mPriceText;
    private Button mBrickInput;
    private DecimalFormat df;

//    private ImageView mBrickQualityImage;
    private EditText mBrickCountEditext;

    //For Firestore brick
    Integer updatedAmount;
    private List<String> listColors, listType, listHight, listLumi = new ArrayList<String>();
    String publicBrickName = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_filters, container, false);
        if(getArguments() != null){
            buttonActivityPressed = getArguments().getString("buttonPressed");
        }else {
            //If not button type, close fragment.
            dismiss();
        }

        mBrickTypeSpinner = mRootView.findViewById(R.id.spinnerBrickItemType);
//        mBrickHeightSpinner = mRootView.findViewById(R.id.spinnerBrickItemHeight);
        mBrickColorSpinner = mRootView.findViewById(R.id.spinnerBrickItemColor);
        mBrickColorImage = mRootView.findViewById(R.id.iconBrickItemColor);
        mBrickLumiSpinner = mRootView.findViewById(R.id.spinnerBrickItemLumi);
        mBrickLimiImage = mRootView.findViewById(R.id.iconBrickItemLumi);
        mBrickQualitySwitch = mRootView.findViewById(R.id.switchBrickItemQuality);
        mBrickQualityImage = mRootView.findViewById(R.id.iconBrickItemQuality);
        mBrickCountEditext = mRootView.findViewById(R.id.editTextBrickItemCount);
        mPriceBrickText = mRootView.findViewById(R.id.textViewBrickSum);
        mPriceText = mRootView.findViewById(R.id.textViewOrderSum);
        mBrickInput = mRootView.findViewById(R.id.buttonBrickItemInputOption);
        df = new DecimalFormat("#.##");

        /**     Get brick from DB
         *      brickNames, contains list of brick Names
         *      @param brickNames
         *      @param
         *
         *
         */
//        savedInstanceState for rotating screen/...?
        BrickDbHelper dbHelper = BrickDbHelper.getInstance(this);
        brickArrayList = dbHelper.getBricks();
        brickNames = new ArrayList<>();
        for (int i = 0; i < brickArrayList.size(); i++){
            brickNames.add(i, brickArrayList.get(i).getName());
        }

        ArrayAdapter<String> adapterBricks = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, brickNames);
        adapterBricks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBrickTypeSpinner.setAdapter(adapterBricks);


//        brickNameArrayList = getResources().getStringArray(R.array.brick_type);
//        brickHightArrayList = getResources().getStringArray(R.array.brick_height);
        brickColorArrayList = getResources().getStringArray(R.array.brick_color);
        brickLumiArrayList = getResources().getStringArray(brick_lumi);

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
//        ArrayAdapter<String[]> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, brickHightArrayList);

                            //   ARRAY ADAPTER FOR SPINNERS ==== TESTS ==> Don`t work
//        final List<String> spinnerList = new ArrayList<String>
//                (Arrays.asList(brickHightArrayList));
//        int spinnerArray =  R.array.brick_height;

//        List<String> arrayList = Collections.singletonList(spinnerList.toString());
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);


        mBrickQualitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mBrickQualitySwitch.setText(R.string.brick_2Class + " -10%");
                    mBrickQualityImage.setImageResource(R.drawable.ic_2_class_black_24dp);
                }else {
                    mBrickQualitySwitch.setText(R.string.brick_1Class);
                    mBrickQualityImage.setImageResource(R.drawable.ic_1_class_black_24dp);
                }
                notifyPriceChange(null, null, null, isChecked, null, 0);

            }
        });

        mBrickTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String brickItemName = parent.getSelectedItem().toString();

                int brickIndex = brickNames.indexOf(brickItemName);
                String sellT = brickArrayList.get(brickIndex).getSellType();
                mBrickInput.setText(sellT);
                notifyPriceChange(brickItemName, null, null, null, null, 0);

                if ( brickItemName.equals("TAKTILAS pump") || brickItemName.equals("TAKTILAS line") ){
                    mBrickColorSpinner.setVisibility(View.GONE);
                } else if ( brickArrayList.get(brickIndex).getPriceColor() == 0 ) {
                    mBrickColorSpinner.setVisibility(View.GONE);
                }else if ( brickArrayList.get(brickIndex).getPriceColor() != 0  ) {
                    mBrickColorSpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        mBrickColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getSelectedItem().toString();

                if ( item.equals(brickColorArrayList[0]) ) {
                    mBrickColorImage.setBackgroundColor((GRAY));
                } else if (item.equals(brickColorArrayList[1]) ){
                    mBrickColorImage.setBackgroundColor((GREEN));
                }else if (item.equals(brickColorArrayList[2]) ){
                    mBrickColorImage.setBackgroundColor((YELLOW));
                }else if (item.equals(brickColorArrayList[3]) ){
                    mBrickColorImage.setBackgroundColor((MAGENTA));
                }else if (item.equals(brickColorArrayList[4]) ){
                    mBrickColorImage.setBackgroundColor((RED));
                }else if (item.equals(brickColorArrayList[5]) ){
                    mBrickColorImage.setBackgroundColor((BLACK));
                }else if (item.equals(brickColorArrayList[6]) ){
                    mBrickColorImage.setBackgroundColor((DKGRAY));
                }else if (item.equals(brickColorArrayList[7]) ){
                    mBrickColorImage.setBackgroundColor((WHITE));
                }

                notifyPriceChange(null, item, null, null, null, 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBrickLumiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case(0):
                        Log.d(TAG, "onItemSelected: Lumi.0");
                        mBrickLimiImage.setBackgroundColor(WHITE);
                        break;
                    case(1):
                        Log.d(TAG, "onItemSelected: Lumi.1");
                        mBrickLimiImage.setBackgroundColor(GREEN);
                        break;
                    case (2):
                        Log.d(TAG, "onItemSelected: Lumi.2");
                        mBrickLimiImage.setBackgroundColor(BLUE);
                        break;
                }
                notifyPriceChange(null , null, String.valueOf(position), null, null, 0);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBrickCountEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                mBrickCountEditext.clearComposingText();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.d(TAG, "afterTextChanged: " + getSelectedBrickAmount());
//                double b = Double.parseDouble(getSelectedBrickAmount());

                if (!s.toString().equals("")){

                    try {
                        double a = parseDouble(s.toString());
                        if ( a >= 0.1 ) {
                            Log.i(TAG, "afterTextChanged: s: " + a);
                            notifyPriceChange(null , null, null, null, null, 2);
                        } else {
                            Log.i(TAG, "afterTextChanged: Text is too small: " + s);
                        }

                    }catch (Exception e){
                        Log.i(TAG, "afterTextChanged: Exception e: " + e);
                    }

                }


            }
        });

        mBrickInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBrickInput.getText().equals("gb")){
                    mBrickInput.setText("m2");

                }else if (mBrickInput.getText().equals("m2")){
                    mBrickInput.setText("gb");
                }
            }
        });

        List<String> lines2 = Arrays.asList(getResources().getStringArray(R.array.brick_color));

        return mRootView;
    }

    //Should I change this function to 6 smaller one>? And set priceB as local variable.
    private void notifyPriceChange(String brickName, String brickColor, String brickLumi, Boolean brickClass, String brickInputType, double brickAmount) {
        int start_amount = 1;
        double priceB = 0.0;
        String brickSelectedName = getSelectedBrickType();
        Log.i(TAG, "notifyPriceChange: \nSelected brick name:" + brickSelectedName);
        int brickIndex = brickNames.indexOf(brickSelectedName);
        double brickStartValue = brickArrayList.get(brickIndex).getPrice();
        
        if(brickAmount == 0 && orderAmount == 1 ){
            Log.i(TAG, "notifyPriceChange: return on brickAmount 0");
//            return;
//            orderAmount = start_amount;

        } else if( brickAmount == 2 ){
            brickAmount = parseDouble(getSelectedBrickAmount());
            Log.i(TAG, "notifyPriceChange: Should not make impact, return !!!");
        }
//        && getSelectedBrickAmount().equals("1")

        
        if (brickName != null){

            //TODO make check for ** TAKTILAIS pump/line ** (specific brick whit no gray color(only yellow))
            if (brickName.equals("TAKTILAS pump") || brickName.equals("TAKTILAS line")){
                priceB  = brickArrayList.get(brickIndex).getPriceColor();
            }else {
                priceB  = brickArrayList.get(brickIndex).getPrice();
                Log.d(TAG, "notifyPriceChange: brickPrice of Name: " + brickName + "\nEquals: " + priceB);
            }
            brickPrice = priceB;
            brickPriceTotal = ((brickPrice + lumiPrice) - discount) ;
        } else if(brickColor != null){

            if(brickColor.equals(brickColorArrayList[0])){
                //Don`t change brick price
                // TAKTILAIS pump/line
                priceB  = brickArrayList.get(brickIndex).getPrice();
            } else {
                //Set new brick price(colored)
                priceB  = brickArrayList.get(brickIndex).getPriceColor();
            }
            brickPrice = priceB;
            brickPriceTotal = ((brickPrice + lumiPrice) - discount) ;

        } else if( brickLumi != null ){

            if(brickLumi.equals("0")){
                lumiPrice = 0;
            } else if (brickLumi.equals("1") || brickLumi.equals("2")){
                lumiPrice = 1.1;
            } else {
                Log.d(TAG, "notifyPriceChange: Error on brickColor");
            }
            brickPriceTotal = ((brickPrice + lumiPrice) - discount) ;

        } else if(brickClass != null ){
            if (brickClass){
                discount = 0.40;
                Log.d(TAG, "notifyPriceChange: New discount to brick from: " + priceB);
                Log.d(TAG, "To: " + priceB);
            }else {
                discount = 0;
            }
            brickPriceTotal = ((brickPrice + lumiPrice) - discount) ;



        } else if( brickInputType != null ){

            /**
             *  TODO: image text resize
             *      create converter
             */

            // check (m2 or gb) as default input type

            String input;
            input = brickArrayList.get(brickIndex).getSellType();

            if (brickInputType.equals("gb")){

                if(input.equals("gb")){
                    priceB  = brickArrayList.get(brickIndex).getPrice();
                    getManufacture().setSellType("gb");
                }else if (input.equals("m2")){
                    Log.d(TAG, "notifyPriceChange: brickInputType: todo...2");
                    //TODO create converter (call)
                }

            } else if (brickInputType.equals("m2")){

                if(input.equals("m2")){
                    priceB  = brickArrayList.get(brickIndex).getPrice();
                    getManufacture().setSellType("m2");
                }else if (input.equals("gb")){
                    Log.d(TAG, "notifyPriceChange: brickInputType: todo...4");
                    //TODO create converter (call)
                }

            }

        } else if (brickAmount != 0 ){

//            orderPrice = orderPrice * brickAmount;
            orderAmount = brickAmount;

        } else {
            priceB  = brickArrayList.get(brickIndex).getPrice();
//            orderPrice = (priceB + lumiPrice) * discount * orderAmount;
            Log.d(TAG, "notifyPriceChange: \n\n Deal whit this!!!!)");
            //Should not work!
        }

//        brickPriceTotal = Double.valueOf(df.format(brickPriceTotal));
//        try {
//            Double brickPriceTotalString = Double.valueOf(df.format(brickPriceTotal));
////            brickPriceTotal = parseDouble(brickPriceTotalString);
//            Log.i(TAG, "notifyPriceChange: brickPriceTotalString: " + brickPriceTotalString);
//        }catch (Exception e){
//            Log.i(TAG, "notifyPriceChange: 1e: " + e);
//        }
//        try {
//            brickPriceTotal = Double.parseDouble(brickPriceTotalString);
//        }catch (Exception e){
//            Log.i(TAG, "notifyPriceChange: 2e: " + e);
//        }

        orderPrice = brickPriceTotal * orderAmount;
        String price = String.valueOf(orderPrice);
        mPriceBrickText.setText(String.valueOf(brickPriceTotal));
        mPriceText.setText(price);
        Log.i(TAG, "notifyPriceChange end: brickPriceTotal: " + brickPriceTotal + "\nOrder price: " + orderPrice);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ManufactureListener) {
            mManufactureListener = (ManufactureListener) context;
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

            if (mManufactureListener == null) {
            Log.d(TAG, "onManufactureClicked: getManufacture");
            getBrickName();
            updatedAmount = Integer.parseInt(Objects.requireNonNull(getSelectedBrickAmount()));
            Log.d(TAG, "Set brick amount:(1.0) " + updatedAmount);

            if(abooleanOrder){
                //TODO check if that order brick already exist! If it does, fdo a Query, just like a queryExistingDatabase()
                onFirestoreInsert(publicBrickName, mOrderRef);
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

    public void onOrderClicked(){
        Manufacture newBrick;
        newBrick = getManufacture();
        if (mManufactureListener == null) {
//            getManufacture();
//            mManufactureListener = (ManufactureListener) getManufacture() );
//            mManufactureListener.onManufacture(newBrick);

        }else {
            checkManufacture(newBrick);
            try {
                mManufactureListener.onManufacture(newBrick);
            }catch (Exception e){
                Log.d(TAG, "onOrderClicked: Exception e: " + e);
            }
//            mManufactureListener.onManufacture(newBrick);
            dismiss();
        }

    }

    private String getBrickName(){
        newManufactureObject = new Manufacture();
        newManufactureObject.setBrick(getManufacture().getBrick());
        newManufactureObject.setColor(getSelectedBricColor());
        newManufactureObject.setLumi(getSelectedBrickLumi());
        newManufactureObject.setQuality(getSelectedBrickQuality());
        newManufactureObject.setAmount(orderAmount);
//        newManufactureObject.setHeight(getSelectedBrickHeight());

        publicBrickName =
                newManufactureObject.getBrick()+    "_"+
                        newManufactureObject.getColor()+    "_" +
                        newManufactureObject.getLumi()+    "_" +
                        newManufactureObject.getQuality() ;
//        newManufactureObject.getHeight()+   "_" +


        //TODO (9.0) Make a function that check if a brick can exist! (Height/type/lumi/
        String a = newManufactureObject.getBrick();
        if(a == null){
            publicBrickName = null;
        }

        if(publicBrickName != null){
            return publicBrickName;
        }else {
            //TODO Pop up, that say brick is invalid (9.1)
            Log.d(TAG, "getBrickName: Brick is invalid!");
            dismiss();
            return null;
        }

    }

    private void queryExistingDatabase(){

        Query query =  mBrickRef.whereGreaterThanOrEqualTo(publicBrickName,0);
        Log.d("Big challenge: ", publicBrickName);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.i(TAG, "onComplete: Task:" + task);

                if(task.isSuccessful()){
                    //Check if there is a brick.amount >= 0
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Log.d(TAG, "onComplete: There exist a " + publicBrickName + " brick in a document");
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        Integer oldBrickAmount = Integer.parseInt( document.getData().get(publicBrickName).toString());
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

                onFirestoreInsert(publicBrickName, mWarehauseRef);
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

    private String getSelectedBrickInput() {
        String selected = mBrickInput.getText().toString();
        return selected;
//        if (getString(R.string.brick_insert_amount).equals(selected)){
//            return null;
//        } else {
//            return selected;
//        }
    }

    public Manufacture getManufacture() {
        Manufacture manufacture = new Manufacture();

        if(mRootView != null){
            manufacture.setBrick(getSelectedBrickType());
            manufacture.setColor(getSelectedBricColor());
            manufacture.setQuality(getSelectedBrickQuality());
            manufacture.setLumi(getSelectedBrickLumi());
            manufacture.setAmount(orderAmount);
            manufacture.setPrice(brickPriceTotal);
            manufacture.setSellType(getSelectedBrickInput());
            //created in notifyPriceChange();
            //manufacture.setPrice(getSe);
//            manufacture.getSellType()
        }
//        manufacture.setHeight(getSelectedBrickHeight());

        newManufactureObject = manufacture;

        return manufacture;
    }
    private void checkManufacture(Manufacture brickToCheck){
        if( brickToCheck.getBrick() == null || 
            brickToCheck.getQuality() == null ||
            brickToCheck.getAmount() == null ||
            brickToCheck.getLumi() == null ||
            brickToCheck.getColor() == null ||
            brickToCheck.getHeight() == null ) 
        {
            Log.d(TAG, "checkManufacture: some item is Null\n\n");
            Log.d(TAG, "checkManufacture: " + brickToCheck);
            //TODO make pop up dialog, to make sure all field is (correct & !empty)
            dismiss();
        }else {
            Log.d(TAG, "checkManufacture: Brick is fine, continue ...");

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonManufacture:
                Log.d(TAG, "onClick: buttonManufacture");
                onManufactureClicked();
                break;
            case R.id.buttonOrder:
                //method return publicBrickName
                onOrderClicked();
                break;
            case R.id.buttonCancel:
                onCancelClicked(v);
                break;

//            case R.id.spinnerBrickItemLumi:
//                Log.d(TAG, "onClick: spinnerClick" );
//                break;

        }
    }


/**
      ####  NOTES   ####
 */
    private void brickNameClick(){
//        if (item.equals(brickNameArrayList[1]) || item.equals(brickNameArrayList[2]) || item.equals(brickNameArrayList[3])) {
//                    final ArrayAdapter<CharSequence> modificedArrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.brick_height1, android.R.layout.simple_spinner_item);
//                    modificedArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    mBrickHeightSpinner.setAdapter(modificedArrayAdapter);
//
//                    mBrickHeightSpinner.setSelection(2,true);
//                    mBrickHeightSpinner.setSelection(1,false);
////                    mBrickHeightSpinner.setSelection(4,false);
////
//                    mBrickHeightSpinner.performClick();
//
//                } else if(item.equals(brickNameArrayList[4]) ||item.equals(brickNameArrayList[5]) ){
//                    final ArrayAdapter<CharSequence> modificedArrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.brick_height2, android.R.layout.simple_spinner_item);
//                    modificedArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    mBrickHeightSpinner.setAdapter(modificedArrayAdapter);
//
//                    mBrickHeightSpinner.performClick();
//
//
//                } else if(item.equals(brickNameArrayList[6])){
//                    final ArrayAdapter<CharSequence> modificedArrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.brick_height3, android.R.layout.simple_spinner_item);
//                    modificedArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    mBrickHeightSpinner.setAdapter(modificedArrayAdapter);
////                    mBrickHeightSpinner.getPopupBackground();
//                } else {
//                    Log.d(TAG, "onItemSelected: Else clicked!!!\n\n");
//                    mBrickHeightSpinner.performClick();
//
//                }
    }

    private void brickHightClick(){
//        mBrickHeightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getSelectedItem().toString();
//
//                if ( item.equals(brickHightArrayList[1]) ) {
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private String getSelectedBrickHeight() {
//        String selected = (String) mBrickHeightSpinner.getSelectedItem();
//        if (getString(R.string.value_brick_height).equals(selected)){
//            return null;
//        } else {
//            return selected;
//        }
        return null;
    }
}
