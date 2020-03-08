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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.lumibricks.FilterDialogFragment;
import com.lumibricks.MainActivity;
import com.lumibricks.R;
import com.lumibricks.data.AbstractAddRemoveExpandableDataProvider;
import com.lumibricks.data.AbstractExpandableDataProvider;
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


    private boolean mAllowItemsMoveAcrossSections;

    private RecyclerViewExpandableItemManager mExpandableItemManager;
    private AbstractAddRemoveExpandableDataProvider mProvider;

    //onUnderSwipeableViewButtonClick
    private EventListener mEventListener;
    private int whenNewBrickGroupPosition;

    @Override
    public void onManufacture(Manufacture manufacture) {
        newBrick = manufacture;
        Log.d(TAG, "onManufacture: \nwhenNewBrickGroupPosition:" + whenNewBrickGroupPosition );
        mProvider.addChildItem(whenNewBrickGroupPosition, 0, newBrick);
        mExpandableItemManager.notifyChildItemInserted(whenNewBrickGroupPosition, 0);
        mExpandableItemManager.expandGroup(whenNewBrickGroupPosition);
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
//        public EditText mButtonCargoAddress;
        //        public TextView mTextEuroTogether;
        public ImageButton mImageAddItem;

//        public EditText mEditTextBrickPrice;
//        public EditText mEditTextBrickAmount;
//        public EditText mEditTextBrickPricePalettes;
//        public TextView mTextEuroTogetherPalettes;
//        public TextView mTextTransport;
//        public EditText mEditTextAmountPalettes;

        public Button mButtonChangeTransportUnit;
        public Button mButtonchangeUnitPalettes;
//        public TextView mTextPallettes;

        public MyGroupViewHolder(View v, View.OnClickListener clickListener) {
            super(v, clickListener);
            mIndicator = v.findViewById(R.id.indicator);
            mImageAddItem = v.findViewById(R.id.image_button_add_item);
//            mImageAddItem.setOnClickListener(clickListener);
//            mContainer.setOnClickListener(clickListener);

//            mButtonChangeTransportUnit = v.findViewById(R.id.button_change_transport_unit);
//            mButtonchangeUnitPalettes = v.findViewById(R.id.button_change_unit_palettes);

//            mButtonCargoAddress = v.findViewById(R.id.text_cargo_address);
//            mTextPallettes = v.findViewById(R.id.text_palettes);
//            mEditTextAmountPalettes = v.findViewById(R.id.edit_text_brick_amount_palettes);
//            mEditTextBrickPricePalettes = v.findViewById(R.id.edit_text_brick_price_palettes);
//            mTextEuroTogetherPalettes = v.findViewById(R.id.text_euro_together_palettes);
//            mTextTransport = v.findViewById(R.id.text_transport);
//            mButtonChangeUnit = v.findViewById(R.id.button_change_unit);
//            mEditTextBrickAmount = v.findViewById(R.id.edit_text_brick_amount);
//            mEditTextBrickPrice = v.findViewById(R.id.edit_text_brick_price);
//            mTextEuroTogether = v.findViewById(R.id.text_euro_together);
//
//            mButtonCargoAddress.setOnClickListener(clickListener);
//            mTextPallettes.setOnClickListener(clickListener);
//            mButtonChangeTransportUnit.setOnClickListener(clickListener);
//            mButtonchangeUnitPalettes.setOnClickListener(clickListener);
//            mEditTextAmountPalettes.setOnClickListener(clickListener);
//            mEditTextBrickPricePalettes.setOnClickListener(clickListener);
//            mTextEuroTogetherPalettes.setOnClickListener(clickListener);
//            mTextTransport.setOnClickListener(clickListener);
//            mButtonChangeUnit.setOnClickListener(clickListener);
//            mEditTextBrickAmount.setOnClickListener(clickListener);
//            mEditTextBrickPrice.setOnClickListener(clickListener);
//            mTextEuroTogether.setOnClickListener(clickListener);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public Button mButtonChangeUnit;
        public EditText mEditTextBrickAmount;
        public Button mEditTextBrickPrice;
        public TextView mTextBrickEuroSum;

        public MyChildViewHolder(View v, View.OnClickListener clickListener) {
            super(v, clickListener);
            mButtonChangeUnit = v.findViewById(R.id.button_change_unit);
            mEditTextBrickAmount = v.findViewById(R.id.edit_text_brick_amount);
            mEditTextBrickPrice = v.findViewById(R.id.edit_text_brick_price);
            mTextBrickEuroSum = v.findViewById(R.id.text_euro_together);

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
        } else {
            return GROUP_ITEM_VIEW_TYPE_SECTION_ITEM;
        }
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {

        return 0;
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
//        holder.itemView.setClickable(true);


    }

    private void onBindItemGroupViewHolder(@NonNull MyGroupViewHolder holder, int groupPosition) {
        // group item
        final AbstractAddRemoveExpandableDataProvider.GroupData item = mProvider.getGroupItem(groupPosition);

        // mark as clickable ItemGroupViewHolder
        holder.mImageAddItem.setOnClickListener(mItemOnClickListener);
        holder.itemView.setClickable(true);

//        mImageAddItem = v.findViewById(R.id.image_button_add_item);
//        mImageAddItem.setOnClickListener(clickListener);

        // set text
        holder.mTextView.setText(R.string.brick_order_address);
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

    @Override
    public void onBindChildViewHolder(@NonNull MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // group item
        final AbstractAddRemoveExpandableDataProvider.ChildData item = mProvider.getChildItem(groupPosition, childPosition);

        if (newBrick != null){
            Log.i(TAG, "onBindChildViewHolder: \n newBrick.getBrick(): " + newBrick.getBrick() + "\nitem.getText(): " + item.getText());
            Log.i(TAG, "onBindChildViewHolder: item: " + item + "\n newBrick: " + newBrick);
            holder.mTextView.setText(newBrick.getBrick());
            holder.mTextView.setText(item.getText());
            holder.mEditTextBrickAmount.setText( String.valueOf(item.getChildAmount()));
            holder.mButtonChangeUnit.setText(item.getChildUnit());
            holder.mEditTextBrickPrice.setText(item.getChildPrice().toString());
//            double a = item.getChildAmount();
//            double b = item.getChildPrice();
//            double c = a*b;
            double g = item.getChildAmount()*item.getChildPrice();
            String euroSum = String.valueOf(g);
            holder.mTextBrickEuroSum.setText(euroSum);

            Log.d(TAG, "onBindChildViewHolder: Manufacture name: " + newBrick.getBrick()) ;
        }else{
            holder.mTextView.setText(item.getText());
//            holder.mTextView.setText(R.string.app_name);
        }
        // set text
//        holder.mTextView.setText(R.string.app_name);

//        final DraggableItemState dragState = holder.getDragState();
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
        if (!isSectionHeader(holder)) {
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

            case R.id.containerItem:
                if (childPosition == RecyclerView.NO_POSITION) {
//                    handleOnClickGroupItemContainerView(groupPosition);
                    Log.d(TAG, "onClickItemView: handleOnClickGroupItemContainerView");
                } else {
//                    handleOnClickChildItemContainerView(groupPosition, childPosition);
                    Log.d(TAG, "onClickItemView: handleOnClickChildItemContainerView");
                }
                break;

            // child item events

            case R.id.button_change_unit:
                handleOnClickChildItemAddAboveButton(groupPosition, childPosition);
                break;
            case R.id.edit_text_brick_amount:
                handleOnClickChildItemAddBelowButton(groupPosition, childPosition);
                break;
            case R.id.edit_text_brick_price:
                handleOnClickChildItemRemoveButton(groupPosition, childPosition);
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

//        filterDialogFragment.setTargetFragment((Fragment) AddRemoveExpandableExampleFragment.SavedState, 1);
//        filterDialogFragment.show(get)
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        startActivity(intent);
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
        if (!isSectionHeader(holder)) {
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
        AbstractAddRemoveExpandableDataProvider.GroupData item = mProvider.getGroupItem(position);

        if (item.isSectionHeader()) {
            throw new IllegalStateException("section item is expected");
        }

        final int lastIndex = getGroupCount() - 1;

        while (position < lastIndex) {
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
        return (groupViewType != GROUP_ITEM_VIEW_TYPE_SECTION_HEADER);
    }
}
