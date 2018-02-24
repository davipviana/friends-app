package com.davipviana.friends;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Davi Viana on 18/02/2018.
 */

public class FriendsProvider extends ContentProvider {
    private FriendsDatabase openHelper;

    private static String TAG = FriendsProvider.class.getSimpleName();
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int FRIENDS = 100;
    private static final int FRIENDS_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FriendsContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "friends", FRIENDS);
        matcher.addURI(authority, "friends/*", FRIENDS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        this.openHelper = new FriendsDatabase(getContext());
        return true;
    }

    private void deleteDatabase() {
        this.openHelper.close();
        FriendsDatabase.deleteDatabase(getContext());
        this.openHelper = new FriendsDatabase(getContext());
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = this.openHelper.getReadableDatabase();
        final int match = this.uriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsDatabase.Tables.FRIENDS);

        switch (match) {
            case FRIENDS:
                // do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = this.uriMatcher.match(uri);
        switch (match) {
            case FRIENDS:
                return FriendsContract.Friends.CONTENT_TYPE;
            case FRIENDS_ID:
                return FriendsContract.Friends.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = this.openHelper.getWritableDatabase();
        final int match = this.uriMatcher.match(uri);

        switch (match) {
            case FRIENDS:
                long recordId = db.insertOrThrow(FriendsDatabase.Tables.FRIENDS, null, values);
                return FriendsContract.Friends.buildFriendUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.v(TAG, "delete(uri=" + uri);

        if(uri.equals(FriendsContract.URI_TABLE)) {
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = this.openHelper.getWritableDatabase();
        final int match = this.uriMatcher.match(uri);
        switch (match) {
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

                return db.delete(FriendsDatabase.Tables.FRIENDS, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.v(TAG, "update(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = this.openHelper.getWritableDatabase();
        final int match = this.uriMatcher.match(uri);
        String selectionCriteria = selection;

        switch (match) {
            case FRIENDS:
                // do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return db.update(FriendsDatabase.Tables.FRIENDS, values, selectionCriteria, selectionArgs);
    }
}
