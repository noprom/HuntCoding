package com.huntdreams.coding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huntdreams.coding.common.Global;
import com.huntdreams.coding.common.LoginBackground;
import com.huntdreams.coding.common.UnreadNotify;
import com.huntdreams.coding.model.AccountInfo;
import com.huntdreams.coding.model.UserObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
        File file = photoItem.getCacheFile(this);
        if(file.exists()){
            background = Uri.fromFile(file);
            image.setImageURI(background);
            title.setText(photoItem.getTitle());
            if(photoItem.isGuoguo())
                hideLogo();
        }

        entrance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!mNeedUpdateUser) {
                    next();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        image.startAnimation(entrance);


        if (MyApp.sUserObject.global_key.isEmpty() && AccountInfo.isLogin(this)) {
            getNetwork(HOST_CURRENT, HOST_CURRENT);
            mNeedUpdateUser = true;
        }
    }

    private void hideLogo() {
        mask.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
    }

    @Override
    public void parseJson(int code, JSONObject respanse, String tag, int pos, Object data) throws JSONException {
        if (tag.equals(HOST_CURRENT)) {
            mNeedUpdateUser = false;
            if (code == 0) {
                UserObject user = new UserObject(respanse.getJSONObject("data"));
                AccountInfo.saveAccount(this, user);
                MyApp.sUserObject = user;
                AccountInfo.saveReloginInfo(this, user.email, user.global_key);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("更新")
                        .setMessage("刷新账户信息失败")
                        .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getNetwork(HOST_CURRENT, HOST_CURRENT);
                            }
                        })
                        .setNegativeButton("关闭程序", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
                dialogTitleLineColor(dialog);

            }
        }
    }

    void next() {
        Toast.makeText(this,"登录啊",Toast.LENGTH_LONG).show();
        Intent intent;
        String mGlobalKey = AccountInfo.loadAccount(this).global_key;
        if (mGlobalKey.isEmpty()) {
            intent = new Intent(this, LoginActivity_.class);
            if (background != null) {
                intent.putExtra("background", background);
            }
        } else {
            intent = new Intent(this, LoginActivity_.class);
        }

        startActivity(intent);

        UnreadNotify.update(this);
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }
}