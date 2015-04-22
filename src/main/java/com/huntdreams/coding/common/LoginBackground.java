package com.huntdreams.coding.common;

import android.content.Context;
import android.util.Log;

import com.huntdreams.coding.common.network.MyAsyncHttpClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;

/**
 * 登陆背景
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/24.
 */
public class LoginBackground {

    private static final String TAG = "LoginBackground";

    private Context context;

    final String URL_DOWNLOAD = Global.HOST + "/api/wallpaper/wallpapers?type=4";

    public LoginBackground(Context context) {
        this.context = context;
    }

    public void update(){
        if (!Global.isWifiConnected(context)) {
            return;
        }

        if(needUpdate()){
            AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
            client.get(URL_DOWNLOAD,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG,"get detail in onSuccess()");
                    if(response.optInt("code",-1) == 0){

                    }
                }
            });
        }



    }

    private boolean needUpdate(){
        return true;
    }

    /**
     * PhotoItem 类
     */
    public static class PhotoItem implements Serializable{
        Group mGroup = new Group();
        String url = "";

        public PhotoItem(JSONObject json){
            mGroup = new Group(json.optJSONObject("group"));
            url = json.optString("url");
        }

        public PhotoItem(){
        }

        class Group implements Serializable{
            String name = "";
            String author = "";
            String link = "";
            String description = "";
            int id;

            Group(JSONObject json){
                name = json.optString("name");
                author = json.optString("author");
                link = json.optString("link");
                description = json.optString("description");
                id = json.optInt("id");
            }

            Group(){}
        }

        public String getUrl() {
            return url;
        }

        // 表明不要显示其它的控件，只显示图片
        public boolean isGuoguo() {
            return mGroup.author.equals("guoguo");
        }

        public String getTitle() {
            return String.format("%s © %s", mGroup.name, mGroup.author);
        }

        private String getCacheName() {
            return String.valueOf(mGroup.id);
        }

        public File getCacheFile(Context ctx) {
            File file = new File(getPhotoDir(ctx), getCacheName());
            return file;
        }

        public boolean isCached(Context ctx) {
            return getCacheFile(ctx).exists();
        }

        private File getPhotoDir(Context ctx) {
            final String dirName = "BACKGROUND";
            File root = ctx.getExternalFilesDir(null);
            File dir = new File(root, dirName);
            if (!dir.exists() || dir.isDirectory()) {
                dir.mkdir();
            }

            return dir;
        }

    }
}
