package com.davipviana.friends;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Davi Viana on 18/02/2018.
 */

public class FriendsCustomAdapter extends ArrayAdapter<Friend> {
    private LayoutInflater layoutInflater;
    private static FragmentManager fragmentManager;

    public FriendsCustomAdapter(Context context, FragmentManager fragmentManager) {
        super(context, android.R.layout.simple_list_item_2);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FriendsCustomAdapter.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view;
        if(convertView == null) {
            view = this.layoutInflater.inflate(R.layout.custom_friend, parent, false);
        } else {
            view = convertView;
        }

        final Friend friend = getItem(position);
        final int _id = friend.getId();
        final String name = friend.getName();
        final String email = friend.getEmail();
        final String phone = friend.getPhone();

        ((TextView) view.findViewById(R.id.friend_name)).setText(name);
        ((TextView) view.findViewById(R.id.friend_email)).setText(email);
        ((TextView) view.findViewById(R.id.friend_phone)).setText(phone);

        Button editButton = (Button) view.findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra(FriendsContract.FriendsColumns.ID, String.valueOf(_id));
                intent.putExtra(FriendsContract.FriendsColumns.NAME, name);
                intent.putExtra(FriendsContract.FriendsColumns.EMAIL, email);
                intent.putExtra(FriendsContract.FriendsColumns.PHONE, phone);
                getContext().startActivity(intent);
            }
        });

        Button deleteButton = (Button) view.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendsDialog dialog = new FriendsDialog();
                Bundle args = new Bundle();
                args.putString(FriendsDialog.DIALOG_TYPE, FriendsDialog.DELETE_RECORD);
                args.putInt(FriendsContract.FriendsColumns.ID, _id);
                args.putString(FriendsContract.FriendsColumns.NAME, name);

                dialog.setArguments(args);
                dialog.show(FriendsCustomAdapter.fragmentManager, "delete-record");
            }
        });

        return view;
    }

    public void setData(List<Friend> data) {
        clear();
        if(data != null) {
            for(Friend friend : data) {
                add(friend);
            }
        }
    }
}
