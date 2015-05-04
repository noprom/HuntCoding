package com.huntdreams.coding.common;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.huntdreams.coding.MyApp;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 全局配置文件
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/24.
 */
public class Global {
    private static final String HOST_CODING = "https://coding.net";

    public static String HOST = HOST_CODING;

    public static SimpleDateFormat DateFormatTime = new SimpleDateFormat("HH:mm");

    private static SimpleDateFormat DayFormatTime = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat MonthDayFormatTime = new SimpleDateFormat("MMMdd日");

    public static SimpleDateFormat WeekFormatTime = new SimpleDateFormat("EEE");
    public static SimpleDateFormat NextWeekFormatTime = new SimpleDateFormat("下EEE");
    public static SimpleDateFormat LastWeekFormatTime = new SimpleDateFormat("上EEE");

    public static String dayFromTime(long time) {
        return DayFormatTime.format(time);
    }

    public static long longFromDay(String day) throws ParseException {
        final String format = "yyyy-MM-dd";
        final SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.parse(day).getTime();
    }

    public static String dayCount(long time) {
        return DayFormatTime.format(time);
    }

    public static void errorLog(Exception e) {
        e.printStackTrace();
        Log.e("", "" + e);
    }

    public static String encodeInput(String at, String input) {
        if (at == null || at.isEmpty()) {
            return input;
        }

        return String.format("@%s %s", at, input);
    }

    public static String encodeUtf8(String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (Exception e) {
        }

        return "";
    }

    /**
     * 是否wifi连接
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context){
        if(context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null &&networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    /**
     * 是否已经联网
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        if(context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 同步cookie
     * @param context
     */
    public static void syncCookie(Context context){
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        List<Cookie> cookies = cookieStore.getCookies();

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();

        for(int i=0,size = cookies.size();i < size;++i){
            Cookie eachCookie = cookies.get(i);
            String cookieString  = eachCookie.getName()+"="+eachCookie.getValue();
            cookieManager.setCookie(Global.HOST,cookieString);
        }

        CookieSyncManager.getInstance().sync();
    }

    public static String makeLargeUrl(String url) {
        final int MAX = 4096; // ImageView显示的图片不能大于这个数
        return String.format(IMAGE_URL_SCAL, url, 4096, 4096);
    }

    private static String intToString(int length) {
        String width;
        if (length > 0) {
            width = String.valueOf(length);
        } else {
            width = "";
        }

        return width;
    }

    public static int dpToPx(int dpValue) {
        return (int) (dpValue * MyApp.sScale + 0.5f);
    }

    public static int pxToDp(float pxValue) {
        return (int) (pxValue / MyApp.sScale + 0.5f);
    }

    private static final String IMAGE_URL_SCAL = "%s?imageMogr2/thumbnail/!%s";

    public static String makeSmallUrl(ImageView view, String url) {
        String realUrl = url.split("\\?")[0];

        if (url.indexOf("http") == 0) {
            // 头像再裁剪需要算坐标，就不改参数了
            // https://dn-coding-net-production-static.qbox.me/c28b97dd-61f2-41d4-bd7e-b04f0c634751.jpg?imageMogr2/auto-orient/format/jpeg/crop/!164x164a568a38
            if (url.contains("/crop/")) {
                return url;
            }

            ViewGroup.LayoutParams lp = view.getLayoutParams();
            String width = intToString(lp.width);
            String height = intToString(lp.height);

            // 如果初始化的时候没有长宽，默认取高度为200dp缩略图
            if (width.isEmpty() && height.isEmpty()) {
                height = String.valueOf(Global.dpToPx(200));
                width = String.valueOf(Global.dpToPx(200));

            }
            return String.format(IMAGE_URL_SCAL, realUrl, width);
        } else {
            return realUrl;
        }
    }

    /**
     * 替换头像
     * @param json
     * @return
     */
    public static String replaceAvatar(JSONObject json) {
        return replaceUrl(json, "avatar");
    }

    /**
     * 获得错误信息
     * @param jsonObject
     * @return
     */
    public static String getErrorMsg(JSONObject jsonObject) {
        String s = "";
        try {
            JSONObject jsonData = jsonObject.getJSONObject("msg");
            String key = (String) jsonData.keys().next();
            s = jsonData.getString(key);
        } catch (Exception e) {
        }

        return s;
    }

    /**
     * 替换URL
     * @param json
     * @param name
     * @return
     */
    public static String replaceUrl(JSONObject json, String name) {
        String s = json.optString(name);
        if (s.indexOf("/static") == 0) {
            return Global.HOST + s;
        }

        return s;
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static DecimalFormat df = new java.text.DecimalFormat("#.00");

    /**
     * 初始化webview配置
     * @param webView
     */
    public static void initWebView(WebView webView){
        webView.getSettings().setJavaScriptEnabled(true);

        // 防止webView滚动时背景变成黑色
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            webView.setBackgroundColor(0x00000000);
        } else {
            webView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }

        webView.getSettings().setDefaultTextEncodingName("UTF-8");
    }

    /**
     * 从文件里面读文字数据
     * @param inputStream
     * @return
     */
    public static String readTextFile(InputStream inputStream){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try{
            while((len = inputStream.read(buf)) != -1){
                outputStream.write(buf,0,len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }

}
