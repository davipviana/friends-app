package com.davipviana.friends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Davi Viana on 18/02/2018.
 */

public class FriendsDatabase extends SQLiteOpenHelper {

    private static final String TAG = FriendsDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "friends.db";
    private static final int DATABASE_VERSION = 2;
    private final Context context;

    interface Tables {
        String FRIENDS = "friends";
    }

    public FriendsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.FRIENDS + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FriendsContract.FriendsColumns.NAME + " TEXT NOT NULL,"
                + FriendsContract.FriendsColumns.EMAIL + " TEXT NOT NULL,"
                + FriendsContract.FriendsColumns.PHONE + " TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if(version == 1) {
            // Add some extra fields to the database without deleting existing data
            version = 2;
        }

        if(version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.FRIENDS);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
