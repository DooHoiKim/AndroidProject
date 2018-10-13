package com.dh.pro1822.pwassistance;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PasswordAdapter extends CursorAdapter {

/*
    private ContentResolver mContent;

    private static final int COLUMN_DISPLAY_NAME = 1;
    public static final String[] CONTACT_PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
    };
*/

    //    public PasswordAdapter(Context context, Cursor c, int flag) {
    public PasswordAdapter(Context context, Cursor c) {
        super(context, c, false);
//        super(context, c, flag);
//        mContent = context.getContentResolver();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//        TextView listName = (TextView) view.findViewById(R.id.list_name);
//        TextView listId = (TextView) view.findViewById(R.id.list_id);
//        TextView listPassword = (TextView) view.findViewById(R.id.list_password);


        /*
        Toast toast = Toast.makeText(context, listName.getText().toString(), Toast.LENGTH_SHORT);
        toast.show();
        */

//        listName.setText(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
//        listId.setText(cursor.getString(cursor.getColumnIndexOrThrow("LOG_IN_ID")));
//        listPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow("LOG_IN_PW")));

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.listName.setText(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
        viewHolder.listId.setText(cursor.getString(cursor.getColumnIndexOrThrow("LOG_IN_ID")));
        viewHolder.listPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow("LOG_IN_PW")));

    }

    static class ViewHolder {
        TextView listName;
        TextView listId;
        TextView listPassword;

        public ViewHolder(View view) {
            this.listName = (TextView) view.findViewById(R.id.list_name);
            this.listId = (TextView) view.findViewById(R.id.list_id);
            this.listPassword = (TextView) view.findViewById(R.id.list_password);
        }
    }

}
