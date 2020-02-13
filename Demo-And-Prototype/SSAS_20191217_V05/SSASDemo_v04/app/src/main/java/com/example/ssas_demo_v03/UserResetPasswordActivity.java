package com.example.ssas_demo_v03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.example.ssas_demo_v03.RecyclerviewAdapters.*;

public class UserResetPasswordActivity extends AppCompatActivity {

    private String username;
    private String userid;
    private String userdatecreated;
    private String useremail;
    private String fullname;
    private String useraccountstatus;


    public static final String LoginPreferences = "LoginInfo";


    private EditText mEditTextOldPassword;
    private EditText mEditTextNewPassword;
    private EditText mEditTextNewPasswordConfirm;

    private TextView mTextViewResult;

    private Button mButtonToSaveNewPassword;

    private SQLiteDatabase mDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reset_password);

        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();


        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);
        username = sharedPreferences.getString("Username","");


        mEditTextOldPassword=findViewById(R.id.editText_old_password);
        mEditTextNewPassword=findViewById(R.id.editText_new_password);
        mEditTextNewPasswordConfirm = findViewById(R.id.editText_new_password_confirm);
        mButtonToSaveNewPassword = findViewById(R.id.button_save_reset_password);
        mTextViewResult = findViewById(R.id.textView_reset_password_result);

        mButtonToSaveNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        if (!check_login_pass(username,mEditTextOldPassword.getText().toString())){
            mTextViewResult.setText("Password Error!");
            return;
        }else if(!mEditTextNewPasswordConfirm.getText().toString().equals(mEditTextNewPassword.getText().toString()))
        {
            mTextViewResult.setText("New Password Different!");
            return;
        }
        String newpassword = mEditTextNewPassword.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.UserEntry.COLUMN_PASSWORD, newpassword);


        mDataBase.update(DataBaseContract.UserEntry.TABLE_NAME,
                cv,
                DataBaseContract.UserEntry.COLUMN_NAME + " = ?",
                new String[]{username});

        finish();
        return;

    }


    private Boolean check_login_pass(String username, String password) {

        Cursor cursor = searchPassword(username);
        while (cursor.moveToNext()) {
            String answer = cursor.getString(cursor.getColumnIndex(DataBaseContract.UserEntry.COLUMN_PASSWORD));
            if (answer.equals(password)) {
                return true;
            }
        }
        return false;
    }

    private Cursor searchPassword(String Username) {
        return mDataBase.query(DataBaseContract.UserEntry.TABLE_NAME,
                new String[]{DataBaseContract.UserEntry.COLUMN_PASSWORD},
                DataBaseContract.UserEntry.COLUMN_NAME + " = ?",
                new String[]{Username},
                null,
                null,
                null);
    }
}
