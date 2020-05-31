/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.lumibricks.order_list;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.lumibricks.R;
import com.lumibricks.data.AbstractAddRemoveExpandableDataProvider;
import com.lumibricks.db.BrickDbHelper;
import com.lumibricks.fragment.ExampleAddRemoveExpandableDataProviderFragment;
import com.lumibricks.fragment.ExpandableItemPinnedMessageDialogFragment;

import java.util.HashMap;
import java.util.Map;


public class AddRemoveExpandableExampleActivity extends AppCompatActivity implements ExpandableItemPinnedMessageDialogFragment.EventListener{
    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_LIST_VIEW = "list view";
    private static final String FRAGMENT_TAG_ITEM_PINNED_DIALOG = "item pinned dialog";
    private static final String TAG = "ExpandableActivity";

    private FloatingActionButton floatingUpdateButton;
    private FloatingActionButton floatingPushButton;
    private FirebaseFirestore mFirestore;
    private DocumentReference mOrdersRef;
    private DocumentReference mUsersRef;
    private DocumentReference mAddressRef;




    private BottomNavigationView mToolbar;
//    public FilterDialogFragment mDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mToolbar = findViewById(R.id.toolbar);
        floatingUpdateButton = findViewById(R.id.floatingUpload);
        floatingPushButton = findViewById(R.id.floatingPushInDb);
        mFirestore = FirebaseFirestore.getInstance();
        mOrdersRef = mFirestore.collection("bricks").document("orders").collection("users").document("orders");;
//        mOrdersRef = mFirestore.collection("bricks").document("orders").collection("users").document("address");
//        mOrdersRef = mFirestore.collection("bricks").document("orders").collection("users").document("users");

        BrickDbHelper dbHelper = BrickDbHelper.getInstance(this);

        if (savedInstanceState == null) {
//            createBrickItemInDialogFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(new ExampleAddRemoveExpandableDataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddRemoveExpandableExampleFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }

        floatingUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateFirestore();
                floatingUpdateButton.hide();
            }
        });

        floatingPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                floatingPushButton.hide();
            }
        });

