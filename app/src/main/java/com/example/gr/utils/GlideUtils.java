package com.example.gr.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.gr.R;


public class GlideUtils {

    public static void loadUrlBanner(String url, ImageView imageView) {
        if (StringUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.default_img);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.default_img)
                .dontAnimate()
                .into(imageView);
    }

    public static void loadUrl(String url, ImageView imageView) {
        if (StringUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.default_img);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.default_img)
                .dontAnimate()
                .into(imageView);
    }
}