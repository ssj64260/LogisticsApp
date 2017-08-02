package com.msqsoft.aboutapp.widget.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.msqsoft.aboutapp.R;

/**
 * 加载图片实现
 */

public class ImageLoader implements ImageLoaderWrapper {

    private final int mPlaceHolder = R.drawable.list_headportrait;
    private final int mErrorImage = R.drawable.list_headportrait;

    @Override
    public void loadImageFitCenter(Context context, ImageView imageView, String url) {
        loadImageFitCenter(context, imageView, url, mPlaceHolder, mErrorImage);
    }

    @Override
    public void loadImageFitCenter(Context context, ImageView imageView, String url, int placeholder, int errorImage) {
        loadImageFitCenter(context, imageView, url, placeholder, errorImage, null);
    }

    @Override
    public void loadImageFitCenter(Context context, ImageView imageView, String url, BitmapTransformation transformation) {
        loadImageFitCenter(context, imageView, url, mPlaceHolder, mErrorImage, transformation);
    }

    @Override
    public void loadImageFitCenter(Context context, ImageView imageView, String url, int placeholder, int errorImage, BitmapTransformation transformation) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(errorImage)
                .fitCenter()
                .dontAnimate();

        if (transformation != null) {
            options.transform(transformation);
        }

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadImageCenterCrop(Context context, ImageView imageView, String url) {
        loadImageCenterCrop(context, imageView, url, mPlaceHolder, mErrorImage);
    }

    @Override
    public void loadImageCenterCrop(Context context, ImageView imageView, String url, int placeholder, int errorImage) {
        loadImageCenterCrop(context, imageView, url, placeholder, errorImage, null);
    }

    @Override
    public void loadImageCenterCrop(Context context, ImageView imageView, String url, int placeholder, int errorImage, BitmapTransformation transformation) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(errorImage)
                .centerCrop()
                .dontAnimate();

        if (transformation != null) {
            options.transform(transformation);
        }

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