//        mToolbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                Log.d("AddRemoveEEActivity", "onMenuItemClick: " + menuItem);
//                return false;
//            }
//        });


    }

    /**
     * This method will be called when a group item is removed
     *
     * @param groupPosition The position of the group item within data set
     */
    public void onGroupItemRemoved(int groupPosition) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container),
                R.string.snack_bar_text_group_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRemoveExpandableExampleActivity.this.onItemUndoActionClicked();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
        snackbar.show();
    }

    /**
     * This method will be called when a child item is removed
     *
     * @param groupPosition The group position of the child item within data set
     * @param childPosition The position of the child item within the group
     */
    public void onChildItemRemoved(int groupPosition, int childPosition) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container),
                R.string.snack_bar_text_child_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRemoveExpandableExampleActivity.this.onItemUndoActionClicked();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
        snackbar.show();
    }

    /**
     * This method will be called when a group item is pinned
     *
     * @param groupPosition The position of the group item within data set
     */
    public void onGroupItemPinned(int groupPosition) {
        final DialogFragment dialog = ExpandableItemPinnedMessageDialogFragment.newInstance(groupPosition, RecyclerView.NO_POSITION);

        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FRAGMENT_TAG_ITEM_PINNED_DIALOG)
                .commit();
    }

    /**
     * This method will be called when a child item is pinned
     *
     * @param groupPosition The group position of the child item within data set
     * @param childPosition The position of the child item within the group
     */
    public void onChildItemPinned(int groupPosition, int childPosition) {
        final DialogFragment dialog = ExpandableItemPinnedMessageDialogFragment.newInstance(groupPosition, childPosition);

        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FRAGMENT_TAG_ITEM_PINNED_DIALOG)
                .commit();
    }

    public void onGroupItemClicked(int groupPosition) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        AbstractAddRemoveExpandableDataProvider.GroupData data = getDataProvider().getGroupItem(groupPosition);

        if (data.isPinned()) {
            // unpin if tapped the pinned item
            data.setPinned(false);
            ((AddRemoveExpandableExampleFragment) fragment).notifyGroupItemChanged(groupPosition);
        }
    }

    public void onChildItemClicked(int groupPosition, int childPosition) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        AbstractAddRemoveExpandableDataProvider.ChildData data = getDataProvider().getChildItem(groupPosition, childPosition);

        if (data.isPinned()) {
            // unpin if tapped the pinned item
            data.setPinned(false);
            ((AddRemoveExpandableExampleFragment) fragment).notifyChildItemChanged(groupPosition, childPosition);
        }
    }

    private void onItemUndoActionClicked() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        final long result = getDataProvider().undoLastRemoval();

        if (result == RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION) {
            return;
        }

        final int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup(result);
        final int childPosition = RecyclerViewExpandableItemManager.getPackedPositionChild(result);

        if (childPosition == RecyclerView.NO_POSITION) {
            // group item
            ((AddRemoveExpandableExampleFragment) fragment).notifyGroupItemRestored(groupPosition);
        } else {
            // child item
            ((AddRemoveExpandableExampleFragment) fragment).notifyChildItemRestored(groupPosition, childPosition);
        }
    }


    private int lastSectionItem(AbstractAddRemoveExpandableDataProvider dataProvider, int position){
        AbstractAddRemoveExpandableDataProvider.GroupData item = dataProvider.getGroupItem(position);
        int groupCount = dataProvider.getGroupCount();

        if (item.isSectionHeader()) {
            throw new IllegalStateException("section item is expected");
        }

        //If not one item(address) in section (zero)
        int lastSectionIndex = position;
        if (item.isSectionFooter()){
            lastSectionIndex = position-1;
            return lastSectionIndex;
        }

        Log.i(TAG, "findLastSectionItem: mProvider.getGroupItem(position+1):" + dataProvider.getGroupItem(position));
        while (position < groupCount-1 ) {
            if (dataProvider.getGroupItem(position+1).isSectionFooter()){
                break;
            }
            position+=1;
            lastSectionIndex=position;
        }


        //Check groupHeader
        while (position < lastSectionIndex) {
            AbstractAddRemoveExpandableDataProvider.GroupData nextItem = dataProvider.getGroupItem(position + 1);

            if (nextItem.isSectionHeader()) {
                break;
            }

            position += 1;
        }

        return position;
    }

    private void pushSQLite(){

    }

    private void updateFirestore(){
        AbstractAddRemoveExpandableDataProvider dataProvider = getDataProvider();
        String addressName;

        int groupCount;
        int childCount;
        String userName = "Error";
        Double addressSum;
        Double palettesAmount;
        Double palettesSum;
        Double transportKm;
        Double transportSum;
        groupCount = dataProvider.getGroupCount();
        Boolean checkAllBrickItems;
        final Map<String, Object> userItems = new HashMap<>();

        //All groups
        Map<String, Object> addressItems = new HashMap<>();
        Map<String, Object> addressList = null;

        for (int i=0; i<groupCount; i++){

            if(getGroupHead(i))
            {

                userName = dataProvider.getGroupItem(i).getText();
                addressList = new HashMap<>();
            }
            else if (!dataProvider.getGroupItem(i).isSectionHeader() ||
                    !dataProvider.getGroupItem(i).isSectionFooter())
            {


//                addressList = createSectionOfAddress((i));


                addressItems.put(userName, createSectionOfAddress(i, addressList)); //is it returns value???
            }
            else if(dataProvider.getGroupItem(i).isSectionFooter())
            {
                String footerTXT = dataProvider.getGroupItem(i).getText();
                Log.i(TAG, "updateFirestore: footerTXT:" + footerTXT);
            }

//            userItems.put(userName, addressItems);

//            if (dataProvider.getGroupItem(i).isSectionHeader()){
//                userName = dataProvider.getGroupItem(i).getText();
//
//                for (int groupItem=0; i<groupCount;i++){
//
//                    //check if section address
//                    if (!dataProvider.getGroupItem(i+1).isSectionHeader() ||
//                            !dataProvider.getGroupItem(i+1).isSectionFooter()){
//
//                        int lastSectionItem = lastSectionItem(dataProvider, i+1);
//                        final Map<String, Object> addressItems = new HashMap<>();
//                    }
//                }
//
//                int lastSectionItem = lastSectionItem(dataProvider, i+1);
//                final Map<String, Object> addressItems = new HashMap<>();
//
//                for (int sectionGroupItem=i; sectionGroupItem<lastSectionItem; sectionGroupItem++){
//                    addressName = dataProvider.getGroupItem(i+1).getText();
//
//                    palettesAmount = .0;
//                    addressSum =    0.0;
//                    Timestamp time;
//                    transportKm =   .0;
//                    childCount = dataProvider.getChildCount(i+1);
//
//                    final Map<String, Object> bricksItems = new HashMap<>();
//
//                    for (int j =0; j<childCount; j++){
//
//                        if(!dataProvider.getChildItem(i+1, j).isSectionFooterTransport() ||
//                                !dataProvider.getChildItem(i+1, j).isSectionFooterTransport()){
//
//                            Double amount  = dataProvider.getChildItem(i+1, j).getChildAmount();
//                            Double price  = dataProvider.getChildItem(i+1, j).getChildPrice();
//
//                            addressSum = addressSum + price;    //sum of address (-minus palettes&transport)
//                            String amount_Price = Double.toString(amount) + "_" + Double.toString(price);
//                            String brickName  = dataProvider.getChildItem(i+1, j).getText();
//
//                            bricksItems.put(brickName, amount_Price);    //addressID
//
//                        } else if (dataProvider.getChildItem(i+1, j).isSectionFooterPaletes()){
//
//                            palettesAmount = dataProvider.getChildItem(i+1, j).getChildAmount();
//                            palettesSum = dataProvider.getChildItem(i+1, j).getChildPrice();
//                            String palettes = dataProvider.getChildItem(i+1, j).getText();
//                            addressSum  = addressSum + palettesSum;
//                        } else if(dataProvider.getChildItem(i+1, j).isSectionFooterTransport()){
//
//                            transportKm = dataProvider.getChildItem(i+1, j).getChildAmount();
//                            transportSum  = dataProvider.getChildItem(i+1, j).getChildPrice();
//                            addressSum  = addressSum + transportSum;
//
//                            String transport = dataProvider.getChildItem(i+1, j).getText();
//                        }
//
//                        bricksItems.put("p", palettesAmount);    //palettes
//                        bricksItems.put("t", 21);    //timestamp
//                        bricksItems.put("s", addressSum);    //sum
//                        bricksItems.put("d", transportKm);    //distance
//
//                    }
//
//                    addressItems.put(addressName, bricksItems);
//
//                }
//
//                //add result(last items) items in here
//
//                userItems.put(userName, addressItems);
//            }

        }
        uploadUserItems(addressItems);
    }

    private boolean getGroupHead(int group){
        AbstractAddRemoveExpandableDataProvider dataProvider = getDataProvider();
        return dataProvider.getGroupItem(group).isSectionHeader();
    }

    private Map<String, Object> createSectionOfAddress(int sectionItem, Map<String, Object> addressList){
        String addressName;
//        final Map<String, Object> addressList = new HashMap<>();

        int childCount;

        AbstractAddRemoveExpandableDataProvider dataProvider = getDataProvider();
        int end =lastSectionItem(dataProvider, sectionItem);

        for (int sectionGroupItem=sectionItem; sectionGroupItem<=end; sectionGroupItem++){
            addressName = dataProvider.getGroupItem(sectionItem).getText();

            childCount = dataProvider.getChildCount(sectionItem+1);

            final Map<String, Object> bricksItems = new HashMap<>();
            Map<String, Object> bricksItemCreated;

            for (int j =0; j<childCount; j++){
                bricksItemCreated= createAddres(sectionItem, j, bricksItems);
                addressList.put(addressName, bricksItemCreated);
            }

//            addressItems.put(addressName, bricksItems);
        }


        return addressList; //
    }

    private Map<String, Object> createAddres(int sectionItem, int j, Map<String, Object> bricksItems){
        AbstractAddRemoveExpandableDataProvider dataProvider = getDataProvider();
        Double addressSum;
        Double palettesAmount;
        Double palettesSum;
        Double transportKm;
        Double transportSum;
        palettesAmount = .0;
        addressSum =    0.0;
        transportKm =   .0;


        if(!dataProvider.getChildItem(sectionItem+1, j).isSectionFooterTransport() ||
                !dataProvider.getChildItem(sectionItem+1, j).isSectionFooterTransport()){

            Double amount  = dataProvider.getChildItem(sectionItem+1, j).getChildAmount();
            Double price  = dataProvider.getChildItem(sectionItem+1, j).getChildPrice();

            addressSum = addressSum + price;    //sum of address (-minus palettes&transport)
            String amount_Price = Double.toString(amount) + "_" + Double.toString(price);
            String brickName  = dataProvider.getChildItem(sectionItem+1, j).getText();

            bricksItems.put(brickName, amount_Price);    //addressID

        } else if (dataProvider.getChildItem(sectionItem+1, j).isSectionFooterPaletes()){

            palettesAmount = dataProvider.getChildItem(sectionItem+1, j).getChildAmount();
            palettesSum = dataProvider.getChildItem(sectionItem+1, j).getChildPrice();
            String palettes = dataProvider.getChildItem(sectionItem+1, j).getText();
            addressSum  = addressSum + palettesSum;
        } else if(dataProvider.getChildItem(sectionItem+1, j).isSectionFooterTransport()){

            transportKm = dataProvider.getChildItem(sectionItem+1, j).getChildAmount();
            transportSum  = dataProvider.getChildItem(sectionItem+1, j).getChildPrice();
            addressSum  = addressSum + transportSum;

            String transport = dataProvider.getChildItem(sectionItem+1, j).getText();
        }


        bricksItems.put("p", palettesAmount);    //palettes
        bricksItems.put("t", Timestamp.now());    //timestamp
        bricksItems.put("s", addressSum);    //sum
        bricksItems.put("d", transportKm);    //distance


        return bricksItems;
    }

    private void uploadUserItems(final Map<String, Object> userItems){

        mOrdersRef.set(userItems).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFail: e:\n" + e);

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuc: brickW3 created 6.0:  " + userItems);

            }
        });
    }



    // implements ExpandableItemPinnedMessageDialogFragment.EventListener
    @Override
    public void onNotifyExpandableItemPinnedDialogDismissed(int groupPosition, int childPosition, boolean ok) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);

        if (childPosition == RecyclerView.NO_POSITION) {
            // group item
            getDataProvider().getGroupItem(groupPosition).setPinned(ok);
            ((AddRemoveExpandableExampleFragment) fragment).notifyGroupItemChanged(groupPosition);
        } else {
            // child item
            getDataProvider().getChildItem(groupPosition, childPosition).setPinned(ok);
            ((AddRemoveExpandableExampleFragment) fragment).notifyChildItemChanged(groupPosition, childPosition);
        }
    }

    public AbstractAddRemoveExpandableDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((ExampleAddRemoveExpandableDataProviderFragment) fragment).getDataProvider();
    }
}
