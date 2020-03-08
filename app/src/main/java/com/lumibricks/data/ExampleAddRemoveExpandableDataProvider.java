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

package com.lumibricks.data;


import android.os.Bundle;
import android.util.Log;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.lumibricks.FilterDialogFragment;
import com.lumibricks.fragment.ExampleAddRemoveExpandableDataProviderFragment;
import com.lumibricks.model.Manufacture;
import com.lumibricks.order_list.AddRemoveExpandableExampleActivity;
import com.lumibricks.order_list.AddRemoveExpandableExampleFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExampleAddRemoveExpandableDataProvider extends AbstractAddRemoveExpandableDataProvider {
    public static Manufacture ChildData;
    private List<Pair<GroupData, List<ChildData>>> mData;

    // for undo group item
    private Pair<GroupData, List<ChildData>> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    // for undo child item
    private ChildData mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

//    private FilterDialogFragment mDialogFragment;





    private static String GROUP_ITEM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String CHILD_ITEM_CHARS = "abcdefghijklmnopqrstuvwxyz";

//    private List<GroupSet> mData;
//    mData = new LinkedList<>();
    private IdGenerator mGroupIdGenerator;
    private IdGenerator mChildIdGenerator;

    public ExampleAddRemoveExpandableDataProvider() {
        mGroupIdGenerator = new IdGenerator();
        mChildIdGenerator= new IdGenerator();

        //|GHI|JKL|MNO|PQR|STU|VWX|YZ
        final String groupItems = "|AB|C|DEF";
        final String childItems = "es";

        mData = new LinkedList<>();
//        mGroupIdGenerator = new IdGenerator();
        int sectionCount = 1;

        for (int i = 0; i < groupItems.length(); i++) {
            //noinspection UnnecessaryLocalVariable
//            final long groupId = i;
            final long groupId = mGroupIdGenerator.next();
            final boolean isSection = (groupItems.charAt(i) == '|');
            //Group txt => section txt(groupHeader)
            final String groupText = isSection ? ("Peron " + sectionCount) : Character.toString(groupItems.charAt(i));
            final ConcreteGroupData group = new ConcreteGroupData(groupId, isSection, groupText);
            final List<ChildData> children = new ArrayList<>();

            if (isSection) {
                sectionCount += 1;
            } else {
                // add child items
                for (int j = 0; j < childItems.length(); j++) {
//                    ConcreteChildData childData = new ConcreteChildData();
                    final long childId = mChildIdGenerator.next();

//                    final long childId = group.generateNewChildId();
                    final String childText = Character.toString(childItems.charAt(j));
                    children.add(new ConcreteChildData(childId, false, false, "Example: "+childText, "m", 30.0, 3.3));
                }
            }

            mData.add(new Pair<GroupData, List<ChildData>>(group, children));
//            mData.add(new Pair<>(group, children));
        }

//        for (int i = 0; i < 1; i++) {
//            addGroupItem(i);
//            for (int j = 0; j < 3; j++) {
//                addChildItem(i, j);
//            }
//        }
    }

//    public void onManufactureCreated() {
////        mDialogFragment = new FilterDialogFragment();
//        Log.d("", "onManufactureClicked: So far so good.");
//        Bundle bundle = new Bundle();
//        bundle.putString("buttonPressed", "brick_navigation_order");
////        mDialogFragment.setArguments(bundle);
////        mDialogFragment.show((ExampleAddRemoveExpandableDataProvider.this).getSupportFragmentManager(), FilterDialogFragment.TAG);
//    }

//    @Override
//    protected FragmentManager getSupportFragmentManager() {
//        return null;
//    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
//        return mData.get(groupPosition).mChildren.size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
//        return mData.get(groupPosition).mGroup;
    }

    @Override
    public ChildData getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

//        final List<ConcreteChildData> children = mData.get(groupPosition).mChildren;
        final List<ChildData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }

    @Override
    public void removeGroupItem(int groupPosition) {
//        mData.remove(groupPosition);
        mLastRemovedGroup = mData.remove(groupPosition);
        mLastRemovedGroupPosition = groupPosition;

        mLastRemovedChild = null;
        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
    }

    @Override
    public void removeChildItem(int groupPosition, int childPosition) {
//        mData.get(groupPosition).mChildren.remove(childPosition);
        mLastRemovedChild = mData.get(groupPosition).second.remove(childPosition);
        mLastRemovedChildParentGroupId = mData.get(groupPosition).first.getGroupId();
        mLastRemovedChildPosition = childPosition;

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;
    }

    @Override
    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }
        final Pair<GroupData, List<ChildData>> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        final Pair<GroupData, List<ChildData>> fromGroup = mData.get(fromGroupPosition);
        final Pair<GroupData, List<ChildData>> toGroup = mData.get(toGroupPosition);
        final Pair<GroupData, List<ChildData>> fromChild = mData.get(fromGroupPosition);


        if (fromGroup.first.isSectionHeader()) {
            throw new IllegalStateException("Source group is a section header!");
        }
        if (toGroup.first.isSectionHeader()) {
            throw new IllegalStateException("Destination group is a section header!");
        }
//        if (toGroup.second.get(fromChildPosition).isSectionFooterPaletes()){
//            throw new IllegalStateException("Destination child is Section Footer Paletes!");
//        }
//        if (toGroup.second.get(fromChildPosition).isSectionFooterTransport()){
//            throw new IllegalStateException("Destination child is Section Footer transport!");
//        }


        final ConcreteChildData item = (ConcreteChildData) fromGroup.second.remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            // assign a new ID
