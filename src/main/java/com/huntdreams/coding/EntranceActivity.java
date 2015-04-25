package com.huntdreams.coding;

import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntdreams.coding.common.Global;
import com.huntdreams.coding.common.LoginBackground;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

/**
 * 项目入口文件
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/24.
 */
@EActivity(R.layout.entrance_image)
public class EntranceActivity extends BaseActivity {

    @ViewById
    ImageView image;

    @ViewById
    TextView title;

    @ViewById
    View mask;

    @ViewById
    View logo;

    @AnimationRes
    Animation entrance;

    Uri background = null;

    static final String HOST_CURRENT = Global.HOST + "/api/current_user";

    boolean mNeedUpdateUser = false;

    @AfterViews
    void init(){
        LoginBackground.PhotoItem photoItem = new LoginBackground(this).getPhoto();
    }
}
