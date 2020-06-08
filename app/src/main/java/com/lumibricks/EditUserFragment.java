package com.lumibricks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lumibricks.db.BrickDbHelper;
import com.lumibricks.model.Address;
import com.lumibricks.model.User;

import java.util.ArrayList;

public class EditUserFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "EditPersonFragment";

    private User user;
    private Address address;
    private ArrayList<User> userList;
    private ArrayList<Address> addressList;


    //User()
    private ArrayList<String> userNames;
    private ArrayList<String> names;
    private ArrayList<String> surnames;
    private ArrayList<String> eMails;
    private ArrayList<String> mobiles;
    private ArrayList<String> bankNrs;

    private String userName;
    private String name;
    private String surname;
    private String eMail;
    private String mobile;  //TODO pass data so **from edit could call**!!! client to discus details
    private String bankNr;

    private String newUserName;
    private String newName;
    private String newSurname;
    private String newEMail;
    private String newMobile;  //TODO pass data so **from edit could call**!!! client to discus details
    private String newBankNr;

    private int userIndex;
    private String getUserString;   //String from Adapter to compare

    //Address()
    private ArrayList<String> addressNames;

    private User chosenUser;

    private ChooseListiner mChooseListiner;

    public void setNewChooseListener(ChooseListiner chooseListener){
        mChooseListiner = chooseListener;
    }

    public void EditUserFragment(ChooseListiner chooseListiner) {
        chooseListiner = chooseListiner;
    }

    public interface ChooseListiner {
        void onChoose(User user);
    }

    private View mRootView;
    private Button buttonChoose;
    private Button buttonEdit;
    private EditText editText;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private EditText editText7;
    private Spinner mChooseSpinner;
    private Button mButtonOk;
    private Button mButtonCancel;

    Boolean editTrue;


//    private RecyclerView recyclerViewEdit;
//    private RecyclerView recyclerViewChoose;
    private RecyclerView.LayoutManager mLayoutManager;

    String buttonDialogPressed;

    BrickDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_choose_item, container, false);
        if(getArguments() != null){
            buttonDialogPressed = getArguments().getString("buttonPressed");  // TODO edit User || edit Address
            getUserString = getArguments().getString("currentItem");  //TODO check all data instead of just **Username**
            editTrue = true;
        }else {
            //If not equal type, close fragment.
            dismiss();
        }

        if (buttonDialogPressed == "editUser"){

        }

        buttonChoose = mRootView.findViewById(R.id.buttonChooseDialogFragment);
        buttonEdit = mRootView.findViewById(R.id.buttonEditDialogFragment);
        editText = mRootView.findViewById(R.id.usernameFragment);
        editText1= mRootView.findViewById(R.id.editTextItem1);
        editText2= mRootView.findViewById(R.id.editTextItem2);
        editText3= mRootView.findViewById(R.id.editTextItem3);
        editText4= mRootView.findViewById(R.id.editTextItem4);
        editText5= mRootView.findViewById(R.id.editTextItem5);
        editText6= mRootView.findViewById(R.id.editTextItem6);
        editText7= mRootView.findViewById(R.id.editTextItem7);

        mChooseSpinner = mRootView.findViewById(R.id.spinnerItemChoose);
        mButtonCancel = mRootView.findViewById(R.id.butItemChooseCancel);
        mButtonOk = mRootView.findViewById(R.id.butItemChooseOk);
        //RelLay Items

