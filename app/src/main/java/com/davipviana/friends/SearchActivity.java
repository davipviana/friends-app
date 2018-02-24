package com.davipviana.friends;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Davi Viana on 18/02/2018.
 */

public class SearchActivity extends FragmentActivity
    implements LoaderManager.LoaderCallbacks<List<Friend>> {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private FriendsCustomAdapter friendsCustomAdapter;
    private static int LOADER_ID = 2;
    private ContentResolver contentResolver;
    private List<Friend> friendListRetrieved;
    private ListView listView;
    private EditText searchText;
    private Button searchButton;
    private String matchText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        listView = (ListView) findViewById(R.id.searchResultList);
        searchText = (EditText) findViewById(R.id.searchName);
        searchButton = (Button) findViewById(R.id.searchButton);
        contentResolver = getContentResolver();
        friendsCustomAdapter = new FriendsCustomAdapter(SearchActivity.this, getSupportFragmentManager());
        listView.setAdapter(friendsCustomAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchText = searchText.getText().toString();
                getSupportLoaderManager().initLoader(LOADER_ID++, null, SearchActivity.this);
            }
        });
    }

    @Override
    public Loader<List<Friend>> onCreateLoader(int id, Bundle args) {
        return new FriendsSearchListLoader(SearchActivity.this, FriendsContract.URI_TABLE, this.contentResolver, matchText);
    }

    @Override
    public void onLoadFinished(Loader<List<Friend>> loader, List<Friend> data) {
        this.friendsCustomAdapter.setData(data);
        this.friendListRetrieved = data;
    }

    @Override
    public void onLoaderReset(Loader<List<Friend>> loader) {
        this.friendsCustomAdapter.setData(null);
    }
}
