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
import com.lumibricks.model.BrickOrder;
import com.lumibricks.model.Manufacture;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.transform.dom.DOMLocator;

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
import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Double.parseDouble;

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "ManufactureDialog";

    private FirebaseFirestore mFirestore;
    private CollectionReference mBrickRef;
    private DocumentReference mTestDoc;
    private DocumentReference mWarehauseRef;
    private DocumentReference mOrderRef;
    private Manufacture newManufactureObject;


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

    private ArrayList<Brick> brickArrayList;
    private ArrayList<String> brickNames;
    private String brickItemName;
    private int brickIndex;
    private String sellTypes;
    private List<String> items;
    private String mainTypeItem;
    private String currentTypeItem;
    private Double m2InPal;
    private int gb_m2;
    private int gbRinda;
    private Double orginBrickAmount;

    private Double palletes;

//as
    private double inputSellPrice = 0;

    public double orderPrice = 0.0;
    private double brickPriceTotal;
    private double orderAmount = 1;
    private double brickPrice;
    private double brickColorPrice = 0;
    private double discount = 0;
//    private double lumiPrice = 0;
    private double typeCoefficient = 1;

    private ManufactureListener mManufactureListener;

    public void setNewManufactureListener(ManufactureListener manufactureListener){
        mManufactureListener = manufactureListener;
    }

    public interface ManufactureListener {
        void onManufacture(BrickOrder manufacture);
    }

    private View mRootView;

    private Spinner mBrickTypeSpinner;
