package com.huntdreams.coding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huntdreams.coding.common.Global;
import com.huntdreams.coding.common.network.BaseFragment;
import com.huntdreams.coding.model.AccountInfo;
import com.huntdreams.coding.model.UserObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.viewbadger.BadgeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

@EFragment(R.layout.fragment_navigation_drawer)
public class NavigationDrawerFragment extends BaseFragment {

    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private boolean mFirstDisplay = true;

    @ViewById
    TextView name;

    @ViewById
    TextView sign;

    @ViewById
    TextView follows;

    @ViewById
    TextView fans;

    final int radioIds[]={R.id.radio0,R.id.radio1,R.id.radio2,R.id.radio3,R.id.radio4};
    RadioButton radios[] = new RadioButton[radioIds.length];
    @ViewById
    CircleImageView circleIcon;

    BadgeView badgeProject;
    BadgeView badgeMessage;

    // 4.1系统bug，setHasOptionsMenu(true) 如果放在 onCreate 中会报错
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @AfterViews
    void init(){
        // 获得当前登录的用户的信息
        UserObject user = AccountInfo.loadAccount(getActivity());
        setControlContent(user);

        // 设置每一项的点击事件
        for(int i=0;i<radioIds.length;++i){
            radios[i] = (RadioButton) getView().findViewById(radioIds[i]);
            radios[i].setOnClickListener(clickItem);
        }

        radios[0].setChecked(true);
        badgeProject = (BadgeView) getView().findViewById(R.id.badge0);
        badgeProject.setVisibility(View.INVISIBLE);
        badgeMessage = (BadgeView) getView().findViewById(R.id.badge3);
        badgeMessage.setVisibility(View.INVISIBLE);

        if(mFirstDisplay){
            updateUserInfo();
        }
    }

    String HOST = Global.HOST + "/api/user/key/%s";

    // 更新个人资料
    public void updateUserInfo(){
        UserObject oldUser = AccountInfo.loadAccount(getActivity());
        getNetwork(String.format(HOST,oldUser.global_key),HOST);
    }

    // 左侧每一项点击
    View.OnClickListener clickItem = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            for(int i=0;i<radios.length;++i){
                if(v.equals(radios[i])){
                    selectItem(i);
                }else{
                    radios[i].setChecked(false);
                }
            }
        }
    };

    int mSelectMenuPos = 0;
    // 设置每一项对应的内容
    private void selectItem(int position){
        if(mDrawerLayout != null){
            mDrawerLayout.closeDrawer(mFragmentContainerView);
            mSelectMenuPos = position;
        }
    }

    private void setControlContent(UserObject user){
        name.setText(user.name);
        sign.setText(user.slogan);
        follows.setText(String.valueOf(user.follows_count));
        fans.setText(String.valueOf(user.fans_count));
        ImageLoader.getInstance().displayImage(user.avatar,circleIcon);

        // TODO 设置冒泡界面
    }

    public static interface NavigationDrawerCallbacks{
        void onNavigationDrawerItemSelected(int position);
    }
}
