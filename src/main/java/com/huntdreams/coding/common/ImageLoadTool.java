package com.huntdreams.coding.common;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import com.huntdreams.coding.R;


/**
 * 图片加载工具
 * 封装了图片下载并缓存
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/5/2.
 */
public class ImageLoadTool {

    public ImageLoader imageLoader = ImageLoader.getInstance();

    public static DisplayImageOptions options = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.ic_default_user)
            .showImageForEmptyUri(R.drawable.ic_default_user)
            .showImageOnFail(R.drawable.ic_default_user)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public static DisplayImageOptions optionsImage = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.ic_default_image)
            .showImageForEmptyUri(R.drawable.ic_default_image)
            .showImageOnFail(R.drawable.ic_default_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    //两像素圆角
    public static final DisplayImageOptions optionsRounded = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_default_image)
            .showImageForEmptyUri(R.drawable.ic_default_image)
            .showImageOnFail(R.drawable.ic_default_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(2))
            .build();


    //两dp圆角
    public static final DisplayImageOptions optionsRounded2 = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_default_image)
            .showImageForEmptyUri(R.drawable.ic_default_image)
            .showImageOnFail(R.drawable.ic_default_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(Global.dpToPx(2)))
            .build();

    public ImageLoadTool() {
    }

    public void loadImage(ImageView imageView, String url) {
        imageLoader.displayImage(Global.makeSmallUrl(imageView, url), imageView, options);
    }

    public void loadImage(ImageView imageView, String url, SimpleImageLoadingListener animate) {
        imageLoader.displayImage(Global.makeSmallUrl(imageView, url), imageView, options, animate);
    }

    public void loadImage(ImageView imageView, String url, DisplayImageOptions imageOptions) {
        imageLoader.displayImage(Global.makeSmallUrl(imageView, url), imageView, imageOptions);
    }

    public void loadImage(ImageView imageView, String url, DisplayImageOptions displayImageOptions,SimpleImageLoadingListener animate) {
        imageLoader.displayImage(url, imageView, displayImageOptions,animate);
    }

    public void loadImageFromUrl(ImageView imageView, String url) {
        imageLoader.displayImage(url, imageView, options);
    }

    public void loadImageFromUrl(ImageView imageView, String url, DisplayImageOptions displayImageOptions) {
        imageLoader.displayImage(url, imageView, displayImageOptions);
    }



}

