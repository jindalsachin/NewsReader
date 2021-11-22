package com.example.android.newsapp.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.android.newsapp.MyRecyclerView;
import com.example.android.newsapp.News;
import com.example.android.newsapp.NewsLoader;
import com.example.android.newsapp.R;
import com.example.android.newsapp.adapter.NewsAdapter;
import com.example.android.newsapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Business extends Fragment
        implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = World.class.getName();
    private static final int NEWS_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private NewsAdapter mAdapter;

    public Business() { }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycle, container, false);

        mAdapter = new NewsAdapter(getActivity(), new ArrayList<News>());
        MyRecyclerView recyclerView = (MyRecyclerView) rootView.findViewById(R.id.recycle_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        mEmptyStateTextView = rootView.findViewById(R.id.empty_view);
        recyclerView.setEmptyView(mEmptyStateTextView);
        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = rootView.findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        return rootView;
    }
    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(Constants.NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("section", "business");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("from-date", "2021-01-01");
        uriBuilder.appendQueryParameter("show-fields", "headline,byline,firstPublicationDate,thumbnail");
        uriBuilder.appendQueryParameter("orderby", orderBy);
        uriBuilder.appendQueryParameter("api-key", "test");

        return new NewsLoader(getActivity(), uriBuilder.toString());
    }
    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsInfo) {
        getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
        mAdapter.clearAll();
        if (newsInfo != null && !newsInfo.isEmpty()) {
            mAdapter.addAll(newsInfo);
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        mAdapter.clearAll();
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}