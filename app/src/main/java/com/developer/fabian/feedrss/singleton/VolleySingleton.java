package com.developer.fabian.feedrss.singleton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public final class VolleySingleton {

    @SuppressLint("StaticFieldLeak")
    private static VolleySingleton singleton;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private ImageLoader imageLoader;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(40);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (singleton == null)
            singleton = new VolleySingleton(context.getApplicationContext());

        return singleton;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
