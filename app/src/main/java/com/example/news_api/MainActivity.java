package com.example.news_api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<news>> {

    private static final int Loader_ID=1;
    private static String LOG=MainActivity.class.getName();



    ListView mListView;
    TextView mTextView;
    new_Adapter mAdapter;
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        mListView = (ListView) findViewById(R.id.list_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextView = (TextView) findViewById(R.id.text_view);

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager.getInstance(this).initLoader(Loader_ID,null,this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(R.string.no_internet_text);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                news news=mAdapter.getItem(i);

                Uri newurl=Uri.parse(news.getUrl());

                Intent web=new Intent(Intent.ACTION_VIEW,newurl);

                startActivity(web);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.setting,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent settingsIntent = new Intent(this, Setting_Activity2.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<news>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String query=sharedPreferences.getString(getString(R.string.setting_query_Search),"google");
        String section=sharedPreferences.getString(getString(R.string.setting_section_search),"technology");
        Uri uri=Uri.parse("https://content.guardianapis.com/search");
        Uri.Builder builder=uri.buildUpon();
        builder.appendQueryParameter("api-key","5db22a5c-8bb4-40eb-89a3-4945f5966fcc");
        if(query!=null)
       builder.appendQueryParameter("q",query);
        else{
            query="google";
            builder.appendQueryParameter("q",query);

        }
        //builder.appendQueryParameter("page","1");
      //  builder.appendQueryParameter("")

        if(section!=null)
        builder.appendQueryParameter("section",section);
        else
            builder.appendQueryParameter("section","technology");

        return new new_Loader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<news>> loader, List<news> data) {
        mAdapter = new new_Adapter(this, data);
        mProgressBar.setVisibility(View.GONE);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<news>> loader) {

    }
}