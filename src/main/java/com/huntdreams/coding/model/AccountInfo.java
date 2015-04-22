package com.huntdreams.coding.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.huntdreams.coding.common.LoginBackground;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 账户信息数据缓存类
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/24.
 */
public class AccountInfo {


    private static final String GLOBAL_SETTING = "GLOBAL_SETTING";
    private static final String GLOBAL_SETTING_BACKGROUND = "GLOBAL_SETTING_BACKGROUND";

    /**
     * 设置检查登录时的背景
     * @param context
     */
    public static void setCheckLoginBackground(Context context){
        Calendar calendar = Calendar.getInstance();
        SharedPreferences.Editor editor = context.getSharedPreferences(GLOBAL_SETTING,Context.MODE_PRIVATE).edit();
        editor.putLong(GLOBAL_SETTING_BACKGROUND,calendar.getTimeInMillis());
        editor.commit();
    }

    /**
     * 距离上次检查24小时后再检查
     * @param context
     * @return
     */
    public static boolean needCheckLoginBackground(Context context){
        long last = context.getSharedPreferences(GLOBAL_SETTING,Context.MODE_PRIVATE).getLong(GLOBAL_SETTING_BACKGROUND,0);
        return (Calendar.getInstance().getTimeInMillis() - last) > 1000 * 3600 * 24;
    }

    /**
     * 数据缓存类
     * @param <T>
     */
    static class DataCache<T>{

        public final static String FILDER_GLOBAL = "FILDER_GLOBAL";

        public void save(Context context,ArrayList<T> data,String name){
            save(context,data,name,"");
        }

        public void saveGlobal(Context context,ArrayList<T> data,String name){
            save(context,data,name,FILDER_GLOBAL);
        }

        public void delete(Context context,String name){
            File file = new File(context.getFilesDir(),name);
            if(file.exists())
                file.delete();
        }

        private void save(Context context,ArrayList<T> data,String name,String folder){
            if(context == null)
                return;

            File file;
            if(!folder.isEmpty()){
                File fileDir = new File(context.getFilesDir(),folder);
                if(!fileDir.exists() || !fileDir.isDirectory()){
                    fileDir.mkdir();
                }
                file = new File(fileDir,name);
            }else{
                file = new File(context.getFilesDir(),name);
            }

            if(file.exists())
                file.delete();

            try{
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(data);
                oos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public ArrayList<T> load(Context context,String name){
            return load(context,name,"");
        }

        public ArrayList<T> loadGlobal(Context context,String name){
            return load(context,name,FILDER_GLOBAL);
        }

        private ArrayList<T> load(Context context,String name,String folder){
            ArrayList<T> data = null;

            File file;
            if(!folder.isEmpty()){
                File fileDir = new File(context.getFilesDir(),folder);
                if(!fileDir.exists() || !fileDir.isDirectory()){
                    fileDir.mkdir();
                }
                file = new File(fileDir,name);
            }else{
                file = new File(context.getFilesDir(),name);
            }

            if(file.exists()){
                try{
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    data = (ArrayList<T>) ois.readObject();
                    ois.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            if(data == null){
                data = new ArrayList<T>();
            }

            return data;
        }
    }


    // 背景图缓存文件夹
    private static final String BACKGROUNDS = "BACKGROUNDS";

    // 保存背景图片的数据
    public static void saveBackgrounds(Context context,ArrayList<LoginBackground.PhotoItem> data){
        new DataCache<LoginBackground.PhotoItem>().save(context,data,BACKGROUNDS);
    }

    // 加载缓存的背景图
    public static ArrayList<LoginBackground.PhotoItem> loadBackgrounds(Context context){
        return new DataCache<LoginBackground.PhotoItem>().load(context,BACKGROUNDS);
    }

}
