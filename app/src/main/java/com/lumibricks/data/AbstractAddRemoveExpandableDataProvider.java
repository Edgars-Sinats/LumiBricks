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

import androidx.fragment.app.FragmentManager;

import com.lumibricks.model.Manufacture;

public abstract class AbstractAddRemoveExpandableDataProvider {

    public static abstract class BaseData {
        public abstract String getText();

        public abstract void setPinned(boolean pinned);
        public abstract boolean isPinned();
    }

    public static abstract class GroupData extends BaseData {
        public abstract boolean isSectionHeader();
        public abstract long getGroupId();
    }

    public static abstract class ChildData extends BaseData {
        public abstract boolean isSectionFooterPaletes();
        public abstract boolean isSectionFooterTransport();
        
        public abstract long getChildId();
        public abstract String getChildUnit();
        public abstract Double getChildAmount();
        public abstract Double getChildPrice();
    }

    public abstract int getGroupCount();
    public abstract int getChildCount(int groupPosition);

    public abstract GroupData getGroupItem(int groupPosition);
    public abstract ChildData getChildItem(int groupPosition, int childPosition);

    public abstract void addGroupItem(int groupPosition);
    public abstract void addChildItem(int groupPosition, int childPosition, Manufacture manufacture);

    public abstract void removeGroupItem(int groupPosition);
    public abstract void removeChildItem(int groupPosition, int childPosition);

    public abstract void moveGroupItem(int fromGroupPosition, int toGroupPosition);
    public abstract void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition);

    public abstract void clear();
    public abstract void clearChildren(int groupPosition);
}
