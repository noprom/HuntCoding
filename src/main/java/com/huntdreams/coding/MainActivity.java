package com.huntdreams.coding;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks{

    public static final String TAG = "MainActivity";
    NavigationDrawerFragment_ mNavigationDrawerFragment;
    String mTitle;

    @Extra
    String mPushUrl;

    @ViewById
    ViewGroup drawer_layout;

    boolean mFirstEnter = true;
    private View actionbarCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }


}