//    private Spinner mBrickHeightSpinner;
    public Spinner mBrickColorSpinner;
    private Spinner mBrickLumiSpinner;
    private Switch  mBrickQualitySwitch;
    private ImageView mBrickQualityImage;
    private ImageView mBrickColorImage;
    private ImageView mBrickTypeImage;
    private ImageView mBrickLimiImage;
    private TextView mPriceBrickText;
    private TextView mPriceText;
    private Button mBrickInput;
    private TextView mTextBrickTypeOrigin;
    private TextView mTextPalletes;
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
        mBrickTypeImage = mRootView.findViewById(R.id.icon_brick_type);
        mBrickLumiSpinner = mRootView.findViewById(R.id.spinnerBrickItemLumi);
        mBrickLimiImage = mRootView.findViewById(R.id.iconBrickItemLumi);
        mBrickQualitySwitch = mRootView.findViewById(R.id.switchBrickItemQuality);
        mBrickQualityImage = mRootView.findViewById(R.id.iconBrickItemQuality);
        mBrickCountEditext = mRootView.findViewById(R.id.editTextBrickItemCount);
        mPriceBrickText = mRootView.findViewById(R.id.textViewBrickSum);
        mBrickInput =  mRootView.findViewById(R.id.buttonBrickItemInputOption);
        mPriceText = mRootView.findViewById(R.id.textViewOrderSum);
        mTextBrickTypeOrigin = mRootView.findViewById(R.id.textViewBrickType);
        mTextPalletes = mRootView.findViewById(R.id.textViewPalletes);
        df = new DecimalFormat("#.##");

        /**     Get brick from DB
         *      brickNames, contains list of brick Names
         *      @param brickNames
         *      @param
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
        ArrayAdapter<String> adapterBrickColors = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, brickColorArrayList);
        adapterBrickColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBrickColorSpinner.setAdapter(adapterBrickColors);

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

        mBrickTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brickItemName = parent.getSelectedItem().toString();
                brickIndex = brickNames.indexOf(brickItemName);
                m2InPal = brickArrayList.get(brickIndex).getM2_InPallet();
                gb_m2 = brickArrayList.get(brickIndex).getGb_m2();
                gbRinda = brickArrayList.get(brickIndex).getGbRinda();

//                configurateSellType()
                sellTypes = brickArrayList.get(brickIndex).getSellType();
                items = Arrays.asList(sellTypes.split("\\s*,\\s*"));

                mainTypeItem = items.get(0);
                mBrickInput.setText(items.get(0));
                setDefault();



                //TODO if brickItemName.equals("bord") || brickItemName.equals("apmale"){ show color black & gray} ???
                if ( brickItemName.equals("TAKTILAS pump") || brickItemName.equals("TAKTILAS line") ){
                    hideColor("YELLOW");
                } else if ( brickArrayList.get(brickIndex).getPriceBlack() == 0.0 ) {
                    hideColor("nan");
                }else if ( brickArrayList.get(brickIndex).getPriceBlack() != 0  ) {
                    showColor();
                }

                //Lumi brick
                if ( brickArrayList.get(brickIndex).getPriceLumi() == 0 ){
                    hideLumi();
                } else {
                    showLumi();
                }

                currentTypeItem = mainTypeItem;
                notifyPriceChange(brickItemName, null, null, null, null, 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

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



        mBrickColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedColor = parent.getSelectedItem().toString();

                if ( selectedColor.equals(brickColorArrayList[0]) ) {
                    mBrickColorImage.setBackgroundColor((GRAY));
                } else if (selectedColor.equals(brickColorArrayList[1]) ){
                    mBrickColorImage.setBackgroundColor((GREEN));
                }else if (selectedColor.equals(brickColorArrayList[2]) ){
                    mBrickColorImage.setBackgroundColor((YELLOW));
                }else if (selectedColor.equals(brickColorArrayList[3]) ){
                    mBrickColorImage.setBackgroundColor((MAGENTA));
                }else if (selectedColor.equals(brickColorArrayList[4]) ){
                    mBrickColorImage.setBackgroundColor((RED));
                }else if (selectedColor.equals(brickColorArrayList[5]) ){
                    mBrickColorImage.setBackgroundColor((BLACK));
                }else if (selectedColor.equals(brickColorArrayList[6]) ){
                    mBrickColorImage.setBackgroundColor((DKGRAY));
                }else if (selectedColor.equals(brickColorArrayList[7]) ){
                    mBrickColorImage.setBackgroundColor((WHITE));
                }

                notifyPriceChange(null, selectedColor, null, null, null, 0);
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
                        mBrickInput.setText(items.get(0));
                        mBrickInput.setClickable(true);
                        Log.d(TAG, "onItemSelected: Lumi.0");
                        mBrickLimiImage.setBackgroundColor(WHITE);
                        notifyPriceChange(brickItemName , null, null, null, null, 0);
                        return;
                    case(1):
                        mBrickInput.setText("gb");
                        mainTypeItem = "gb";
                        mBrickInput.setClickable(false);
                        Log.d(TAG, "onItemSelected: Lumi.1");
                        mBrickLimiImage.setBackgroundColor(GREEN);
                        break;
                    case (2):
                        mBrickInput.setText("gb");
                        mainTypeItem = "gb";
                        mBrickInput.setClickable(false);
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
                            orderAmount = a;
                            calculateTypes();
//                            notifyPriceChange(null , null, null, null, null, 2);
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
//                brickItemName = getSelectedBrickType();
//                int brickIndex = brickNames.indexOf(brickItemName);
//                String sellTypes = brickArrayList.get(brickIndex).getSellType();
//                List<String> items = Arrays.asList(sellTypes.split("\\s*,\\s*"));
                int lastIndex = items.size()-1;
                String oldBrickType = (String) mBrickInput.getText();

               if (oldBrickType.equals(items.get(lastIndex))){
                   Log.i(TAG, "onClick: lastIndex true: " + items.get(lastIndex));
                   mBrickInput.setText(items.get(0));
                   currentTypeItem = items.get(0);
               }else {
                   for (int next = 0; next<lastIndex; next++){
                       if (oldBrickType.equals(items.get(next))){
                           mBrickInput.setText(items.get(next+1));
                           currentTypeItem = items.get(next+1);
                           break;
                       }
                   }
               }

                notifyPriceChange(null, null, null, null, sellTypes, 0);

//                if(mBrickInput.getText().equals("gb")){
//                    mBrickInput.setText("m2");
//
//                }else if (mBrickInput.getText().equals("m2")){
//                    mBrickInput.setText("gb");
//                }
            }
        });

        List<String> lines2 = Arrays.asList(getResources().getStringArray(R.array.brick_color));

        return mRootView;
    }

    private void setDefault(){
        mBrickColorImage.setBackgroundColor((GRAY));
        brickColorPrice = 0;
        mBrickLimiImage.setBackgroundColor(WHITE);
        mBrickLumiSpinner.setSelection(0);
    }

    private void hideColor(String color){
        mBrickColorSpinner.setVisibility(View.GONE);
        mBrickColorImage.setVisibility(View.GONE);
        mBrickColorSpinner.setSelection(0);
        if (color.equals("YELLOW")){
            mBrickTypeImage.setBackgroundColor((YELLOW));
        } else {
            mBrickTypeImage.setBackgroundColor((WHITE));
        }
    }
    private void showColor(){
        mBrickTypeImage.setBackgroundColor((WHITE));
        mBrickColorImage.setVisibility(View.VISIBLE);
        mBrickColorSpinner.setVisibility(View.VISIBLE);
    }
    private void hideLumi(){
        mBrickLumiSpinner.setVisibility(View.GONE);
        mBrickLimiImage.setVisibility(View.GONE);
        //reset spinner
        mBrickLimiImage.setBackgroundColor(WHITE);
        mBrickLumiSpinner.setSelection(0);
    }
    private void showLumi(){
        mBrickLumiSpinner.setVisibility(View.VISIBLE);
        mBrickLimiImage.setVisibility(View.VISIBLE);
    }


    //Should I change this function to 6 smaller one>? And set priceB as local variable.
    private void notifyPriceChange(String brickName, String brickColor, String brickLumi, Boolean brickClass, String brickInputType, double brickAmount) {
        double priceB = 0.0;
//        String brickSelectedName = getSelectedBrickType();
        Log.i(TAG, "notifyPriceChange: \nSelected brick name:" + brickItemName);
//        brickIndex = brickNames.indexOf(brickSelectedName);
//        double brickStartValue = brickArrayList.get(brickIndex).getPrice();

        if (brickName != null) {

//            TODO make check for ** TAKTILAIS pump/line ** (specific brick whit no gray color(only yellow))
//            if (brickName.equals("TAKTILAS pump") || brickName.equals("TAKTILAS line")){
//                brickPrice  = brickArrayList.get(brickIndex).getPriceColor();
//            }else {
//            }

            brickPrice = brickArrayList.get(brickIndex).getPrice();
            brickPriceTotal = ((brickPrice + brickColorPrice) - discount);
            calculateTypes();
            Log.d(TAG, "notifyPriceChange: brickPrice of Name: " + brickName + "\nEquals: " + priceB);
            return;
        } else if (brickColor != null) {
            if (brickColor.equals(brickColorArrayList[0])) {
                brickColorPrice = brickArrayList.get(brickIndex).getPriceGray();
            } else if (brickColor.equals(brickColorArrayList[1])) {
                brickColorPrice = brickArrayList.get(brickIndex).getPriceGreen();
            } else if (brickColor.equals(brickColorArrayList[2])) {
                brickColorPrice = brickArrayList.get(brickIndex).getPriceYellow();
            } else if (brickColor.equals(brickColorArrayList[3])) {
                brickColorPrice = brickArrayList.get(brickIndex).getPriceOrange();
            } else if (brickColor.equals(brickColorArrayList[4])) {
                brickColorPrice = brickArrayList.get(brickIndex).getPriceRed();
            } else if (brickColor.equals(brickColorArrayList[5])) {
                brickColorPrice = brickArrayList.get(brickIndex).getPriceBlack();
            } else if (brickColor.equals(brickColorArrayList[6])) {
                brickColorPrice = brickArrayList.get(brickIndex).getPriceBrown();
            } else if (brickColor.equals(brickColorArrayList[7])) {
                brickColorPrice = brickArrayList.get(brickIndex).getPriceWhite();
            } else {
                Log.i(TAG, "notifyPriceChange: \n\n System has incorect data- Color: 2.n");
            }

            brickPriceTotal = ((brickPrice + brickColorPrice) - discount);
            calculateTypes();
            return;

        } else if (brickLumi != null) {

            if (brickLumi.equals("0")) {
                brickPrice = brickArrayList.get(brickIndex).getPriceLumi();
                Log.i(TAG, "notifyPriceChange: Lumi = 0,  Should not pass data!!! Error!?");
            } else if (brickLumi.equals("1") || brickLumi.equals("2")) {
                brickPrice = brickArrayList.get(brickIndex).getPriceLumi();
            } else {
                Log.d(TAG, "notifyPriceChange: Error on brickColor");
            }
            brickPriceTotal = ((brickPrice) - discount);
            calculateTypes();

        } else if (brickClass != null) {
            if (brickClass) {
                discount = 0.40;
                Log.d(TAG, "notifyPriceChange: New discount to brick from: " + priceB);
                Log.d(TAG, "To: " + priceB);
            } else {
                discount = 0;
            }
            brickPriceTotal = ((brickPrice + brickColorPrice) - discount);
            calculateTypes();


        } else if (brickInputType != null) {
            calculateTypes();


        } else if (brickAmount != 0) {
//            orderPrice = orderPrice * brickAmount;
            orderAmount = brickAmount;
        } else {
            priceB = brickArrayList.get(brickIndex).getPrice();
            Log.d(TAG, "notifyPriceChange:/n\n\n Error! 2.m\n\n Deal whit this!!!!)");
        }

//        brickPriceTotal = Double.valueOf(df.format(brickPriceTotal));
//        try {
//            Double brickPriceTotalString = Double.valueOf(df.format(brickPriceTotal));
////            brickPriceTotal = parseDouble(brickPriceTotalString);
//            Log.i(TAG, "notifyPriceChange: brickPriceTotalString: " + brickPriceTotalString);
//        }catch (Exception e){
//            Log.i(TAG, "notifyPriceChange: 1e: " + e);
//        }

//        orderPrice = inputSellPrice * orderAmount;
//        String price = String.valueOf(orderPrice);
//        mPriceBrickText.setText(String.valueOf(inputSellPrice));
//        mPriceText.setText(price);
//        Log.i(TAG, "notifyPriceChange end: brickPriceTotal: " + brickPriceTotal + "\nOrder price: " + orderPrice);
    }

    private void calculateTypes(){
        Double gb_pal = gb_m2*m2InPal;
        Double rows = gb_pal/gbRinda;
        palletes = 0.;
        Double gbPalDb = brickArrayList.get(brickIndex).getGb_InPallet();

        Double garums = Double.valueOf(brickArrayList.get(brickIndex).getSizeG())/1000;
        Double biezums = Double.valueOf(brickArrayList.get(brickIndex).getSizeB())/1000;
        Double laukumsGB = garums*biezums;
        Double m2Rinda = Double.valueOf(laukumsGB * gbRinda);
        orginBrickAmount=0.;
//            calculateTypes(brickInputType, brickIndex, brickPriceTotal);
        // check (m2 or gb) as default input type
        String input;
        input = brickArrayList.get(brickIndex).getSellType();
//        priceB = brickArrayList.get(brickIndex).getPrice();


        if (sellTypes.equals("m2,gb,rinda")) {

            if (currentTypeItem.equals("gb")) {
                inputSellPrice = 1 / (gb_m2 / brickPriceTotal);
                getManufacture().setSellType("gb");
                palletes = orderAmount/gb_pal;
                orginBrickAmount = orderAmount/gb_m2;

            } else if (currentTypeItem.equals("rinda")) {
                inputSellPrice = 1 * (brickPriceTotal * m2Rinda);
                getManufacture().setSellType("rinda");
                palletes = orderAmount/rows;
                orginBrickAmount = orderAmount*m2Rinda;
            } else if (currentTypeItem.equals("m2")) {
                inputSellPrice = 1 * (brickPriceTotal);
                getManufacture().setSellType("m2");
                palletes = orderAmount/m2InPal;
                orginBrickAmount = orderAmount;
            }

        } else if (sellTypes.equals("m2,rinda")) {

            //Only for Asorti & Mozaika
            rows=13.0;
            if (brickItemName.equals("ASORTI")){
                m2Rinda=0.7;
            } else if (brickItemName.equals("MOZAĪKA 5")){
                m2Rinda=0.67;
            }
            if (currentTypeItem.equals("m2")) {
                inputSellPrice = 1 * (brickPriceTotal);
                getManufacture().setSellType("m2");
                palletes = orderAmount/m2InPal;
                orginBrickAmount = orderAmount;
            } else if (currentTypeItem.equals("rinda")) {
                inputSellPrice = 1 * (brickPriceTotal * m2Rinda);
                getManufacture().setSellType("rinda");
                palletes = orderAmount/rows;
                orginBrickAmount = orderAmount*m2Rinda;
            }
        } else if (sellTypes.equals("gb,m,rinda")){
            if (currentTypeItem.equals("gb")){
                inputSellPrice = 1 * (brickPriceTotal);
                getManufacture().setSellType("gb");
                palletes = orderAmount/gbPalDb;
                orginBrickAmount = orderAmount;
            } else if(currentTypeItem.equals("m")){
                inputSellPrice = 1 * (brickPriceTotal*garums);
                getManufacture().setSellType("m");
                //TODO palletes error
                palletes = orderAmount/gbPalDb;
                orginBrickAmount = orderAmount*garums;
            } else if(currentTypeItem.equals("rinda")){
                inputSellPrice = 1 * (brickPriceTotal*gbRinda);
                getManufacture().setSellType("rinda");
                palletes = orderAmount/(gbPalDb/gbRinda);
                orginBrickAmount = orderAmount*gbRinda;
            }

        } else if(sellTypes.equals("gb,m2")){
            if (currentTypeItem.equals("gb")){
                inputSellPrice = 1 * (brickPriceTotal);
                getManufacture().setSellType("gb");
                palletes = orderAmount/gbPalDb;
                orginBrickAmount = orderAmount;
            } else if(currentTypeItem.equals("m2")){
                inputSellPrice = 1 * (brickPriceTotal*gb_m2);
                getManufacture().setSellType("m2");
                palletes = orderAmount/m2InPal;
                orginBrickAmount = orderAmount/gb_m2;
            }
        } else if (sellTypes.equals("gb")){
            inputSellPrice = 1 * (brickPriceTotal);
            getManufacture().setSellType("gb");
            palletes = orderAmount/gbPalDb;
            orginBrickAmount = orderAmount;
        }

        //We round items to .## (2.digits after ".")
        inputSellPrice = round2(inputSellPrice);
        orderPrice = inputSellPrice * orderAmount;
        String price = String.valueOf(round2(orderPrice));
        mPriceBrickText.setText(String.valueOf(inputSellPrice));
        mPriceText.setText(price);
        Log.i(TAG, "notifyPriceChange end: brickPriceTotal: " + brickPriceTotal + "\nOrder price: " + orderPrice);
        String stringPalletes = String.valueOf(round2(palletes));
        mTextBrickTypeOrigin.setText(mainTypeItem + ": " + round2(orginBrickAmount));
        mTextPalletes.setText(stringPalletes);
    }

    public static double round2(double value) {
//        int places = 2;
//        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
                mManufactureListener.onManufacture(getBrickOrder());
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
            manufacture.setPalettes(palletes);
            //created in notifyPriceChange();
            //manufacture.setPrice(getSe);
//            manufacture.getSellType()
        }
//        manufacture.setHeight(getSelectedBrickHeight());

        newManufactureObject = manufacture;

        return manufacture;
    }

    public BrickOrder getBrickOrder(){
        BrickOrder brick= new BrickOrder();
        brick.setName(brickItemName + "_" + getSelectedBricColor() + "_" + getSelectedBrickLumi());  // + "_" + getSelectedBrickQuality()
        brick.setAmount(orderAmount);
        brick.setSellType(currentTypeItem);
        brick.setSellPrice(brickPriceTotal);
        brick.setInputSellType(mainTypeItem);  //TODO give full list- "m2,bg,rinda"
        brick.setInputSellPrice(inputSellPrice);
        brick.setOrginAmount(orginBrickAmount);
        brick.setPalletes(round2(palletes));
        return brick;
//                        items = Arrays.asList(sellTypes.split("\\s*,\\s*"));
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
