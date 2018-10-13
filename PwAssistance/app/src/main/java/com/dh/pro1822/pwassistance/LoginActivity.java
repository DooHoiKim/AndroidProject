package com.dh.pro1822.pwassistance;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button btn[] = new Button[12];
    private ImageView imageView[] = new ImageView[6];

    private int curPwInputPosition = -1;
    private String inputPassword = "";

    public static final int PW_INCREASE = 0;
    public static final int PW_DECREASE = 1;
    public static final int MAX_PW_LENGTH = 6 - 1;
    public static final int MAX_NUM_KEYPAD_COUNT = 10;

    private ArrayList<String> list = new ArrayList<>();

    private SQLiteDatabase db;
    private Cursor loginPwCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn[0] = (Button) findViewById(R.id.button1);
        btn[1] = (Button) findViewById(R.id.button2);
        btn[2] = (Button) findViewById(R.id.button3);
        btn[3] = (Button) findViewById(R.id.button4);
        btn[4] = (Button) findViewById(R.id.button5);
        btn[5] = (Button) findViewById(R.id.button6);
        btn[6] = (Button) findViewById(R.id.button7);
        btn[7] = (Button) findViewById(R.id.button8);
        btn[8] = (Button) findViewById(R.id.button9);
        btn[9] = (Button) findViewById(R.id.button0);
        btn[10] = (Button) findViewById(R.id.button_reset);
        btn[11] = (Button) findViewById(R.id.button_del);

        for (int i = 0; i < btn.length; i++) {
            btn[i].setOnClickListener(this);
        }

        imageView[0] = (ImageView) findViewById(R.id.pw_image1);
        imageView[1] = (ImageView) findViewById(R.id.pw_image2);
        imageView[2] = (ImageView) findViewById(R.id.pw_image3);
        imageView[3] = (ImageView) findViewById(R.id.pw_image4);
        imageView[4] = (ImageView) findViewById(R.id.pw_image5);
        imageView[5] = (ImageView) findViewById(R.id.pw_image6);

        for (int i = 0; i < MAX_NUM_KEYPAD_COUNT; i++) {
            list.add(new Integer(i).toString());
        }
        reOrderNumberPad();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_del: {
                if (curPwInputPosition >= 0) {
                    setPwImage(PW_DECREASE);
                    makePassword(PW_DECREASE, null);
                    curPwInputPosition--;
                }
                break;
            }
            case R.id.button_reset:
                reOrderNumberPad();
                break;
            default:
                if (curPwInputPosition < MAX_PW_LENGTH) {
                    curPwInputPosition++;
                    setPwImage(PW_INCREASE);

                    String text = ((Button) findViewById(v.getId())).getText().toString();
                    makePassword(PW_INCREASE, text);
                }
        }

        /*
        if (curPwInputPosition >= MAX_PW_LENGTH) {
            Toast toast = Toast.makeText(this, "Password Input End!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this, "Password Input : " + inputPassword, Toast.LENGTH_SHORT);
            toast.show();
        }
        */
        if (curPwInputPosition == MAX_PW_LENGTH) {
            if (inputPassword.equals(getLoginPw())) {
                /*
                Toast toast = Toast.makeText(this, "Password Input : " + inputPassword, Toast.LENGTH_SHORT);
                toast.show();
                */
                initPassword();
                Intent intent = new Intent(LoginActivity.this, PwListActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void setPwImage(int flag) {

        Drawable drawable;

        if (flag == PW_INCREASE) {
            drawable = getResources().getDrawable(R.drawable.pw_circle_fill);
        } else {
            drawable = getResources().getDrawable(R.drawable.pw_circle_grey);
        }

        imageView[curPwInputPosition].setImageDrawable(drawable);
    }

    private void makePassword(int flag, String text) {
        if (flag == PW_INCREASE) {
            inputPassword = inputPassword.concat(text);
        } else {
            int pwLength = inputPassword.length();
            if (pwLength > 0) {
                inputPassword = inputPassword.substring(0, pwLength - 1);
            } else {
                inputPassword = "";
            }
        }
    }

    private void reOrderNumberPad() {
        initPassword();

        long seed = System.nanoTime();
        Collections.shuffle(list, new Random(seed));

        for (int i = 0; i < MAX_NUM_KEYPAD_COUNT; i++) {
            btn[i].setText(list.get(i));
        }
    }

    private void initPassword() {
        curPwInputPosition = -1;
        inputPassword = "";

        Drawable drawable = getResources().getDrawable(R.drawable.pw_circle_grey);

        for (int i = 0; i <= MAX_PW_LENGTH; i++) {
            imageView[i].setImageDrawable(drawable);
        }
    }

    private String getLoginPw() {
//        SQLiteOpenHelper pwDatabaseHelper = new PwDatabaseHelper(this);
        PwDatabaseHelper pwDatabaseHelper = PwDatabaseHelper.getsInstance(this);

        String loginPw = "";
        try {
            db = pwDatabaseHelper.getReadableDatabase();
            loginPwCursor = db.query(ContractDB.LoginEntry.TABLE_NAME,
                    new String[]{ContractDB.LoginEntry.COLUMN_LOG_IN_PW},
                    "_id = 1",
                    null, null, null, null);
            //Move to the first record in the Cursor
            if (loginPwCursor.moveToFirst()) {
                loginPw = loginPwCursor.getString(0);
            } else {
                loginPw = "";
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            loginPw = "";
        }
        loginPwCursor.close();
        db.close();
        return loginPw;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginPwCursor != null) {
            loginPwCursor.close();
        }
        if (db != null) {
            db.close();
        }
    }
}
