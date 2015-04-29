package com.huntdreams.coding.common.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huntdreams.coding.LoginActivity_;
import com.huntdreams.coding.common.Global;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
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
    public static final int NETWORK_ERROR = -1;
    public static final int NETWORK_ERROR_SERVICE = -2;
    private final NetworkCallback callback;

    public HashMap<String, PageInfo> mPages = new HashMap<String, PageInfo>();
    private HashMap<String, Boolean> mUpdateing = new HashMap<String, Boolean>();

    Context appContext;

    public enum Request {
        Get, Post, Put, Delete
    }

    public NetworkImpl(Context ctx, NetworkCallback networkCallback) {
        this.appContext = ctx;
        this.callback = networkCallback;
    }

    // 是否需要刷新所有数据
    public boolean isLoadingFirstPage(String tag) {
        PageInfo info = mPages.get(tag);
        return info == null || info.isNewRequest;
    }

    public void loadData(String url, RequestParams params, final String tag, final int dataPos, final Object data, Request type) {
        Log.d("", "url " + type + " " + url);

        if (mUpdateing.containsKey(tag) && mUpdateing.get(tag).booleanValue()) {
            Log.d("", "url#" + (params == null ? "get " : "post ") + url);
            return;
        }

        mUpdateing.put(tag, true);

        AsyncHttpClient client = MyAsyncHttpClient.createClient(appContext);

        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    int code = response.getInt("code");

                    if (code == 1000) {
                        appContext.startActivity(new Intent(appContext, LoginActivity_.class));
                    }

                    try {
                        updatePage(response, tag);
                    } catch (Exception e) {
                    }
                    callback.parseJson(code, response, tag, dataPos, data);

                    try {
                        updateRequest(response, tag);
                    } catch (Exception e) {

                    }

                } catch (Exception e) {
                    Global.errorLog(e);
                }
                mUpdateing.put(tag, false);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    int translateStatusCode = translateErrorCode(statusCode);
                    if (errorResponse == null) {
                        errorResponse = makeErrorJson(statusCode);
                    }
                    callback.parseJson(translateStatusCode, errorResponse, tag, dataPos, data);
                    if (isPageRequest(tag)) {
//                        callback.setPageBottom(NetworkCallback.PageStyle.LoadingFail);
                    }

                } catch (Exception e) {
                    Global.errorLog(e);
                }
                mUpdateing.put(tag, false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                try {
                    int translateErrorCode = translateErrorCode(statusCode);
                    JSONObject json = makeErrorJson(statusCode);

                    callback.parseJson(translateErrorCode, json, tag, dataPos, data);
                    if (isPageRequest(tag)) {
//                        callback.setPageBottom(NetworkCallback.PageStyle.LoadingFail);
                    }

                } catch (Exception e) {
                    Global.errorLog(e);
                }
                mUpdateing.put(tag, false);
            }

            private int translateErrorCode(int statusCode) {
                if (statusCode == 0) { // 我这里的设计有问题，statusCode为0的时候表示网络不通，而code==0又表示请求成功，parseJson函数的第一个参数是以0来表示成功的
                    statusCode = NETWORK_ERROR;
                } else {
                    statusCode = NETWORK_ERROR_SERVICE;
                }

                return statusCode;
            }

            private JSONObject makeErrorJson(int statusCode) {
                JSONObject json = new JSONObject();
                try {
                    JSONObject jsonErrorMsg = new JSONObject();
                    jsonErrorMsg.put("msg", "服务器内部错误，有人要扣奖金了");

                    json.put("code", statusCode);
                    json.put("msg", jsonErrorMsg);
                } catch (Exception e) {
                }

                return json;
            }

            @Override
            public void onFinish() {
            }
        };

        switch (type) {
            case Get:
                client.get(url, jsonHttpResponseHandler);
                break;

            case Post:
                client.post(url, params, jsonHttpResponseHandler);
                break;

            case Put:
                client.put(url, params, jsonHttpResponseHandler);
                break;

            case Delete:
                client.delete(url, jsonHttpResponseHandler);
                break;
        }
    }

    public void initSetting() {
        mPages = new HashMap<String, PageInfo>();
    }

    private boolean isPageRequest(String tag) {
        return mPages.containsKey(tag);
    }

    private void updatePage(JSONObject json, final String tag) throws JSONException {
        if (!isPageRequest(tag)) {
            return;
        }

        PageInfo pageInfo = mPages.get(tag);
        if (json.has("data")) {
            json = json.getJSONObject("data");
            if (json.has("totalPage")) {
                pageInfo.pageAll = json.getInt("totalPage");
                pageInfo.pageIndex = json.getInt("page");
            } else if (json.has("page")) {
                pageInfo.pageIndex = json.getInt("page");
                pageInfo.pageAll = json.getInt("pageSize");
            } else {
                pageInfo.pageIndex = 0;
                pageInfo.pageAll = 0;
            }
        } else {
            pageInfo.pageIndex = 0;
            pageInfo.pageAll = 0;
        }

//        if (pageInfo.isLoadingLastPage()) {
//            callback.setPageBottom(NetworkCallback.PageStyle.NoData);
//        } else {
//            callback.setPageBottom(NetworkCallback.PageStyle.Loading);
//        }
    }


    private void updateRequest(JSONObject json, final String tag) throws JSONException {
        if (!isPageRequest(tag)) {
            return;
        }

        PageInfo pageInfo = mPages.get(tag);
        pageInfo.isNewRequest = false;
    }

    public boolean isLoadingLastPage(String tag) {
        PageInfo pageInfo = mPages.get(tag);
        return pageInfo != null && pageInfo.isLoadingLastPage();
    }

    public void getNextPageNetwork(String url, final String tag) {
        PageInfo pageInfo = mPages.get(tag);
        if (pageInfo == null) {
            pageInfo = new PageInfo();
            mPages.put(tag, pageInfo);
        }

        if (pageInfo.isLoadingLastPage()) {
//            callback.setPageBottom(NetworkCallback.PageStyle.NoData);
            return;
        }

        String pageUrl = url + "&page=" + (pageInfo.pageIndex + 1);
        callback.getNetwork(pageUrl, tag);
    }

}
