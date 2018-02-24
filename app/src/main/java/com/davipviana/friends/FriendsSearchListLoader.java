package com.davipviana.friends;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davi Viana on 18/02/2018.
 */

public class FriendsSearchListLoader extends AsyncTaskLoader<List<Friend>> {
    private static final String LOG_TAG = FriendsSearchListLoader.class.getSimpleName();
    private List<Friend> friendList;
    private ContentResolver contentResolver;
    private Cursor cursor;
    private String filterText;

    public FriendsSearchListLoader(Context context, Uri uri, ContentResolver contentResolver, String filterText) {
        super(context);
        this.contentResolver = contentResolver;
        this.filterText = filterText;

    }

    @Override
    public List<Friend> loadInBackground() {
        String[] projection = {
                BaseColumns._ID,
                FriendsContract.FriendsColumns.NAME,
                FriendsContract.FriendsColumns.EMAIL,
                FriendsContract.FriendsColumns.PHONE
        };

        List<Friend> entries = new ArrayList<Friend>();
        String selection = FriendsContract.FriendsColumns.NAME + " LIKE '" + filterText + "%'";

        this.cursor = this.contentResolver.query(FriendsContract.URI_TABLE, projection, selection, null, null);
        if (this.cursor != null && this.cursor.moveToFirst()) {
            do {
                int _id = this.cursor.getInt(this.cursor.getColumnIndex(BaseColumns._ID));
                String name = this.cursor.getString(
                        this.cursor.getColumnIndex(FriendsContract.FriendsColumns.NAME));
                String email = this.cursor.getString(
                        this.cursor.getColumnIndex(FriendsContract.FriendsColumns.EMAIL));
                String phone = this.cursor.getString(
                        this.cursor.getColumnIndex(FriendsContract.FriendsColumns.PHONE));
                Friend friend = new Friend(_id, name, email, phone);
                entries.add(friend);
            } while (cursor.moveToNext());
        }

        return entries;
    }

    @Override
    public void deliverResult(List<Friend> newFriendList) {
        if (isReset() && newFriendList != null) {
            this.cursor.close();
        }
        List<Friend> oldFriendList = this.friendList;
        if (this.friendList == null || this.friendList.size() == 0) {
            Log.d(LOG_TAG, "+++++++++ No Data returned");
        }
        this.friendList = newFriendList;

        if(isStarted()) {
            super.deliverResult(newFriendList);
        }
        if(oldFriendList != null && oldFriendList != newFriendList) {
            this.cursor.close();        }
    }

    @Override
    protected void onStartLoading() {
        if(this.friendList != null) {
            deliverResult(this.friendList);
        }
        if(takeContentChanged() || this.friendList == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(this.cursor != null) {
            this.cursor.close();
        }

        this.friendList = null;
    }

    @Override
    public void onCanceled(List<Friend> friendList) {
        super.onCanceled(friendList);

        if(this.cursor != null) {
            this.cursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}
