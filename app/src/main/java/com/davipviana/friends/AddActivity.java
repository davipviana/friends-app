package com.davipviana.friends;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Davi Viana on 18/02/2018.
 */

public class AddActivity extends FragmentActivity {
    private final String LOG_TAG = AddActivity.class.getSimpleName();
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

        contentResolver = AddActivity.this.getContentResolver();

        btnSave = (Button) findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    ContentValues values = new ContentValues();
                    values.put(FriendsContract.FriendsColumns.NAME, txtName.getText().toString());
                    values.put(FriendsContract.FriendsColumns.EMAIL, txtEmail.getText().toString());
                    values.put(FriendsContract.FriendsColumns.PHONE, txtPhone.getText().toString());

                    Uri returned = contentResolver.insert(FriendsContract.URI_TABLE, values);
                    Log.d(LOG_TAG, "record id returned is " + returned.toString());

                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please ensure your have entered some valid data.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValid() {
        return !(txtName.getText().toString().length() == 0) ||
                (txtEmail.getText().toString().length() == 0) ||
                (txtPhone.getText().toString().length() == 0);
    }

    private boolean someDataEntered() {
        return (txtName.getText().toString().length() > 0) ||
                (txtEmail.getText().toString().length() > 0) ||
                (txtPhone.getText().toString().length() > 0);
    }

    @Override
    public void onBackPressed() {
        if(someDataEntered()) {
            FriendsDialog dialog = new FriendsDialog();
            Bundle args = new Bundle();
            args.putString(FriendsDialog.DIALOG_TYPE, FriendsDialog.CONFIRM_EXIT);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "confirm-exit");
        } else {
            super.onBackPressed();
        }
    }
}
