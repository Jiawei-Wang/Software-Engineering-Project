package com.example.logindemo1211;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Properties;

import javax.mail.*;

public class RegisterActivity extends AppCompatActivity {


    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private EditText mEditTextPasswordConfirm;
    private EditText mEditTextEmail;

    private TextView mTextViewResult;

    private SQLiteDatabase mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mEditTextUsername = findViewById(R.id.editText_username_register);
        mEditTextPassword = findViewById(R.id.editText_password_register);

        mEditTextPasswordConfirm = findViewById(R.id.editText_password_register_confirm);

        mEditTextEmail = findViewById(R.id.editText_email);

        mTextViewResult = findViewById(R.id.textView_register_result);

        Button button_register = findViewById(R.id.button_register);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


    }


    private void register() {
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();
        String password_confirm = mEditTextPasswordConfirm.getText().toString();

        String email = mEditTextEmail.getText().toString();

        if (!password.equals(password_confirm)){
            mTextViewResult.setText("Password entered differently!");
            return;
        }
        Cursor cursor = searchUsername(username);
        if (cursor.getCount() > 0) {
            mTextViewResult.setText("Username Existed!");
            return;
        } else {
            ContentValues cv = new ContentValues();
            cv.put(UserContract.UserEntry.COLUMN_NAME,username);
            cv.put(UserContract.UserEntry.COLUMN_PASSWORD,password);
            mDataBase.insert(UserContract.UserEntry.TABLE_NAME,null,cv);

            sendEmail(email);
            startActivity(new Intent (RegisterActivity.this, MainActivity.class));

            return;

        }
    }


    //TODO ADD SENDEMAIL Function for Register.
    private void sendEmail(String email){
        return;
    }

    private Cursor searchUsername(String Username) {
        return mDataBase.query(UserContract.UserEntry.TABLE_NAME,
                new String[]{UserContract.UserEntry.COLUMN_NAME},
                UserContract.UserEntry.COLUMN_NAME + " = ?",
                new String[]{Username},
                null,
                null,
                null);
    }
}