//            final long newId = ((ConcreteGroupData) toGroup.first).generateNewChildId();
            final long newId = mChildIdGenerator.next();

            item.setChildId(newId);
        }

        toGroup.second.add(toChildPosition, item);
    }

    @Override
    public void addGroupItem(int groupPosition) {
//        mData = new LinkedList<>();

        //Can be the same id? (when move/delete, then add new group)
        long newGroupId = getGroupCount() +1;
        String text = getOneCharString(GROUP_ITEM_CHARS, newGroupId);

        final ConcreteGroupData group = new ConcreteGroupData(newGroupId, false, text);
        mGroupIdGenerator.next();
        final List<ChildData> children = new ArrayList<>();

        mData.add(new Pair<GroupData, List<ChildData>>(group, children));
    }

    @Override
    public void addChildItem(int groupPosition, int childPosition, Manufacture manufacture) {
        final Pair<GroupData, List<ChildData>> toGroup = mData.get(groupPosition);

        if (toGroup.first.isSectionHeader()) {
            throw new IllegalStateException("Destination group is a section header!");
        }

        final String childItems = "abc";
        final List<ChildData> children = new ArrayList<>();
//        final long childId = ((ConcreteGroupData) toGroup.first).generateNewChildId();
//        final String childText = Character.toString(childItems.charAt(1));

        final long childId = mChildIdGenerator.next();
        final String childText = manufacture.getBrick();
        final Double childAmount = manufacture.getAmount();
        final double childPrice = manufacture.getPrice();

        final ConcreteChildData item = new ConcreteChildData(childId, false, false, childText, "Gb", childAmount, childPrice);
//        children.add(new ConcreteChildData(childId, childText));

        Log.d("DataProvider", "addChildItem: "+ item.toString());
        toGroup.second.add(childPosition, item);
//        mData.get(groupPosition).addNewChildData(childPosition);
    }

    @Override
    public void clear() {
        mData.clear();
    }

    @Override
    public void clearChildren(int groupPosition) {
        mData.get(groupPosition).second.clear();
//        mData.get(groupPosition).mChildren.clear();
    }

    public static final class ConcreteGroupData extends GroupData {
        private final long mId;
        private final boolean mIsSectionHeader;
        private final String mText;
//        private long mNextChildId;



        ConcreteGroupData(long id, boolean isSectionHeader, String text) {
            mId = id;
            mText = text;
            mIsSectionHeader = isSectionHeader;
//            mNextChildId = 0;
        }

        @Override
        public boolean isSectionHeader() {
            return mIsSectionHeader;
        }

        @Override
        public long getGroupId() {
            return mId;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinned) {

        }

        @Override
        public boolean isPinned() {
            return false;
        }

//        public long generateNewChildId() {
//            final long id = mNextChildId;
//            mNextChildId += 1;
//            return id;
//        }

    }

    public static final class ConcreteChildData extends ChildData {

        private final String mText;
        private long mNextChildId;
        private final boolean mIsSectionFooterPaletes;
        private final boolean mIsSectionFooterTransport;

        private long mId;
        private String mUnit;
        private Double mAmount;
        private Double mPrice;

        ConcreteChildData(long id, boolean isSectionFooterPaletes, boolean isSectionFooterTransport, String text, String unit, Double amount, Double price) {
            mText = text;
            mNextChildId = 0;
            mIsSectionFooterPaletes = isSectionFooterPaletes;
            mIsSectionFooterTransport = isSectionFooterTransport;
            mId = id;
            mUnit = unit;
            mAmount = amount;
            mPrice = price;

        }

        @Override
        public boolean isSectionFooterPaletes() {
            return mIsSectionFooterPaletes;
        }

        @Override
        public boolean isSectionFooterTransport() {
            return mIsSectionFooterTransport;
        }

        @Override
        public long getChildId() {
            return mId;
        }

        @Override
        public String getChildUnit() {
            return mUnit;
        }


        @Override
        public Double getChildAmount() {
            return mAmount;
        }

        @Override
        public Double getChildPrice() {
            return mPrice;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinned) {

        }

        @Override
        public boolean isPinned() {
            return false;
        }

        public void setChildId(long id) {
            this.mId = id;
        }

//        public long generateNewChildId() {
//            final long id = mNextChildId;
//            mNextChildId += 1;
//            return id;
//        }
    }


    private static String getOneCharString(String str, long index) {
        return Character.toString(str.charAt((int) (index % str.length())));
    }

    private static class IdGenerator {
        long mId;

        public long next() {
            final long id = mId;
            mId += 1;
            return id;
        }
    }

    private static class GroupSet {
        private ConcreteGroupData mGroup;
        private List<ConcreteChildData> mChildren;
        private IdGenerator mChildIdGenerator;

        public GroupSet(ConcreteGroupData group) {
            mGroup = group;
            mChildren = new LinkedList<>();
            mChildIdGenerator = new IdGenerator();
        }

        public void addNewChildData(int position) {
            long id = mChildIdGenerator.next();
            String text = getOneCharString(CHILD_ITEM_CHARS, id);
//            ConcreteChildData child = new ConcreteChildData(id, text);

//            mChildren.add(position, child);
        }
    }
}
