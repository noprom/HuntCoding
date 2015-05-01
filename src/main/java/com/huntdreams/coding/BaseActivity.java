package com.huntdreams.coding;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.huntdreams.coding.common.CustomDialog;
import com.huntdreams.coding.common.network.NetworkCallback;
import com.huntdreams.coding.common.network.NetworkImpl;
import com.huntdreams.coding.common.umeng.UmengActivity;
import com.loopj.android.http.RequestParams;

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

    protected LayoutInflater mInflater;

    private NetworkImpl networkImpl;

    private ProgressDialog mProgressDialog;

    protected void showProgressBar(boolean show){
        showProgressBar(show,"");
    }

    protected void showProgressBar(boolean show,String message){
        if(show){
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }else{
            mProgressDialog.hide();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkImpl = new NetworkImpl(this, this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        mInflater = getLayoutInflater();
//        initSetting();

//        UnreadNotify.update(this);
    }


    @Override
    public void parseJson(int code, JSONObject respanse, String tag, int pos, Object data) throws JSONException {

    }

    protected void postNetwork(String url, RequestParams params, final String tag) {
        networkImpl.loadData(url, params, tag, -1, null, NetworkImpl.Request.Post);
    }

    @Override
    public void getNetwork(String url, final String tag) {
        networkImpl.loadData(url, null, tag, -1, null, NetworkImpl.Request.Get);
    }

    public final void dialogTitleLineColor(Dialog dialog) {
        CustomDialog.dialogTitleLineColor(this, dialog);
    }

    protected void showButtomToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showButtomToast(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }

    protected void showMiddleToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
