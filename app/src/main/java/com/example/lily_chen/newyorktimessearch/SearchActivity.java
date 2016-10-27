package com.example.lily_chen.newyorktimessearch;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.lily_chen.newyorktimessearch.Articles.Article;
import com.example.lily_chen.newyorktimessearch.Articles.ArticleArrayAdapter;
import com.example.lily_chen.newyorktimessearch.Articles.EndlessRecyclerViewScrollListener;
import com.example.lily_chen.newyorktimessearch.Filters.Filters;
import com.example.lily_chen.newyorktimessearch.Filters.FiltersDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity
        implements FiltersDialogFragment.FiltersDialogListener {

    @BindView(R.id.rvResults) RecyclerView rvResults;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    RequestParams params;
    Filters filters;

    Handler handler = new Handler();

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews() {
        ButterKnife.bind(this);
        params = new RequestParams();
        filters = new Filters();
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);

        adapter.setOnItemClickListener(new ArticleArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String url = articles.get(position).getWebUrl();
                launchCustomChrome(url);
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(staggeredGridLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvResults.addOnScrollListener(scrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onArticleSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filters) {
            showFiltersDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(String query) {
        scrollListener.resetState();
        params.put("q", query);
        params.put("page", 0);
        paramsFromFilters();
        articles.clear();
        fetchArticles.run();
    }

    public void loadNextDataFromApi(int offset) {
        params.put("page", offset);
        int curSize = adapter.getItemCount();
        handler.postDelayed(fetchArticles, 1000);
        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
    }

    private Runnable fetchArticles = new Runnable() {
        @Override
        public void run() {
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleJsonResults = null;
                    Log.d("DEBUG", params.toString());
                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        articles.addAll(Article.fromJSONArray(articleJsonResults));
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", params.toString());
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    };

    private void launchCustomChrome(String url){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        builder.addDefaultShareMenuItem();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void showFiltersDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FiltersDialogFragment filtersDialogFragment = FiltersDialogFragment.newInstance(filters);
        filtersDialogFragment.show(fm, "fragment_filters");
    }

    @Override
    public void onFinishFiltersDialog(Filters filters){
        this.filters = filters;
    }

    private void paramsFromFilters(){
        Date beginDate = filters.getBeginDate();
        String sortOrder = filters.getSortOrder();
        String newsDesk = filters.getNewsDesk();

        if (beginDate == null) {
            params.remove("begin_date");
        } else {
            String year = beginDate.getYear() + "";
            String month;
            String day;

            if (beginDate.getMonth() < 9){
                month = "0"+ (beginDate.getMonth() + 1);
            } else {
                month = "" + (beginDate.getMonth() + 1);
            }

            if (beginDate.getDay() < 9){
                day = "0"+ (beginDate.getDate() + 1);
            } else {
                day = "" + (beginDate.getDate() + 1);
            }

            params.put("begin_date", "" + year + month + day);
        }

        if (sortOrder.equals("")) {
            params.remove("sort");
        } else {
            params.put("sort", sortOrder);
        }

        if (newsDesk.equals("Any") || newsDesk.equals("")) {
            params.remove("fq");
        } else {
            params.put("fq", "news_desk:" + newsDesk);
        }
    }

}
