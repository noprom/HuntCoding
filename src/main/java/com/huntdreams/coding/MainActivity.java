package com.huntdreams.coding;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks{

    public static final String TAG = "MainActivity";
    NavigationDrawerFragment_ mNavigationDrawerFragment;
    String mTitle;




    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }
}
