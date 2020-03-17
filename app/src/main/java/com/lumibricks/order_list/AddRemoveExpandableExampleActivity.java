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
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lumibricks.FilterDialogFragment;
import com.lumibricks.R;
import com.lumibricks.data.AbstractAddRemoveExpandableDataProvider;
import com.lumibricks.fragment.ExampleAddRemoveExpandableDataProviderFragment;


public class AddRemoveExpandableExampleActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_LIST_VIEW = "list view";
    private BottomNavigationView mToolbar;
//    public FilterDialogFragment mDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mToolbar = findViewById(R.id.toolbar);



        if (savedInstanceState == null) {
//            createBrickItemInDialogFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(new ExampleAddRemoveExpandableDataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddRemoveExpandableExampleFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }

        mToolbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d("AddRemoveEEActivity", "onMenuItemClick: " + menuItem);
                return false;
            }
        });


    }

//    public  FilterDialogFragment createBrickItemInDialogFragment(){
//        FilterDialogFragment mDialogFragment;
//
//        mDialogFragment = new FilterDialogFragment();
//        Bundle bundle = new Bundle();
//        Log.d("TAG", "onManufactureClicked: So far so good.");
//        bundle.putString("buttonPressed", "brick_navigation_order");
//        mDialogFragment.setArguments(bundle);
//        mDialogFragment.show((AddRemoveExpandableExampleActivity.this).getSupportFragmentManager(), FilterDialogFragment.TAG);
//        return null;
//    }

    public AbstractAddRemoveExpandableDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((ExampleAddRemoveExpandableDataProviderFragment) fragment).getDataProvider();
    }
}
