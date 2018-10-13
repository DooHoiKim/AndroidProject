package com.dh.pro1822.pwassistance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PwEditActivity extends AppCompatActivity implements View.OnClickListener {

    private int pwId = -1;

    private EditText inName;
    private EditText inId;
    private EditText inPassword;
    private EditText inDescription;

    private SQLiteDatabase db;
    private Cursor cursor;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_manage);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.button_save);
        button.setOnClickListener(this);

        Button button2 = (Button) findViewById(R.id.button_del);
        button2.setOnClickListener(this);

        Button button3 = (Button) findViewById(R.id.button_init);
        button3.setOnClickListener(this);

        inName = (EditText) findViewById(R.id.input_name);
        inId = (EditText) findViewById(R.id.input_id);
        inPassword = (EditText) findViewById(R.id.input_password);
        inDescription = (EditText) findViewById(R.id.input_desc);

        setInitItem();


        LinearLayout layout = (LinearLayout) findViewById(R.id.edit_main);
        layout.setOnClickListener(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View v) {

        hideKeyboard();

        switch (v.getId()) {
            case R.id.button_save:
                saveItem();
                break;
            case R.id.button_del:
                deleteItem();
                break;
            case R.id.button_init:
                setInit();
                break;
            default:
                break;
        }
    }

    private void setInitItem() {
        Intent intent = getIntent();

        if (intent != null) {
            pwId = intent.getIntExtra(ContractDB.PwListEntry.COLUMN_ID, -1);

            if (pwId > -1) {
                searchItem();
            }
        }
    }

    private void searchItem() {
        PwDatabaseHelper pwDatabaseHelper = PwDatabaseHelper.getsInstance(this);

        try {
            db = pwDatabaseHelper.getReadableDatabase();
            cursor = db.query(ContractDB.PwListEntry.TABLE_NAME,
                    new String[]{ContractDB.PwListEntry.COLUMN_NAME,
                            ContractDB.PwListEntry.COLUMN_LOG_IN_ID,
                            ContractDB.PwListEntry.COLUMN_LOG_IN_PW,
                            ContractDB.PwListEntry.COLUMN_DESCRIPTION},
                    "_id = ?",
                    new String[]{Integer.toString(pwId)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                inName.setText(cursor.getString(0));
                inId.setText(cursor.getString(1));
                inPassword.setText(cursor.getString(2));
                inDescription.setText(cursor.getString(3));

                cursor.close();
                db.close();
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveItem() {

        String svName = inName.getText().toString();
        String svId = inId.getText().toString();
        String svPassword = inPassword.getText().toString();
        String svDescription = inDescription.getText().toString();

        if (svName.trim().length() == 0 || svId.trim().length() == 0 || svPassword.trim().length() == 0) {
            Toast.makeText(this, "입력 항목 누락", Toast.LENGTH_SHORT).show();
            return;
        }
        PwDatabaseHelper pwDatabaseHelper = PwDatabaseHelper.getsInstance(this);

        try {
            db = pwDatabaseHelper.getWritableDatabase();

            if (pwId == -1) {
                long newRowId = PwDatabaseHelper.insertPwList(db, svName, svId, svPassword, svDescription);

                if (newRowId == -1) {
                    Toast.makeText(this, "Database insert error!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Insert success", Toast.LENGTH_SHORT).show();
//                    setInit();
                    pwId = (int) newRowId;
                }
            } else {
                int count = PwDatabaseHelper.updatePwList(db, pwId, svName, svId, svPassword, svDescription);

                if (count == 0) {
                    Toast.makeText(this, "Database update error!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Update success", Toast.LENGTH_SHORT).show();
//                    setInit();
                }
            }
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteItem() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PwEditActivity.this);
        builder.setTitle("Delete Item");
        builder.setMessage("해당 항목을 삭제 하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PwDatabaseHelper pwDatabaseHelper = PwDatabaseHelper.getsInstance(PwEditActivity.this);
                db = pwDatabaseHelper.getWritableDatabase();

                int count = PwDatabaseHelper.deletePwList(db, pwId);

                if (count == 0) {
                    Toast.makeText(PwEditActivity.this, "Database delete error!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PwEditActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                    setInit();
                }
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }

    private void setInit() {
        inName.setText("");
        inId.setText("");
        inPassword.setText("");
        inDescription.setText("");

        pwId = -1;
    }

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(inName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(inId.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(inPassword.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(inDescription.getWindowToken(), 0);
    }
}
