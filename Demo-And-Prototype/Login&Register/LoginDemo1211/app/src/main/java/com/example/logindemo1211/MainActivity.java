package com.example.logindemo1211;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    public static final String LoginPreferences = "LoginInfo";

    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private TextView mTextViewResult;


    private SQLiteDatabase mDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mEditTextUsername = findViewById(R.id.editText_username);
        mEditTextPassword = findViewById(R.id.editText_password);

        mTextViewResult = findViewById(R.id.textView_result);

        Button buttonLogin = findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });


        Button button_jump_to_register = findViewById(R.id.button_to_register);
        button_jump_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });


    }

    private void login() {

        SharedPreferences sharedPreferences = super.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();
        if (check_login_pass(username,password)) {



            String userid;
            //String useremail;
            String userdatecreated;

            //TODO ADD EMAILCOLUMN IN DATABASE
            Cursor cursor = searchAllInfoOfUser(username,password);

            cursor.moveToFirst();
            userid=String.valueOf(cursor.getInt(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_ID)));
            //useremail=cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.))
            userdatecreated = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_DATE_CREATED));

            editor.putString("Username",username);
            editor.putString("Userid",userid);
            editor.putString("UserDateCreated",userdatecreated);
            editor.apply();


            startActivity(new Intent(MainActivity.this, MainpageActivity.class));
        } else {
            mTextViewResult.setText("Please Retry");
        }
    }


    private Boolean check_login_pass(String username, String password) {

        Cursor cursor = searchPassword(username);
        while (cursor.moveToNext()) {
            String answer = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD));
            if (answer.equals(password)) {
                return true;
            }
        }
        return false;
    }

    private Cursor searchPassword(String Username) {
        return mDataBase.query(UserContract.UserEntry.TABLE_NAME,
                new String[]{UserContract.UserEntry.COLUMN_PASSWORD},
                UserContract.UserEntry.COLUMN_NAME + " = ?",
                new String[]{Username},
                null,
                null,
                null);
    }

    private Cursor searchAllInfoOfUser(String Username, String Password) {
        return mDataBase.query(UserContract.UserEntry.TABLE_NAME,
                null,
                UserContract.UserEntry.COLUMN_NAME + " = ? AND " + UserContract.UserEntry.COLUMN_PASSWORD + " = ?",
                new String[]{Username, Password},
                null,
                null,
                null);
    }
}
