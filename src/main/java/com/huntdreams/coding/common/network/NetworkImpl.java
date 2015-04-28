package com.huntdreams.coding.common.network;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 封装好的网络操作类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/28.
 */
public class NetworkImpl {
    public static final String TAG = "NetworkImpl";

    public static final int NETWORK_ERROR = -1;
    public static final int NEWWORK_ERROR_SERVICE = -2;
    private final NetworkCallback callback;

    public HashMap<String,PageInfo> mPages = new HashMap<String,PageInfo>();
    private HashMap<String,Boolean> mUpdateing = new HashMap<String,Boolean>();

    Context appContext;

    public enum Request{
        Get,Post,Put,Delete
    }

    public NetworkImpl(Context ctx,NetworkCallback callback) {
        this.appContext = ctx;
        this.callback = callback;
    }

    /**
     * 是否需要刷新所有数据
     * @param tag
     * @return
     */
    public boolean isLoadingFirstPage(String tag){
        PageInfo info = mPages.get(tag);
        return info == null || info.isNewRequest;
    }

    /**
     * 加载数量
     * @param url
     * @param params
     * @param tag
     * @param dataPos
     * @param data
     * @param type
     */
    public void loadData(String url ,RequestParams params,final String tag,final int dataPos,final Object data,Request type){
        Log.d(TAG,"loadData:url = "+url+",type = "+type+",tag = "+tag);

        if (mUpdateing.containsKey(tag) && mUpdateing.get(tag).booleanValue()) {
            Log.d("", "url#" + (params == null ? "get " : "post ") + url);
            return;
        }

        mUpdateing.put(tag,true);

        AsyncHttpClient client = MyAsyncHttpClient.createClient(appContext);

        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        };
    }
}
