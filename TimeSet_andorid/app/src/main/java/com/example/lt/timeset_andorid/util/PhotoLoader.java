package com.example.lt.timeset_andorid.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

import indi.liyi.viewer.ImageLoader;

public class PhotoLoader extends ImageLoader {

    @Override
    public void displayImage(final Object src, ImageView imageView, final LoadCallback callback) {
        Glide.with(imageView.getContext())
                .load(src)
//                .into(imageView);
                .into(new CustomViewTarget<ImageView, Drawable>(imageView) {

                    @Override
                    protected void onResourceLoading(@Nullable Drawable placeholder) {
                        super.onResourceLoading(placeholder);
//                        ProgressController.registerListener(src, new OnProgressListener() {
//                            @Override
//                            public void onProgress(float progress, long totalSize) {
//                                callback.onLoading(progress);
//                            }
//                        });
                        if(callback!=null){
                            callback.onLoadStarted(placeholder);
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if(callback!=null) {
                            callback.onLoadSucceed(resource);
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if(callback!=null) {
                            callback.onLoadFailed(errorDrawable);
                        }
//                        ProgressController.unregisterListener(src);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
