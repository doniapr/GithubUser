package com.doniapr.githubuser;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvUser;
    private ProgressBar progressBar;
    UserAdapter adapter;
    UserViewModel userViewModel;
    LinearLayoutManager layoutManager;
    int since = 0;
    int itemCount = 42;
    private int pastVisibleItems,visibleItemCount, totalItemCount, previousTotal = 0;
    private int viewThreshold =43;

    private Observer<ArrayList<User>> getUser = new Observer<ArrayList<User>>() {
        @Override
        public void onChanged(@Nullable ArrayList<User> users) {
            if (users != null) {
                adapter.setData(users);
                showLoading(false);
            }
        }
    };

    private Observer<ArrayList<User>> addUser = new Observer<ArrayList<User>>() {
        @Override
        public void onChanged(@Nullable ArrayList<User> users) {
            if (users != null) {
                adapter.addUser(users);
                showLoading(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUsers().observe(this, getUser);
        userViewModel.setUserData(getApplicationContext(), since);
        showLoading(true);

        adapter = new UserAdapter();
        adapter.notifyDataSetChanged();
        layoutManager = new LinearLayoutManager(getApplicationContext());

        rvUser.setLayoutManager(layoutManager);
        rvUser.setAdapter(adapter);

        rvUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                itemCount++;

                if (dy>0){
                    if (progressBar.getVisibility()== View.VISIBLE){
                        if (totalItemCount>previousTotal){
                            showLoading(false);
                            previousTotal = totalItemCount;
                            Log.e("totaldata", String.valueOf(previousTotal));
                        }
                    }
                    if (!(progressBar.getVisibility()== View.VISIBLE)&&(totalItemCount-visibleItemCount) <= (pastVisibleItems+viewThreshold)){
                        showLoading(true);
                        userViewModel.getUsers().observe(MainActivity.this, addUser);
                        userViewModel.addUserData(getApplicationContext(), previousTotal);

                    }
                }
            }
        });
    }

    private void init() {
        rvUser = findViewById(R.id.rv_user);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getSearchFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
