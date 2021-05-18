package com.example.news_api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class new_Loader extends AsyncTaskLoader<List<news>> {

    private String mUrl;

    public new_Loader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<news> loadInBackground() {
        if(mUrl == null) {
            return null;
        }
        return Query.fetchNewsData(mUrl);
    }
}
