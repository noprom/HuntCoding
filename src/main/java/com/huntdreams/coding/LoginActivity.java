package com.huntdreams.coding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.huntdreams.coding.common.Global;
import com.huntdreams.coding.common.LoginBackground;
import com.huntdreams.coding.third.FastBlur;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tencent.android.tpush.XGPushManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;

/**
 * 用户登录界面
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/26.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @Extra
    Uri background;

    @ViewById
    ImageView userIcon;

    @ViewById
    ImageView backgroundImage;

    @ViewById
    EditText editName;

    @ViewById
    EditText editPassword;

    @ViewById
    ImageView imageValify;

    @ViewById
    EditText editValify;

    @ViewById
    View captchaLayout;

    @ViewById
    View loginButton;

    final float radius = 8;
    final double scaleFactor = 16;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.icon_user_monkey)
            .showImageOnFail(R.drawable.icon_user_monkey)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(300))
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 调用下，防止收到上次登陆账号的通知
        XGPushManager.registerPush(this, "*");
    }

    @AfterViews
    void init(){
        if(background == null){
            LoginBackground.PhotoItem photoItem = new LoginBackground(this).getPhoto();
            File file = photoItem.getCacheFile(this);
            if(file.exists()){
                background = Uri.fromFile(file);
            }
        }

        try{
            BitmapDrawable bitmapDrawable;
            if(background == null){
                bitmapDrawable = createBlur();
            }else{
                bitmapDrawable = createBlur(background);
            }
            backgroundImage.setImageDrawable(bitmapDrawable);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private BitmapDrawable createBlur(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.entrance1, options);
        int height = options.outHeight;
        int width = options.outWidth;

        options.outHeight = (int) (height / scaleFactor);
        options.outWidth = (int) (width / scaleFactor);
        options.inSampleSize = (int) (scaleFactor + 0.5);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.entrance1, options);
        Bitmap blurBitmap = FastBlur.doBlur(bitmap, (int) radius, true);

        return new BitmapDrawable(getResources(), blurBitmap);
    }

    private BitmapDrawable createBlur(Uri uri) {
        String path = Global.getPath(this, uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;

        options.outHeight = (int) (height / scaleFactor);
        options.outWidth = (int) (width / scaleFactor);
        options.inSampleSize = (int) (scaleFactor + 0.5);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        Bitmap blurBitmap = FastBlur.doBlur(bitmap, (int) radius, true);

        return new BitmapDrawable(getResources(), blurBitmap);
    }
}
