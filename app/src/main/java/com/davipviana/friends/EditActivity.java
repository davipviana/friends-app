package com.davipviana.friends;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Davi Viana on 18/02/2018.
 */

public class EditActivity extends FragmentActivity {
    private final String LOG_TAG = EditActivity.class.getSimpleName();
    private TextView txtName, txtEmail, txtPhone;
    private Button btnSave;
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = (TextView) findViewById(R.id.friend_name);
        txtEmail = (TextView) findViewById(R.id.friend_email);
        txtPhone = (TextView) findViewById(R.id.friend_phone);

        contentResolver = EditActivity.this.getContentResolver();

        Intent intent = getIntent();
        final String _id = intent.getStringExtra(FriendsContract.FriendsColumns.ID);
        String name = intent.getStringExtra(FriendsContract.FriendsColumns.NAME);
        String email = intent.getStringExtra(FriendsContract.FriendsColumns.EMAIL);
        String phone = intent.getStringExtra(FriendsContract.FriendsColumns.PHONE);

        txtName.setText(name);
        txtEmail.setText(email);
        txtPhone.setText(phone);

        btnSave = (Button) findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(FriendsContract.FriendsColumns.NAME, txtName.getText().toString());
                values.put(FriendsContract.FriendsColumns.EMAIL, txtEmail.getText().toString());
                values.put(FriendsContract.FriendsColumns.PHONE, txtPhone.getText().toString());

                Uri uri = FriendsContract.Friends.buildFriendUri(_id);
                int recordsUpdated = contentResolver.update(uri, values, null, null);
                Log.d(LOG_TAG, "number of records update = " + recordsUpdated);

                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }
}