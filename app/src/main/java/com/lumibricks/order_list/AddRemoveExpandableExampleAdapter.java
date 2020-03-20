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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemState;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemState;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.expandable.GroupPositionItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.lumibricks.FilterDialogFragment;
import com.lumibricks.R;
import com.lumibricks.data.AbstractAddRemoveExpandableDataProvider;
import com.lumibricks.model.Manufacture;
import com.lumibricks.utils.DrawableUtils;
import com.lumibricks.utils.ViewUtils;
import com.lumibricks.widget.ExpandableItemIndicator;

class AddRemoveExpandableExampleAdapter
        extends AbstractExpandableItemAdapter<AddRemoveExpandableExampleAdapter.MyGroupViewHolder, AddRemoveExpandableExampleAdapter.MyChildViewHolder>
        implements ExpandableDraggableItemAdapter<AddRemoveExpandableExampleAdapter.MyGroupViewHolder, AddRemoveExpandableExampleAdapter.MyChildViewHolder>, FilterDialogFragment.ManufactureListener {
    private static final String TAG = "MyExpandableItemAdapter";

    private Context context;
    private Manufacture newBrick;

    private static final int GROUP_ITEM_VIEW_TYPE_SECTION_HEADER = 0;
    private static final int GROUP_ITEM_VIEW_TYPE_SECTION_ITEM = 1;
    private static final int GROUP_ITEM_VIEW_TYPE_SECTION_FOOTER = 2;

    private static final int CHILD_ITEM_VIEW_TYPE_SECTION_FOOTER = 0;
    private static final int CHILD_ITEM_VIEW_TYPE_SECTION_ITEM = 1;


    private boolean mAllowItemsMoveAcrossSections;

    private RecyclerViewExpandableItemManager mExpandableItemManager;
    private AbstractAddRemoveExpandableDataProvider mProvider;

    //onUnderSwipeableViewButtonClick
    private EventListener mEventListener;
    private int whenNewBrickGroupPosition;
    private String editedBrickName;

    @Override
    public void onManufacture(Manufacture manufacture) {
        newBrick = manufacture;
        Log.d(TAG, "onManufacture: \nwhenNewBrickGroupPosition:" + whenNewBrickGroupPosition );
        mProvider.addChildItem(whenNewBrickGroupPosition, 0, newBrick);
        mExpandableItemManager.notifyChildItemInserted(whenNewBrickGroupPosition, 0);
        mExpandableItemManager.expandGroup(whenNewBrickGroupPosition);
        updateOrderSum(whenNewBrickGroupPosition);
    }

    public interface EventListener {
    void onItemPinned(int position);

    void onItemViewClicked(View v);

    void onUnderSwipeableViewButtonClicked(View v);
}


    private View.OnClickListener mItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddRemoveExpandableExampleAdapter.this.onClickItemView(v);
        }
    };

    public static abstract class MyBaseViewHolder extends AbstractDraggableItemViewHolder implements ExpandableItemViewHolder {
        public FrameLayout mContainer;
        public View mDragHandle;
        public TextView mTextView;
//        public TextView mTextViewItemName;
        private BottomNavigationView bottomNavigationView;
        private final ExpandableItemState mExpandState = new ExpandableItemState();

//View.OnClickListener clickListener
        public MyBaseViewHolder(View v, View.OnClickListener clickListener) {
            super(v);
            mContainer = v.findViewById(R.id.containerItem);
//            mContainer = v.findViewById(R.id.container_base);
            mDragHandle = v.findViewById(R.id.drag_handle);
//            mTextView = v.findViewById(R.id.text_cargo_address);

            mTextView = v.findViewById(android.R.id.text1);
//            mContainer.setOnClickListener(clickListener);

        }

        @Override
        public void setExpandStateFlags(int flags) {
            mExpandState.setFlags(flags);
        }

        @Override
        public int getExpandStateFlags() {
            return mExpandState.getFlags();
        }

        @NonNull
        @Override
        public ExpandableItemState getExpandState() {
            return mExpandState;
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        public ExpandableItemIndicator mIndicator;

        public ImageButton mImageAddItem;
        public ImageButton mImageEditAddress;
        public ImageButton mImageAddAddress;
        public ImageButton mImageEditClient;

        public RelativeLayout mRelativeLayoutInfo;
        public RelativeLayout mRelativeLayoutFooterInfo;
        public TextView mFooterTextOrderPrice;

        public TextView mFooterTextViewSum;
        public TextView mFooterTextViewPvn;
        public TextView mFooterTextViewPvn_Sum;

        public TextView mTextPalettes;
        public TextView mTextTransport;
        public TextView mTextExtra;

        public MyGroupViewHolder(View v, View.OnClickListener clickListener) {
            super(v, clickListener);
            //list item cargo details
            mIndicator = v.findViewById(R.id.indicator);
            mImageAddItem = v.findViewById(R.id.image_button_add_item);
            mImageEditAddress = v.findViewById(R.id.image_button_edit_address);

            //list section header
            mImageAddAddress = v.findViewById(R.id.image_button_add_address);
            mImageEditClient = v.findViewById(R.id.image_button_edit_client);

            mRelativeLayoutInfo = v.findViewById(R.id.footerRelLayExtraInfo);
            mRelativeLayoutFooterInfo = v.findViewById(R.id.relLay_footer_main);

            mFooterTextOrderPrice = v.findViewById(R.id.footerTextOrderPrice);
            mFooterTextViewSum  =  v.findViewById(R.id.footerTextViewNoPvnNum);
            mFooterTextViewPvn  =  v.findViewById(R.id.footerTextViewPVNNum);
            mFooterTextViewPvn_Sum  =  v.findViewById(R.id.footerTextViewPVN_SUMNum);

            mTextPalettes = v.findViewById(R.id.footerTextViewPalettes);
            mTextTransport = v.findViewById(R.id.footerTextViewExtra);
            mTextExtra = v.findViewById(R.id.footerTextViewExtra2);

//            mButtonChangeTransportUnit = v.findViewById(R.id.button_change_transport_unit);
//            mButtonchangeUnitPalettes = v.findViewById(R.id.button_change_unit_palettes);

//            mTextTransport = v.findViewById(R.id.text_transport);
//            mTextEuroTogetherPalettes = v.findViewById(R.id.text_euro_together_palettes);
//            mButtonCargoAddress = v.findViewById(R.id.text_cargo_address);
//
//            mTextTransport.setOnClickListener(clickListener);
//            mTextEuroTogetherPalettes.setOnClickListener(clickListener);
//            mButtonCargoAddress.setOnClickListener(clickListener);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public Button mButtonChangeUnit;
        public EditText mEditTextBrickAmount;
        public Button mEditTextBrickPrice;
        public TextView mTextBrickEuroSum;
        public TextView mTextView2;

        public MyChildViewHolder(View v, View.OnClickListener clickListener) {
            super(v, clickListener);
            mButtonChangeUnit = v.findViewById(R.id.button_change_unit);
            mEditTextBrickAmount = v.findViewById(R.id.edit_text_brick_amount);
            mEditTextBrickPrice = v.findViewById(R.id.edit_text_brick_price);
            mTextBrickEuroSum = v.findViewById(R.id.text_euro_together);
            mTextView2 = v. findViewById(R.id.textView2);

            mButtonChangeUnit.setOnClickListener(clickListener);
            mEditTextBrickAmount.setOnClickListener(clickListener);
//            mEditTextBrickPrice.setOnClickListener(clickListener);
//            mContainer.setOnClickListener(clickListener);
//            mTextBrickEuroSum.setOnClickListener(clickListener);
        }
    }

    public AddRemoveExpandableExampleAdapter(
            RecyclerViewExpandableItemManager expandableItemManager,
            AbstractAddRemoveExpandableDataProvider dataProvider,
            Context c
    ) {

        this.context= c;
        mExpandableItemManager = expandableItemManager;
        mProvider = dataProvider;
//        mItemViewOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: AddRemoveExpandableExampleAdapter");
//                AddRemoveExpandableExampleAdapter.this.onClickItemView(v);
//            }
//        };

        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    private void onUnderSwipeableViewButtonClick(View v) {
        if (mEventListener != null) {
            mEventListener.onUnderSwipeableViewButtonClicked(
                    RecyclerViewAdapterUtils.getParentViewHolderItemView(v));
        }
    }

    @Override
    public int getGroupCount() {
        return mProvider.getGroupCount();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mProvider.getChildCount(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mProvider.getGroupItem(groupPosition).getGroupId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mProvider.getChildItem(groupPosition, childPosition).getChildId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        final AbstractAddRemoveExpandableDataProvider.GroupData item = mProvider.getGroupItem(groupPosition);
        if (item.isSectionHeader()) {
            return GROUP_ITEM_VIEW_TYPE_SECTION_HEADER;
        } else if(item.isSectionFooter()){
            return GROUP_ITEM_VIEW_TYPE_SECTION_FOOTER;
        } else {
            return GROUP_ITEM_VIEW_TYPE_SECTION_ITEM;
        }
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        final AbstractAddRemoveExpandableDataProvider.ChildData item = mProvider.getChildItem(groupPosition, childPosition);
        if (item.isSectionFooterPaletes()) {
            return CHILD_ITEM_VIEW_TYPE_SECTION_FOOTER;
        } else if (item.isSectionFooterTransport()){
            return CHILD_ITEM_VIEW_TYPE_SECTION_FOOTER;
        } else {
            return CHILD_ITEM_VIEW_TYPE_SECTION_ITEM;
        }
    }

    @Override
    @NonNull
    public MyGroupViewHolder onCreateGroupViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

//        View v1 = inflater.inflate(R.layout.list_item_cargo_details, parent, false);
        final View v;
        switch (viewType) {
            case GROUP_ITEM_VIEW_TYPE_SECTION_HEADER:
                v = inflater.inflate(R.layout.list_section_header, parent, false);
                break;
            case GROUP_ITEM_VIEW_TYPE_SECTION_ITEM:
                v = inflater.inflate(R.layout.list_item_cargo_details, parent, false);
                break;
            case GROUP_ITEM_VIEW_TYPE_SECTION_FOOTER:
                v = inflater.inflate(R.layout.list_section_footer, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected viewType (= " + viewType + ")");
        }
        return new MyGroupViewHolder(v, mItemOnClickListener);
    }

    @Override
    @NonNull
    public MyChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.edit_brick_list_item, parent, false);
//        mItemViewOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: onCreateChildViewHolder");
//                AddRemoveExpandableExampleAdapter.this.onClickItemView(v);
//            }
//        };
        return new MyChildViewHolder(v, mItemOnClickListener);
    }

    @Override
    public void onBindGroupViewHolder(@NonNull MyGroupViewHolder holder, int groupPosition, int viewType) {

        switch (viewType) {
            case GROUP_ITEM_VIEW_TYPE_SECTION_HEADER:
                onbBindSectionHeaderGroupViewHolder(holder, groupPosition);
                break;
            case GROUP_ITEM_VIEW_TYPE_SECTION_ITEM:
                onBindItemGroupViewHolder(holder, groupPosition);
                break;
            case GROUP_ITEM_VIEW_TYPE_SECTION_FOOTER:
                onBindSectionFooterGroupViewHolder(holder, groupPosition);
                break;
            default:
                throw new IllegalStateException("Unexpected viewType (= " + viewType + ")");

        }
//        final AbstractAddRemoveExpandableDataProvider.BaseData item = mProvider.getGroupItem(groupPosition);
        // set text
//        holder.mTextView.setText(R.string.brick_order_address);


        // set background resource (target view ID: container)
//        final ExpandableItemState expandState = holder.getExpandState();
//
//        if (expandState.isUpdated()) {
//            int bgResId;
//
//            if (expandState.isExpanded()) {
//                bgResId = R.drawable.bg_group_item_expanded_state;
//            } else {
//                bgResId = R.drawable.bg_group_item_normal_state;
//            }
//
//            holder.mContainer.setBackgroundResource(bgResId);
//        }
    }

    private void onbBindSectionHeaderGroupViewHolder(@NonNull MyGroupViewHolder holder, int groupPosition) {
        // group SectionHeader
        final AbstractAddRemoveExpandableDataProvider.GroupData item = mProvider.getGroupItem(groupPosition);
        // set text
        holder.mTextView.setText(item.getText());
        holder.mImageEditClient.setOnClickListener(mItemOnClickListener);
        holder.mImageAddAddress.setOnClickListener(mItemOnClickListener);
//        holder.itemView.setClickable(true);
    }

    private void onBindItemGroupViewHolder(@NonNull MyGroupViewHolder holder, int groupPosition) {
        // group item
        final AbstractAddRemoveExpandableDataProvider.GroupData item = mProvider.getGroupItem(groupPosition);
        // mark as clickable ItemGroupViewHolder
        holder.mImageAddItem.setOnClickListener(mItemOnClickListener);
        holder.mImageEditAddress.setOnClickListener(mItemOnClickListener);
        holder.itemView.setClickable(true);

//        mImageAddItem = v.findViewById(R.id.image_button_add_item);
//        mImageAddItem.setOnClickListener(clickListener);

        // set text
        holder.mTextView.setText(item.getText());
//      holder.mTextView.setText(item.getText());

        // set background resource (target view ID: container)
        final DraggableItemState dragState = holder.getDragState();
        final ExpandableItemState expandState = holder.getExpandState();

        if (dragState.isUpdated() || expandState.isUpdated()) {
            int bgResId;
            boolean animateIndicator = expandState.hasExpandedStateChanged();

            if (dragState.isActive()) {
                bgResId = R.drawable.bg_group_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if (dragState.isDragging() && dragState.isInRange()) {
                bgResId = R.drawable.bg_group_item_dragging_state;
            } else if (expandState.isExpanded()) {
                bgResId = R.drawable.bg_group_item_expanded_state;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(expandState.isExpanded(), animateIndicator);
        }
    }

    private void onBindSectionFooterGroupViewHolder(MyGroupViewHolder holder, int gropPosition){
        holder.mRelativeLayoutInfo.setVisibility(View.VISIBLE);
        holder.mRelativeLayoutInfo.setOnClickListener(mItemOnClickListener);
        holder.mRelativeLayoutFooterInfo.setVisibility(View.GONE);
//        holder.mFooterTextOrderPrice.setText(stringGroupPriceSum);

        int start = findFirstSectionItem(gropPosition);
        int last = findLastSectionItem(gropPosition);
        Log.i(TAG, "onBindSectionFooterGroupViewHolder: start: " +start + "\nlast: " + last);
        Double groupPriceBricks =mProvider.getChildPriceSum(start, last);

        //hidden layout
        String stringGroupPriceSum = String.valueOf(groupPriceBricks);
        String stringGroupPricePvn = String.valueOf(groupPriceBricks*0.21);
        String stringGroupPriceSum_Pvn = String.valueOf(groupPriceBricks*1.21);
        holder.mFooterTextViewSum.setText(stringGroupPriceSum);
        holder.mFooterTextViewPvn.setText(stringGroupPricePvn);
        holder.mFooterTextViewPvn_Sum.setText(stringGroupPriceSum_Pvn);

        int groupCount = mProvider.getGroupCount();
        int childCount = mProvider.getChildCount(gropPosition);
        long id = mProvider.getGroupItem(gropPosition).getGroupId();
        String gropCount= String.valueOf(groupCount);

        holder.mTextPalettes.setText(gropCount);
        holder.mTextTransport.setText(String.valueOf(childCount));
        holder.mTextExtra.setText(String.valueOf(id));
    }

    private void updateOrderSum(int a){
        int start = findFirstSectionItem(whenNewBrickGroupPosition);
        int end = findLastSectionItem(a);
        Log.i(TAG, "updateOrderSum: start: " + start + "\nend: " + end);
        Double groupPriceBricks = mProvider.getChildPriceSum(start, end);
        AbstractAddRemoveExpandableDataProvider.GroupData gD = mProvider.getGroupItem(end+1);
        if (gD.isSectionFooter()){
            gD.setGroupPrice(groupPriceBricks);
        }else {
            Log.i(TAG, "updateOrderSum: groupPrice is not set  in footer");
        }
        Log.i(TAG, "updateOrderSum: " + groupPriceBricks);

//        mExpandableItemManager.notifyGroupItemChanged(a+1);
        mExpandableItemManager.notifyGroupItemChanged(end+1);


    }

    @Override
    public void onBindChildViewHolder(@NonNull MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // group item
        final AbstractAddRemoveExpandableDataProvider.ChildData item = mProvider.getChildItem(groupPosition, childPosition);

        if (newBrick != null){
            Log.i(TAG, "onBindChildViewHolder: \n newBrick.getBrick(): " + newBrick.getBrick() + "\nitem.getText(): " + item.getText());
            Log.i(TAG, "onBindChildViewHolder: item: " + item + "\n newBrick: " + newBrick);
            holder.mTextView.setText(newBrick.getBrick());
            Log.d(TAG, "onBindChildViewHolder: Manufacture name: " + newBrick.getBrick()) ;
        }else{
            holder.mTextView.setText(item.getText());
        }
        holder.mTextView.setText(item.getText());
        holder.mEditTextBrickAmount.setText( String.valueOf(item.getChildAmount()));
        holder.mButtonChangeUnit.setText(item.getChildUnit());
        holder.mEditTextBrickPrice.setText(item.getChildPrice().toString());

        double amount_price = item.getChildAmount()*item.getChildPrice();
        String euroSum = String.valueOf(amount_price);
        holder.mTextBrickEuroSum.setText(euroSum);

        final DraggableItemState dragState = holder.getDragState();

        if (dragState.isUpdated()) {
            int bgResId;

            if (dragState.isActive()) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if (dragState.isDragging() && dragState.isInRange()) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }

        // set background resource (target view ID: container)
//        int bgResId;
//        bgResId = R.drawable.bg_item_normal_state;
//        holder.mContainer.setBackgroundResource(bgResId);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(@NonNull MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // NOTE: Handles all click events manually

        // check is normal item
        if (!isSectionHeader(holder) || !isSectionFooter(holder)) {
            return false;
        }

        // check is enabled
        if (!(holder.itemView.isEnabled())) {
            return false;
        }

        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);

        return !ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    void onClickItemView(View v) {
        RecyclerView.ViewHolder vh = RecyclerViewAdapterUtils.getViewHolder(v);
        int flatPosition = vh.getAdapterPosition();

        if (flatPosition == RecyclerView.NO_POSITION) {
            return;
        }

        long expandablePosition = mExpandableItemManager.getExpandablePosition(flatPosition);
        int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup(expandablePosition);
        int childPosition = RecyclerViewExpandableItemManager.getPackedPositionChild(expandablePosition);

        switch (v.getId()) {
            // common events

            // group item events
            //TODO 1
            //Open fragment: creteBrickItem
            case R.id.image_button_add_item:
                handleOnClickGroupItemAddChildTopButton(groupPosition);
                break;
            case R.id.image_button_edit_address:
                //TODO pop up dialog ==> set new addres
                if(mProvider.getGroupItem(groupPosition).isSectionHeader() || mProvider.getGroupItem(groupPosition).isSectionFooter()){
                    return;
                }
                handleOnClickGroupItemEditAddress(groupPosition);
                break;

            case R.id.image_button_add_address:
                if (mProvider.getGroupItem(groupPosition).isSectionHeader()){
                    handleOnClickGroupItemAddressAdd(groupPosition);
                }

                //TODO pop up dialog ==>
                // create new (listitemcargo) header item ==> addres *Done*
                break;

            case R.id.image_button_edit_client:
                if (mProvider.getGroupItem(groupPosition).isSectionHeader()){
                    handleOnClickGroupItemEditPerson(groupPosition);
                }

                //TODO pop up dialog ==>
                // create new (listitemcargo) header item ==> addres *Done*
                break;

            case R.id.containerItem:
                if (childPosition == RecyclerView.NO_POSITION) {
//                    handleOnClickGroupItemContainerView(groupPosition);
                    Log.d(TAG, "onClickItemView: handleOnClickGroupItemContainerView (RecyclerView.NO_POSITION)");
                } else {
//                    handleOnClickChildItemContainerView(groupPosition, childPosition);
                    Log.d(TAG, "onClickItemView: handleOnClickChildItemContainerView");
                }
                break;

            // child item events

            case R.id.button_change_unit:
                handleOnClickChildItemUnit(groupPosition, childPosition);
                break;
            case R.id.edit_text_brick_amount:
                Log.i(TAG, "onClickItemView: edit_text_brick_amount");
//                handleOnClickChildItemAddBelowButton(groupPosition, childPosition);
                break;
            case R.id.edit_text_brick_price:
                handleOnClickChildItemRemoveButton(groupPosition, childPosition);
                break;
            case R.id.footerRelLayExtraInfo:
                handleOnClickGroupFooterContainer(groupPosition);
                break;

//            case R.id.button_add_child_top:
//                handleOnClickGroupItemAddChildTopButton(groupPosition);
//                break;
//            case R.id.button_add_child_bottom:
//                handleOnClickGroupItemAddChildBottomButton(groupPosition);
//                break;
//            case R.id.button_add_child_bottom_2:
//                handleOnClickGroupItemAddChild2BottomButton(groupPosition);
//                break;
//            case R.id.button_remove_child_top:
//                handleOnClickGroupItemRemoveChildTopButton(groupPosition);
//                break;
//            case R.id.button_remove_child_bottom:
//                handleOnClickGroupItemRemoveChildBottomButton(groupPosition);
//                break;
//            case R.id.button_remove_child_bottom_2:
//                handleOnClickGroupItemRemoveChild2BottomButton(groupPosition);
//                break;
//            case R.id.button_add_group_above:
//                handleOnClickGroupItemAddAboveButton(groupPosition);
//                break;
//            case R.id.button_add_group_below:
//                handleOnClickGroupItemAddBelowButton(groupPosition);
//                break;
//            case R.id.button_remove_group:
//                handleOnClickGroupItemRemoveButton(groupPosition);
//                break;
//            case R.id.button_clear_children:
//                handleOnClickGroupItemClearChildrenButton(groupPosition);
//                break;
//            // child item events
//            case R.id.button_add_child_above:
//                handleOnClickChildItemAddAboveButton(groupPosition, childPosition);
//                break;
//            case R.id.button_add_child_below:
//                handleOnClickChildItemAddBelowButton(groupPosition, childPosition);
//                break;
//            case R.id.button_remove_child:
//                handleOnClickChildItemRemoveButton(groupPosition, childPosition);
//                break;
            default:
                throw new IllegalStateException("Unexpected click event");
        }
    }

    private void handleOnClickGroupItemAddChildTopButton(int groupPosition) {
        whenNewBrickGroupPosition = groupPosition;
        openDialogFragmentNewBrick();

        Log.d(TAG, "handleOnClickGroupItemAddChildTopButton: Fragment opened!");
        if (newBrick!=null){
            Log.d(TAG, "openDialogFragmentNewBrick: Most likely... Old brick...:" + newBrick);
        }else {
            Log.d(TAG, "openDialogFragmentNewBrick: Most likely... New brick in progress...:\n Wait for: onManufacture");
        }
//        mProvider.addChildItem(groupPosition, 0, newBrick);

//        mExpandableItemManager.notifyChildItemInserted(groupPosition, 0);
//        mExpandableItemManager.expandGroup(groupPosition);

    }

    private void handleOnClickGroupItemEditPerson(final int groupPosition) {
        //groupPosition == isSectionHed
        String oldClientName = mProvider.getGroupItem(groupPosition).getText();
        int layoutView = R.layout.dialog_edit_item_text;
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddRemoveExpandableExampleAdapter.this.context);
        AddRemoveExpandableExampleActivity addRemoveExpandableExampleActivity = (AddRemoveExpandableExampleActivity) context;
        LayoutInflater inflater = addRemoveExpandableExampleActivity.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(layoutView, null);
        final EditText editText = dialogView.findViewById(R.id.username);
        editText.setHint(R.string.dialog_message_new_peron);
        builder.setView(dialogView);
        builder.setMessage("Vecais klieenta nosaukums: " + oldClientName)
                .setTitle(R.string.dialog_message_edit_peron);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked OK button
                String a = String.valueOf(editText.getText());
                editedBrickName = a;
                mProvider.getGroupItem(groupPosition).setText(a);
//                mProvider.getGroupItem(groupPosition).setText(editedBrickName);
                mExpandableItemManager.notifyGroupItemChanged(groupPosition);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
 //        mExpandableItemManager.notifyGroupItemInserted(groupPosition + 1);
    }

    private void handleOnClickGroupItemEditAddress(final int groupPosition){
        String oldAddress = mProvider.getGroupItem(groupPosition).getText();
        int layoutView = R.layout.dialog_edit_item_text;
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddRemoveExpandableExampleAdapter.this.context);
        AddRemoveExpandableExampleActivity addRemoveExpandableExampleActivity = (AddRemoveExpandableExampleActivity) context;
        LayoutInflater inflater = addRemoveExpandableExampleActivity.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(layoutView, null);
        final EditText editText = dialogView.findViewById(R.id.username);


        editText.setHint(R.string.dialog_message_new_address);
        builder.setView(dialogView);
        builder.setMessage("Vecā adrese: " + oldAddress)
                .setTitle(R.string.dialog_message_edit_address);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked OK button
                String a = String.valueOf(editText.getText());
                editedBrickName = a;
                //set new brick name
                mProvider.getGroupItem(groupPosition).setText(editedBrickName);
                mExpandableItemManager.notifyGroupItemChanged(groupPosition);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

//        notifyItemChanged(groupPosition);
//        notifyDataSetChanged();
//        notify();

        //Dialog fragment can be shown whit existing address, in case of new address: +address.
        //Dialog shown as https://developer.android.com/guide/topics/ui/dialogs#AddingAList
//        https://developer.android.com/guide/topics/ui/dialogs
        //if choose first item from list ==> create new list item&(save in list + add in current address)
    }

    private void handleOnClickGroupItemAddChildBottomButton(int groupPosition) {
        int childCount = mProvider.getChildCount(groupPosition);

        mProvider.addChildItem(groupPosition, childCount, newBrick);
        mExpandableItemManager.notifyChildItemInserted(groupPosition, childCount);
    }

    private void handleOnClickGroupItemAddChild2BottomButton(int groupPosition) {
        int childCount = mProvider.getChildCount(groupPosition);

        //TODO create 2 bottom items ==> palletes&transport.
            //manage to show bottom items out of range for moving and editing e.t.c(constant)
        mProvider.addChildItem(groupPosition, childCount, newBrick);
        mProvider.addChildItem(groupPosition, childCount + 1, newBrick);
        mExpandableItemManager.notifyChildItemRangeInserted(groupPosition, childCount, 2);
    }

    private void handleOnClickGroupItemRemoveChildTopButton(int groupPosition) {
        int count = mProvider.getChildCount(groupPosition);

        if (count > 0) {
            mProvider.removeChildItem(groupPosition, 0);
            mExpandableItemManager.notifyChildItemRemoved(groupPosition, 0);
        }
    }

    private void handleOnClickGroupItemRemoveChildBottomButton(int groupPosition) {
        int count = mProvider.getChildCount(groupPosition);
        if (count > 0) {
            mProvider.removeChildItem(groupPosition, (count - 1));
            mExpandableItemManager.notifyChildItemRemoved(groupPosition, (count - 1));
        }
    }

    private void handleOnClickGroupItemRemoveChild2BottomButton(int groupPosition) {
        int count = mProvider.getChildCount(groupPosition);
        int removeCount = Math.min(count, 2);

        if (removeCount > 0) {
            mProvider.removeChildItem(groupPosition, (count - removeCount));
            if (removeCount == 2) {
                mProvider.removeChildItem(groupPosition, (count - removeCount));
            }

            mExpandableItemManager.notifyChildItemRangeRemoved(groupPosition, count - removeCount, removeCount);
        }
    }

    private void handleOnClickGroupItemContainerView(int groupPosition) {
        // toggle expanded/collapsed
        if (isGroupExpanded(groupPosition)) {
            mExpandableItemManager.collapseGroup(groupPosition);
        } else {
            mExpandableItemManager.expandGroup(groupPosition);
        }
    }

    private void handleOnClickGroupItemAddAboveButton(int groupPosition) {
        mProvider.addGroupItem(groupPosition);
        mExpandableItemManager.notifyGroupItemInserted(groupPosition);
    }

    private void handleOnClickGroupItemAddBelowButton(int groupPosition) {
        mProvider.addGroupItem(groupPosition + 1);
        mExpandableItemManager.notifyGroupItemInserted(groupPosition + 1);
    }
    private void handleOnClickGroupItemAddressAdd(int groupPosition) {
        //groupPosition == isSectionHed
        groupPosition = groupPosition+1;
        mProvider.addGroupItem(groupPosition);
        mExpandableItemManager.notifyGroupItemInserted(groupPosition);

        //Create new Brick item when new address is added.
        whenNewBrickGroupPosition = groupPosition;
        openDialogFragmentNewBrick();
    }



    private void handleOnClickGroupItemRemoveButton(int groupPosition) {
        mProvider.removeGroupItem(groupPosition);
        mExpandableItemManager.notifyGroupItemRemoved(groupPosition);
    }

    private void handleOnClickGroupItemClearChildrenButton(int groupPosition) {
        int childCount = mProvider.getChildCount(groupPosition);
        mProvider.clearChildren(groupPosition);
        mExpandableItemManager.notifyChildItemRangeRemoved(groupPosition, 0, childCount);
    }

    private void handleOnClickChildItemContainerView(int groupPosition, int childPosition) {
    }

    private void handleOnClickChildItemAddAboveButton(int groupPosition, int childPosition) {
        mProvider.addChildItem(groupPosition, childPosition, newBrick);
        mExpandableItemManager.notifyChildItemInserted(groupPosition, childPosition);
    }

    private void handleOnClickChildItemAddBelowButton(int groupPosition, int childPosition) {
        mProvider.addChildItem(groupPosition, childPosition + 1, newBrick);
        mExpandableItemManager.notifyChildItemInserted(groupPosition, childPosition + 1);
    }

    private void handleOnClickChildItemRemoveButton(int groupPosition, int childPosition) {
        mProvider.removeChildItem(groupPosition, childPosition);
        mExpandableItemManager.notifyChildItemRemoved(groupPosition, childPosition);
    }
    private void handleOnClickChildItemUnit(int groupPosition, int childPosition) {
        AbstractAddRemoveExpandableDataProvider.ChildData cD = mProvider.getChildItem(groupPosition, childPosition);
        String oldUnit = cD.getChildUnit();
        cD.changeUnit(oldUnit);
        Log.i(TAG, "handleOnClickChildItemUnit: notifyChange 1.");
        mExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition);
//        mExpandableItemManager.notifyChildItemRemoved(groupPosition, childPosition);
    }
    private void handleOnClickChildItemAmount(int groupPosition, int childPosition) {
        //TODO text listener...change price + total price
        Log.i(TAG, "handleOnClickChildItemAmount: notifyChange 2.");
        mExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition);
    }

    private void handleOnClickGroupFooterContainer(int groupPosition){
//        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//       ViewGroup viewGroup = mExpandableItemManager


        mProvider.getGroupItem(groupPosition);


    }

    // NOTE: This method is called from Fragment
    public void addGroupItemsBottom(int count) {
        int groupPosition = mProvider.getGroupCount();

        for (int i = 0; i < count; i++) {
            mProvider.addGroupItem(groupPosition + i);
        }

        mExpandableItemManager.notifyGroupItemRangeInserted(groupPosition, count);
    }

    // NOTE: This method is called from Fragment
    public void removeGroupItemsBottom(int count) {
        int groupCount = mProvider.getGroupCount();

        count = Math.min(count, groupCount);

        int groupPosition = groupCount - count;

        for (int i = 0; i < count; i++) {
            mProvider.removeGroupItem(groupPosition);
        }

        mExpandableItemManager.notifyGroupItemRangeRemoved(groupPosition, count);
    }

    // NOTE: This method is called from Fragment
    public void clearGroupItems() {
        int groupCount = mProvider.getGroupCount();

        mProvider.clear();

        mExpandableItemManager.notifyGroupItemRangeRemoved(0, groupCount);
    }

    //
    // Utilities
    //
    private static long getPackedPositionForGroup(int groupPosition) {
        return RecyclerViewExpandableItemManager.getPackedPositionForGroup(groupPosition);
    }

    private static long getPackedPositionForChild(int groupPosition, int childPosition) {
        return RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, childPosition);
    }

    private int getGroupItemFlatPosition(int groupPosition) {
        long packedPosition = getPackedPositionForGroup(groupPosition);
        return getFlatPosition(packedPosition);
    }

    private int getChildItemFlatPosition(int groupPosition, int childPosition) {
        long packedPosition = getPackedPositionForChild(groupPosition, childPosition);
        return getFlatPosition(packedPosition);
    }



    private int getFlatPosition(long packedPosition) {
        return mExpandableItemManager.getFlatPosition(packedPosition);
    }

    private boolean isGroupExpanded(int groupPosition) {
        return mExpandableItemManager.isGroupExpanded(groupPosition);
    }

    @Override
    public boolean onCheckGroupCanStartDrag(@NonNull MyGroupViewHolder holder, int groupPosition, int x, int y) {
        // check is normal item
        if ( !isSectionHeader(holder) || !isSectionFooter(holder) ) {
            return false;
        }
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public boolean onCheckChildCanStartDrag(@NonNull MyChildViewHolder holder, int groupPosition, int childPosition, int x, int y) {

        if (!isSectionFooterPaletes(holder)) {
            return false;
        }

        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
        final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetGroupItemDraggableRange(@NonNull MyGroupViewHolder holder, int groupPosition) {
        if (mAllowItemsMoveAcrossSections) {
            return null;
        } else {
            // sort within the same section
            final int start = findFirstSectionItem(groupPosition);
            // groupPosition-1 => bottomGroupPosition (sum of address/location)
            final int end = findLastSectionItem(groupPosition);

            return new GroupPositionItemDraggableRange(start, end);
        }
    }

    @Override
    public ItemDraggableRange onGetChildItemDraggableRange(@NonNull MyChildViewHolder holder, int groupPosition, int childPosition) {
        if (mAllowItemsMoveAcrossSections) {
            return null;
        } else {
            // sort within the same group

//            return new GroupPositionItemDraggableRange(groupPosition, groupPosition);

//             sort within the same section
            final int start = findFirstSectionItem(groupPosition);
            final int end = findLastSectionItem(groupPosition);

//            return new ChildPositionItemDraggableRange(childPosition, findLastSectionItem(groupPosition));
            return new GroupPositionItemDraggableRange(start, end);

//
//            // sort within the specified child range
//            final int start = 0;
//            final int end = 2;
//
//            return new GroupPositionItemDraggableRange(start, end);
        }
    }

    @Override
    public void onMoveGroupItem(int fromGroupPosition, int toGroupPosition) {
        mProvider.moveGroupItem(fromGroupPosition, toGroupPosition);

    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        //to child position (2 bottoms.. TODO)
        mProvider.moveChildItem(fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);
    }

    @Override
    public boolean onCheckGroupCanDrop(int draggingGroupPosition, int dropGroupPosition) {
        return true;
    }

    @Override
    public boolean onCheckChildCanDrop(int draggingGroupPosition, int draggingChildPosition, int dropGroupPosition, int dropChildPosition) {
        final AbstractAddRemoveExpandableDataProvider.GroupData item = mProvider.getGroupItem(dropGroupPosition);

        if (item.isSectionHeader()) {
            // If the group item is a section header, skip it.
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onGroupDragStarted(int groupPosition) {
        notifyDataSetChanged();

    }

    @Override
    public void onChildDragStarted(int groupPosition, int childPosition) {
        notifyDataSetChanged();
    }

    @Override
    public void onGroupDragFinished(int fromGroupPosition, int toGroupPosition, boolean result) {
        notifyDataSetChanged();
    }

    @Override
    public void onChildDragFinished(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition, boolean result) {
        notifyDataSetChanged();
    }

    private int findFirstSectionItem(int position) {
        AbstractAddRemoveExpandableDataProvider.GroupData item = mProvider.getGroupItem(position);

        if (item.isSectionHeader()) {
            throw new IllegalStateException("section item is expected");
        }

        while (position > 0) {
            AbstractAddRemoveExpandableDataProvider.GroupData prevItem = mProvider.getGroupItem(position - 1);

            if (prevItem.isSectionHeader()) {
                break;
            }

            position -= 1;
        }

        return position;
    }

    private int findLastSectionItem(int position) {
        Log.i(TAG, "findLastSectionItem: position:" + position);
        AbstractAddRemoveExpandableDataProvider.GroupData item = mProvider.getGroupItem(position);

        if (item.isSectionHeader()) {
            throw new IllegalStateException("section item is expected");
        }

        int lastSectionIndex = position;

        //onBindSectionFooterGroupViewHolder needs find last iem in order to calculate value of order
        if (item.isSectionFooter()){
            lastSectionIndex = position-1;
            return lastSectionIndex;
        }

        Log.i(TAG, "findLastSectionItem: mProvider.getGroupItem(position+1):" + mProvider.getGroupItem(position));
        while (position < getGroupCount()-1 ) {
            if (mProvider.getGroupItem(position+1).isSectionFooter()){
                break;
            }
            position+=1;
            lastSectionIndex=position;
        }


        while (position < lastSectionIndex) {
            AbstractAddRemoveExpandableDataProvider.GroupData nextItem = mProvider.getGroupItem(position + 1);

            if (nextItem.isSectionHeader()) {
                break;
            }

            position += 1;
        }

        return position;
    }

    private void openDialogFragmentNewBrick(){
        AddRemoveExpandableExampleActivity addRemoveExpandableExampleActivity = (AddRemoveExpandableExampleActivity) context;
        FragmentManager fm = addRemoveExpandableExampleActivity.getSupportFragmentManager();
        FilterDialogFragment mDialogFragmentnew = new FilterDialogFragment();
        Bundle bundle = new Bundle();
        Log.d("TAG", "onManufactureClicked:");
        bundle.putString("buttonPressed", "brick_navigation_order");
        mDialogFragmentnew.setArguments(bundle);
        mDialogFragmentnew.setNewManufactureListener(AddRemoveExpandableExampleAdapter.this);

        mDialogFragmentnew.show(fm, FilterDialogFragment.TAG);
//        mDialogFragmentnew.onAttach(context);
    }



    public void setAllowItemsMoveAcrossSections(boolean allowed) {
        mAllowItemsMoveAcrossSections = allowed;
    }

    private static boolean isSectionHeader(MyGroupViewHolder holder) {
        final int groupViewType = RecyclerViewExpandableItemManager.getGroupViewType(holder.getItemViewType());
        return (groupViewType != GROUP_ITEM_VIEW_TYPE_SECTION_HEADER );
    }

    private static boolean isSectionFooter(MyGroupViewHolder holder) {
        final int groupViewType = RecyclerViewExpandableItemManager.getGroupViewType(holder.getItemViewType());
        return (groupViewType != GROUP_ITEM_VIEW_TYPE_SECTION_FOOTER);
    }

    private static boolean isSectionFooterPaletes(MyChildViewHolder holder) {
        final int childViewType = RecyclerViewExpandableItemManager.getChildViewType(holder.getItemViewType());
        return (childViewType != CHILD_ITEM_VIEW_TYPE_SECTION_FOOTER);
    }
}
