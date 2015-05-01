package com.huntdreams.coding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.huntdreams.coding.common.Global;
import com.huntdreams.coding.common.LoginBackground;
import com.huntdreams.coding.common.SimpleSHA1;
import com.huntdreams.coding.common.enter.SimpleTextWatcher;
import com.huntdreams.coding.common.network.MyAsyncHttpClient;
import com.huntdreams.coding.model.UserObject;
import com.huntdreams.coding.third.FastBlur;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tencent.android.tpush.XGPushManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String TAG = "LoginActivity";

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

    private static String HOST_NEED_CAPTCHA = Global.HOST + "/api/captcha/login";

    String HOST_LOGIN = Global.HOST + "/api/login";

    public static String HOST_USER = Global.HOST + "/api/user/key/%s";

    String HOST_USER_RELOGIN = "HOST_USER_RELOGIN";

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
    void init() {
        if (background == null) {
            LoginBackground.PhotoItem photoItem = new LoginBackground(this).getPhoto();
            File file = photoItem.getCacheFile(this);
            if (file.exists()) {
                background = Uri.fromFile(file);
            }
        }

        try { // TODO 图片载入可能失败，因为图片没有下载完
            BitmapDrawable bitmapDrawable;
            if (background == null) {
                bitmapDrawable = createBlur();
            } else {
                bitmapDrawable = createBlur(background);
            }
            backgroundImage.setImageDrawable(bitmapDrawable);
        } catch (Exception e) {}

        needCaptcha();

        editName.addTextChangedListener(textWatcher);
        editPassword.addTextChangedListener(textWatcher);
        editValify.addTextChangedListener(textWatcher);
        upateLoginButton();

        editName.addTextChangedListener(textWatcherName);
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

    @Click
    void imageValify(){
        editValify.requestFocus();
        downloadValifyPhoto();
    }

    private void needCaptcha() {
        getNetwork(HOST_NEED_CAPTCHA, HOST_NEED_CAPTCHA);
    }

    private void downloadValifyPhoto(){
        String host = Global.HOST + "/api/getCaptcha";
        AsyncHttpClient client = MyAsyncHttpClient.createClient(this);

        client.get(host,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                imageValify.setImageBitmap(BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length));
                editValify.setText("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showMiddleToast("获取验证码失败");
            }
        });
    }

    @Click
    protected final void loginButton(){
        try{
            String name = editName.getText().toString();
            String password = editPassword.getText().toString();
            String captcha = editValify.getText().toString();

            if(name.isEmpty()){
                showMiddleToast("邮箱或个性后缀不能为空");
                return;
            }

            if (password.isEmpty()) {
                showMiddleToast("密码不能为空");
                return;
            }

            RequestParams params = new RequestParams();
            params.put("email",name);
            params.put("password", SimpleSHA1.sha1(password));
            if(captchaLayout.getVisibility() == View.VISIBLE){
                params.put("j_captcha",captcha);
            }
            params.put("remember_me",true);

            postNetwork(HOST_LOGIN, params, HOST_LOGIN);
            showProgressBar(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void parseJson(int code, JSONObject respanse, String tag, int pos, Object data) throws JSONException {
        super.parseJson(code, respanse, tag, pos, data);
        if(tag.equals(HOST_LOGIN)){
            if(code == 0){
                UserObject user = new UserObject(respanse.getJSONObject("data"));
                getNetwork(String.format(HOST_USER, user.global_key), HOST_USER);
                showProgressBar(true);
            }else{
                String msg = Global.getErrorMsg(respanse);
                showMiddleToast(msg);
                needCaptcha();
                showProgressBar(false);
            }
        }
    }

    TextWatcher textWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            upateLoginButton();
        }
    };

    TextWatcher textWatcherName = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            userIcon.setImageResource(R.drawable.icon_user_monkey);
//            userIcon.setBackgroundResource(R.drawable.icon_user_monkey);
        }
    };

    private void upateLoginButton(){
        if(editName.getText().length() == 0){
            loginButton.setEnabled(false);
            return;
        }

        if(editPassword.getText().length() == 0){
            loginButton.setEnabled(false);
            return;
        }

        if (captchaLayout.getVisibility() == View.VISIBLE &&
                editValify.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }

        loginButton.setEnabled(true);
    }
}
