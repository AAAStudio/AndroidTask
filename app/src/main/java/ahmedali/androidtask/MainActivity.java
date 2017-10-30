package ahmedali.androidtask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ahmedali.androidtask.adapter.RepoAdapter;
import ahmedali.androidtask.model.RepoModel;
import ahmedali.androidtask.utils.AppController;
import ahmedali.androidtask.utils.EndlessRecyclerViewScrollListener;
import ahmedali.androidtask.utils.MakeRequst;
import ahmedali.androidtask.utils.Urls;

public class MainActivity extends AppCompatActivity {

    private final static int PerPage = 10;

    private Toolbar toolBar;
    private RecyclerView mainList;
    private SwipeRefreshLayout swipe;
    private CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;

    ArrayList<RepoModel> repoModels;

    RepoAdapter repoAdapter;
    LinearLayoutManager layoutManager;
    String reposUrl = Urls.SquareRepos + PerPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repoModels = new ArrayList<>();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        mainList = (RecyclerView) findViewById(R.id.mainList);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AppController.getInstance().getRequestQueue().getCache().clear();
                mainList.getRecycledViewPool().clear();
                repoModels.clear();
                repoAdapter.notifyDataSetChanged();
                RequstRepos(reposUrl);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipe.isRefreshing()){
                            swipe.setRefreshing(false);
                        }
                    }
                },2000);
            }
        });

        SetUpRecyclerView();
        RequstRepos(reposUrl);
        ShowSnackBar();
    }

    private void ShowSnackBar() {
        if (!isInternetAvailable()) {
            snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void SetUpRecyclerView() {
        repoAdapter = new RepoAdapter(MainActivity.this, repoModels, new RepoAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(RepoModel item) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(layoutManager);
        mainList.setItemAnimator(new DefaultItemAnimator());
        mainList.setAdapter(repoAdapter);
        mainList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                RequstRepos(reposUrl+"&page="+(page+1));
            }
        });
    }

    private void RequstRepos(String url) {
        Log.i("url",url);
        new MakeRequst(this).makeStringResponse(url, new MakeRequst.VolleyCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONArray response = new JSONArray(result);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject singleObj = response.getJSONObject(i);
                    JSONObject ownerObj = singleObj.getJSONObject("owner");
                    repoModels.add(new RepoModel(singleObj.getInt("id"),
                            ownerObj.getString("login"),
                            singleObj.getString("name"),
                            singleObj.getString("description"),
                            ownerObj.getString("html_url"),
                            singleObj.getString("html_url"),
                            singleObj.getBoolean("fork")));
                }
                repoAdapter.notifyItemChanged(repoModels.size());
            }
        });
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
