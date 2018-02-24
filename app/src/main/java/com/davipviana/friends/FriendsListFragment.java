package com.davipviana.friends;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

/**
 * Created by Davi Viana on 18/02/2018.
 */

public class FriendsListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<Friend>> {
    private static final String LOG_TAG = FriendsListFragment.class.getSimpleName();
    private FriendsCustomAdapter adapter;
    private static final int LOADER_ID = 1;
    private ContentResolver contentResolver;
    private List<Friend> friendList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        this.contentResolver = getActivity().getContentResolver();
        this.adapter = new FriendsCustomAdapter(getActivity(), getChildFragmentManager());
        setEmptyText("No friends");
        setListAdapter(this.adapter);
        setListShown(false);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Friend>> onCreateLoader(int id, Bundle args) {
        this.contentResolver = getActivity().getContentResolver();
        return new FriendsListLoader(getActivity(), FriendsContract.URI_TABLE, this.contentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<Friend>> loader, List<Friend> data) {
        this.adapter.setData(data);
        this.friendList = data;
        if(isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Friend>> loader) {
        this.adapter.setData(null);
    }
}
