package com.sugarac.sribbble.model;


import android.support.annotation.Nullable;

import java.util.Date;
import java.util.Map;

public class Shot {
    public static final String IMAGE_NORMAL = "normal";
    public static final String IMAGE_HIDPI = "hidpi";

    public String id;
    public String title;
    public String description;
    public String html_url;

    public int width;
    public int height;
    public Map<String, String> images;
    public boolean animated;

    public int views_count;
    public int likes_count;
    public int buckets_count;
    public int comments_count;

    public Date created_at;

    public User user;

    public boolean liked;
    public boolean bucketed;

    @Nullable
    public String getImageUrl() {
        if (images == null) {
            return null;
        } else if (animated) {
            return images.get(IMAGE_NORMAL);
        }

        return images.containsKey(IMAGE_HIDPI)
                ? images.get(IMAGE_HIDPI)
                : images.get(IMAGE_NORMAL);
    }
}
