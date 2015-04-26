package com.huntdreams.coding;

import android.app.Dialog;

import com.huntdreams.coding.common.CustomDialog;
import com.huntdreams.coding.common.network.NetworkCallback;
import com.huntdreams.coding.common.umeng.UmengActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity基类
 * 封装了图片下载并缓存
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/22.
 */
public class BaseActivity extends UmengActivity implements NetworkCallback{
    @Override
    public void parseJson(int code, JSONObject respanse, String tag, int pos, Object data) throws JSONException {

    }

    @Override
    public void getNetwork(String uri, String tag) {

    }

    public final void dialogTitleLineColor(Dialog dialog) {
        CustomDialog.dialogTitleLineColor(this, dialog);
    }
}
