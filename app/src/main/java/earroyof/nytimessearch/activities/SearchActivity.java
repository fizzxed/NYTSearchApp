package earroyof.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import earroyof.nytimessearch.Article;
import earroyof.nytimessearch.ArticleArrayAdapter;
import earroyof.nytimessearch.EndlessRecyclerViewScrollListener;
import earroyof.nytimessearch.R;

public class SearchActivity extends AppCompatActivity {

    //EditText etQuery;
    //Button btnSearch;
    //GridView gvResults;
    RecyclerView rvResults;
    String query;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews(savedInstanceState);

    }

    public void setupViews(Bundle savedInstanceState) {
        //etQuery = (EditText) findViewById(R.id.etQuery);
        //btnSearch = (Button) findViewById(R.id.btnSearch);
        //gvResults = (GridView) findViewById(R.id.gvResults);
        rvResults = (RecyclerView) findViewById(R.id.rvResults);

        // Check for saved instance state if returning to activity from background

        if (savedInstanceState == null || !savedInstanceState.containsKey("started")) {
            articles = new ArrayList<>();
        } else {
            articles = savedInstanceState.getParcelableArrayList("started");
        }
        adapter = new ArticleArrayAdapter(articles);
        rvResults.setAdapter(adapter);

        // hook up listener for grid click
        adapter.setOnItemClickListener(new ArticleArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // create an intent to display the article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);

                // get the article to display
                Article article = articles.get(position);

                // pass in the article into the intent
                i.putExtra("article", article);

                // launch the activity
                startActivity(i);

            }
        });

        // Setup layout manager
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);

        // Setup onScrollListener
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });

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
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599

                // processing query
                SearchActivity.this.query = query;
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?";
                RequestParams params = new RequestParams();
                params.put("api_key", "3599c1c6e204413eb1a9676f31a22c23");
                params.put("page", 0);
                params.put("q", query);

                client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Log.d("mydebug", response.toString());
                        JSONArray articleResults = null;
                        try {
                            articleResults = response.getJSONObject("response").getJSONArray("docs");
                            // V equivalent to making this articles.addall.... and then notifying adapter data has changed
                            // no gotcha's to this
                            articles.addAll(Article.fromJsonArray(articleResults));
                            adapter.notifyDataSetChanged();
                            Log.d("mydebug", articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

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

    public void customLoadMoreDataFromApi(int offset) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?";
        RequestParams params = new RequestParams();
        params.put("api_key", "3599c1c6e204413eb1a9676f31a22c23");
        params.put("page", offset);
        params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("mydebug", response.toString());
                JSONArray articleResults = null;
                try {
                    articleResults = response.getJSONObject("response").getJSONArray("docs");
                    // V equivalent to making this articles.addall.... and then notifying adapter data has changed
                    // no gotcha's to this
                    articles.addAll(Article.fromJsonArray(articleResults));

                    int curSize = adapter.getItemCount();
                    adapter.notifyItemRangeInserted(curSize, articles.size() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("started", articles);
        super.onSaveInstanceState(outState);
    }


    /* public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();

        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?";
        RequestParams params = new RequestParams();
        params.put("api_key", "3599c1c6e204413eb1a9676f31a22c23");
        params.put("page", 0);
        params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("mydebug", response.toString());
                JSONArray articleResults = null;
                try {
                    articleResults = response.getJSONObject("response").getJSONArray("docs");
                    // V equivalent to making this articles.addall.... and then notifying adapter data has changed
                    // no gotcha's to this
                    adapter.addAll(Article.fromJsonArray(articleResults));
                    Log.d("mydebug", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    } */

}