//        recyclerViewEdit = mRootView.findViewById(R.id.recycler_viewItemsEdit);
//        recyclerViewChoose = mRootView.findViewById(R.id.recycler_viewItemsChoose);

        dbHelper = new BrickDbHelper(getActivity());
        userList = dbHelper.getUsers();
        addressList = dbHelper.getAddress();
        chosenUser = new User();

        setChooseSpinnerAdapter();
        //TODO setRestOfAdapters (if chooseClicked)

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//              mLayoutManager = new LinearLayoutManager();
                //TODO Create list view +(UserAdapter) for edit and search items in dialog msg, or fragment.
            }
        });


        mButtonOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onOk(v);
            }
        });

        mChooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userName = parent.getSelectedItem().toString();
                userIndex = userList.indexOf(userName)+1;
                chosenUser = userList.get(userIndex);


                mButtonOk.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return mRootView;
    }

    private void setChooseSpinnerAdapter(){

        userNames = new ArrayList<>();
        names  = new ArrayList<>();
        surnames  = new ArrayList<>();
        eMails  = new ArrayList<>();
        mobiles  = new ArrayList<>();
        bankNrs  = new ArrayList<>();

        for (int i = 0; i < userList.size(); i++){
            userNames.add(i, userList.get(i).getName());
            names.add(i, userList.get(i).getName());
            surnames.add(i, userList.get(i).getName());
            eMails.add(i, userList.get(i).getName());
            mobiles.add(i, userList.get(i).getName());
            bankNrs.add(i, userList.get(i).getName());
        }

        ArrayAdapter<String> adapterBricks = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userNames);
        adapterBricks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseSpinner.setAdapter(adapterBricks);
    }

    private void clearView(){
        editText.setVisibility(View.INVISIBLE);
        mChooseSpinner.setVisibility(View.INVISIBLE);
        mButtonOk.setVisibility(View.INVISIBLE);
        //RelLay

        //clear
        chosenUser = null;

    }

    private void onCancel(){
        dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ChooseListiner) {
            mChooseListiner = (ChooseListiner) context;
        }
    }

    private void chooseUserManager(){
        clearView();
        editTrue=false;
    }

    private void editUserDialog(){
        editText.setVisibility(View.VISIBLE);
        mButtonOk.setVisibility(View.VISIBLE);
        editTrue = true;

    }

    private boolean isChangeInItem(View v){
        switch (v.getId()){
            case R.id.butItemChooseOk:
                if(getUserString.equals(editText.getText().toString())){
                    return false;
                }else {
                    return true;
                }
            default:
                return false;
        }

    }

    private void getnewUser() {

        try {
            newUserName = editText.getText().toString();
            newName = "_"; //editText1.getText().toString();
            newSurname = "_"; //editText1.getText().toString();
            newEMail = "_"; //editText1.getText().toString();
            newMobile = "_"; //editText1.getText().toString();
            newBankNr = "_"; //editText1.getText().toString();

        }catch (Exception e){
            Log.i(TAG, "getnewUser: e:\nw" + e);
        }
        user = new User();

        user.setUserName(newUserName);
        user.setName(newName);
        user.setSurname(newSurname);
        user.seteMail(newEMail);
        user.setMobile(newMobile);
        user.setBankNr(newBankNr);
        
    }


    public void onOk(View v){
        if (editTrue){
            getnewUser();
            if (isChangeInItem(v)){

                if(mChooseListiner ==null){

                    Log.i(TAG, "onOk: Wrong , Error???");
                }else {

                    try {
                        dbHelper.addUser(user);
                        Log.i(TAG, "onOk: user successfully inserted");

                        mChooseListiner.onChoose(user);
                    }catch (Exception e){
                        Log.i(TAG, "onOk: Exception e():\n " + e);
                    }
                    dismiss();
                }

            }else {
                // TODO DIALOG
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Username is the same");
                alertDialogBuilder.setMessage("Are you sure you want to continue?");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //onSuccess
                        dismiss();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null ) {
                            dialog.dismiss();
                        }
                    }
                });




            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.butItemChooseCancel:
                onCancel();
                break;

            case R.id.butItemChooseOk:
                onOk(v);
                break;

            case R.id.buttonChooseDialogFragment:
                Log.i(TAG, "sas");
                chooseUserManager();
                break;

            case R.id.buttonEditDialogFragment:
                editUserDialog();

                break;


        }

    }
}
