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


import android.content.Context;
import android.util.Log;

import androidx.core.util.Pair;

import com.lumibricks.FilterDialogFragment;
import com.lumibricks.db.BrickDbHelper;
import com.lumibricks.model.Brick;
import com.lumibricks.model.BrickOrder;
import com.lumibricks.model.Manufacture;
import com.lumibricks.model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExampleAddRemoveExpandableDataProvider extends AbstractAddRemoveExpandableDataProvider {
    public static Manufacture ChildData;
    private List<Pair<GroupData, List<ChildData>>> mData;
    private Pair<GroupData, List<ChildData>> mLastRemovedData;
    private int mLastRemovedPosition = -1;
    BrickDbHelper dbHelper = BrickDbHelper.getInstance(this);


    // for undo group item
    private Pair<GroupData, List<ChildData>> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    // for undo child item
    private ChildData mLastRemovedChild;
    private int mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

//  private FilterDialogFragment mDialogFragment;

    private static String GROUP_ITEM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String CHILD_ITEM_CHARS = "abcdefghijklmnopqrstuvwxyz";

//    private List<GroupSet> mData;
//    mData = new LinkedList<>();
    private IdGenerator mGroupIdGenerator;
    private IdGenerator mChildIdGenerator;

    //Test doc function
    public ExampleAddRemoveExpandableDataProvider() {
        Brick brick = new Brick();
        BrickOrder brickOrder = new BrickOrder();

        mGroupIdGenerator = new IdGenerator();
        mChildIdGenerator= new IdGenerator();

        //|GHI|JKL|MNO|PQR|STU|VWX|YZ
        final String groupItems = "|_|C_|DEF_";
        final String childItems = "es";

        mData = new LinkedList<>();
//        mGroupIdGenerator = new IdGenerator();
        int sectionCount = 1;

        for (int i = 0; i < groupItems.length(); i++) {
            //noinspection UnnecessaryLocalVariable
//            final long groupId = i;
            final int groupId = mGroupIdGenerator.next();
            final boolean isSection = (groupItems.charAt(i) == '|');
            final boolean isSectionFooter = (groupItems.charAt(i) == '_');

            //Group txt => section txt(groupHeader)
//            final String groupText = isSection ? ("Peron " + sectionCount) : Character.toString(groupItems.charAt(i));
            final String groupText;
            if (isSection){
                groupText = ("Peron " + sectionCount);
            } else if (isSectionFooter){
                groupText = ("Numbers " + sectionCount);
            } else {
                groupText =  Character.toString(groupItems.charAt(i));
            }

            final ConcreteGroupData group = new ConcreteGroupData(groupId, isSection, isSectionFooter, 0.0, groupText, 0.0, false);
            final List<ChildData> children = new ArrayList<>();

            if (isSection) {
                sectionCount += 1;
            } else {

                // add child items
                for (int j = 0; j < childItems.length() ; j++) {
//                    ConcreteChildData childData = new ConcreteChildData();
                    final long childId = mChildIdGenerator.next();

                    final String childText = Character.toString(childItems.charAt(j));
                    children.add(new ConcreteChildData(childId, false, false, "Piemērs: "+childText, "m", 30.0, 3.3, 0.0, false));
                }

                // add child footer items
                for (int j = 0; j < 2; j++) {
                    final long childId = mChildIdGenerator.next();
                    if (j==0){
                        final String childText = "Transports";
                        children.add(new ConcreteChildData(childId, false, true, childText, "KM", 1.0, 00.0, 0.0, false));
                    } else{
                        final String childText = "Palettes";
                        children.add(new ConcreteChildData(childId, true, false, childText, "GB", 1.0, 2.5, 0.0, false));
                    }
                }

                //TODO add footSection item
            }

            mData.add(new Pair<GroupData, List<ChildData>>(group, children));

        }

    }

    @Override
    public Context getContext() {
        return null;
    }

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
    public Double getChildPriceSum(int start, int end) {
        int size = 0;
        if (mData.get(start).second == null || mData.get(end).second == null ) {
            throw new NullPointerException("getChildPriceSum == null");
        }
        Double totalPrice = 0.0;

        for (int adressOrder = start; adressOrder <= end; adressOrder++){

            size = mData.get(adressOrder).second.size();

            for(int i=0; i<size; i++){
                Double amount = mData.get(adressOrder).second.get(i).getChildAmount();
                Double price = mData.get(adressOrder).second.get(i).getChildPrice();
                Double brickTotalPrice = amount*price;
                totalPrice = totalPrice+brickTotalPrice;
            }
            mData.get(adressOrder).first.setGroupPrice(totalPrice);
        }
        return totalPrice;

    }

    @Override
    public Double getChildPalettesSum(int start, int end) {
        int size = 0;
        if (mData.get(start).second == null || mData.get(end).second == null ) {
            throw new NullPointerException("getChildPriceSum == null");
        }
        Double totalPalletes = 0.0;

        for (int adressOrder = start; adressOrder <= end; adressOrder++){

            size = mData.get(adressOrder).second.size();

            for(int i=0; i<size; i++){
                Double palettes = mData.get(adressOrder).second.get(i).getPalletes();

                Double brickTotalPalettes = palettes;
                totalPalletes = totalPalletes+brickTotalPalettes;
            }
            mData.get(adressOrder).first.setPalletes(totalPalletes);
        }
        return totalPalletes;
    }


    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
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
    public long undoLastRemoval() {
        int insertedPosition;

        if (mLastRemovedGroup != null){

            if (mLastRemovedGroupPosition >= 0 && mLastRemovedGroupPosition < mData.size()) {
                insertedPosition = mLastRemovedGroupPosition;
            } else {
                insertedPosition = mData.size();
            }

            mData.add(insertedPosition, mLastRemovedData);

            mLastRemovedGroup = null;
            mLastRemovedGroupPosition = -1;

            return insertedPosition;

        }else if(mLastRemovedChild != null){
            if (mLastRemovedChildPosition >= 0 && mLastRemovedChildPosition < mData.get(mLastRemovedChildParentGroupId).second.size()){
                insertedPosition = mLastRemovedChildPosition;
            } else {
                insertedPosition = mData.get(mLastRemovedChildParentGroupId).second.size();
            }
            mData.get(mLastRemovedChildParentGroupId).second.add(insertedPosition, mLastRemovedChild);

            mLastRemovedChild = null;
            mLastRemovedChildPosition = -1;
            mLastRemovedChildParentGroupId = -1;

            return insertedPosition;

        } else {
            return -1;
        }
    }

    @Override
    public void removeGroupItem(int groupPosition) {
        mLastRemovedData = mData.remove(groupPosition);
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


        //Might not be needed, becouse evhery item has their own ID...(just pass new old id)
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
        int newGroupId = getGroupCount() +1;
        String text = getOneCharString(GROUP_ITEM_CHARS, newGroupId);

        final ConcreteGroupData group = new ConcreteGroupData(newGroupId, false, false, 0.0, text, 0.0, false);
        mGroupIdGenerator.next();
        final List<ChildData> children = new ArrayList<>();

        // add child footer items
        for (int j = 0; j < 2; j++) {
            final long childId = mChildIdGenerator.next();
            if (j==0){
                final String childText = "Palettes";
                children.add(new ConcreteChildData(childId, true, false, childText, "gb", 1.0, 2.5, 0.0, false));
            } else{
                final String childText = "Transports";
                children.add(new ConcreteChildData(childId, false, true, childText, "km", 0.0, 1.1, 0.0, false));
            }
        }
        mData.add(new Pair<GroupData, List<ChildData>>(group, children));

        int size = mData.size();
        final Pair<GroupData, List<ChildData>> item = mData.remove(size-1);
        mData.add(groupPosition, item);

    }

    @Override
    public void addChildItem(int groupPosition, int childPosition, BrickOrder brickOrder) {
        final Pair<GroupData, List<ChildData>> toGroup = mData.get(groupPosition);

        if (toGroup.first.isSectionHeader()) {
            throw new IllegalStateException("Destination group is a section header!");
        }
        final String childItems = "abc";
        final List<ChildData> children = new ArrayList<>();
//        final long childId = ((ConcreteGroupData) toGroup.first).generateNewChildId();
//        final String childText = Character.toString(childItems.charAt(1));

        final long childId = mChildIdGenerator.next();
        final String childText = brickOrder.getName();//BRICK name_color_lumi
        final Double childAmount = brickOrder.getOrginAmount();
        final double childPrice = brickOrder.getSellPrice();
        final double childPalletes = brickOrder.getPalletes();
        final String childUnit = brickOrder.getInputSellType();

        final ConcreteChildData item = new ConcreteChildData(childId, false, false, childText, childUnit, childAmount, childPrice, childPalletes, false);
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
        private final int mId;
        private final boolean mIsSectionHeader;
        private final boolean mIsSectionFooter;
        private Double mgroupPrice;
        private String mText;
        private Double mPalletes;
        private Boolean mIsPinned;
//        private long mNextChildId;


        ConcreteGroupData(int id, boolean isSectionHeader, boolean isSectionFooter, Double groupPrice, String text, Double palletes, Boolean isPinned) {
            mId = id;
            mText = text;
            mIsSectionHeader = isSectionHeader;
            mIsSectionFooter = isSectionFooter;
            mgroupPrice = groupPrice;
            mPalletes = palletes;
            mIsPinned = isPinned;
        }

        @Override
        public boolean isSectionHeader() {
            return mIsSectionHeader;
        }

        @Override
        public boolean isSectionFooter() {
            return mIsSectionFooter;
        }

        @Override
        public int getGroupId() {
            return mId;
        }

        @Override
        public Double getGroupPrice() {
            return mgroupPrice;
        }

        @Override
        public void setGroupPrice(Double price) {
            mgroupPrice = price;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setText(String text) {
            mText = text;
        }

        @Override
        public Double getPalletes() {
            return mPalletes;
        }

        @Override
        public void setPalletes(Double palletes) {
            mPalletes = palletes;

        }

        @Override
        public void setPinned(boolean pinned) {
            mIsPinned = pinned;
        }

        @Override
        public boolean isPinned() {
            return mIsPinned;
        }

//        public long generateNewChildId() {
//            final long id = mNextChildId;
//            mNextChildId += 1;
//            return id;
//        }

    }

    public static final class ConcreteChildData extends ChildData {

        private String mText;
        private long mNextChildId;
        private final boolean mIsSectionFooterPaletes;
        private final boolean mIsSectionFooterTransport;
        private boolean mIsPinned;

        private long mId;
        private String mUnit;
        private Double mAmount;
        private Double mPrice;
        private Double mPalletes;

        ConcreteChildData(long id, boolean isSectionFooterPaletes, boolean isSectionFooterTransport, String text, String unit, Double amount, Double price, Double palletes, boolean isPinned) {
            mText = text;
            mNextChildId = 0;
            mIsSectionFooterPaletes = isSectionFooterPaletes;
            mIsSectionFooterTransport = isSectionFooterTransport;
            mId = id;
            mUnit = unit;
            mAmount = amount;
            mPrice = price;
            mPalletes = palletes;
            mIsPinned = isPinned;
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
        public String changeUnit(String oldChildUnit) {

            String[] fullName;
            fullName = mText.split("_");
            String brickName;
            brickName = fullName[0];

            if (oldChildUnit.equals("M2")){
                return "GB";
            } else if(oldChildUnit.equals("GB")){
                return "M2";
            } else {
                return oldChildUnit;
            }
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
        public void setText(String text) {
            mText = text;
        }

        @Override
        public Double getPalletes() {
            return mPalletes;
        }

        @Override
        public void setPalletes(Double palletes) {
            mPalletes = palletes;
        }

        @Override
        public void setPinned(boolean pinned) {
            mIsPinned = pinned;

        }

        @Override
        public boolean isPinned() {
            return mIsPinned;
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
        int mId;

        public int next() {
            final int id = mId;
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
