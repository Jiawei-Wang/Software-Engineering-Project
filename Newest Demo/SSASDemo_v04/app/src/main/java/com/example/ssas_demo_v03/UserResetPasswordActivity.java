package com.example.ssas_demo_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private DBHelper dbHelper;


    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reset_password);

        loadPage();


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }

    private void loadPage() {
        dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();


        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);
        username = sharedPreferences.getString("Username", "");


        mEditTextOldPassword = findViewById(R.id.editText_old_password);
        mEditTextNewPassword = findViewById(R.id.editText_new_password);
        mEditTextNewPasswordConfirm = findViewById(R.id.editText_new_password_confirm);
        mButtonToSaveNewPassword = findViewById(R.id.button_save_reset_password);
        mTextViewResult = findViewById(R.id.textView_reset_password_result);

        mButtonToSaveNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        bottomNavigationView = findViewById(R.id.bnv);

        generate_navibar_listener(bottomNavigationView, UserResetPasswordActivity.this);

        bottomNavigationView.getMenu().getItem(0).setChecked(true);

    }
    private void generate_navibar_listener(BottomNavigationView bnv, final Activity activity){
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    //TODO Add Jumping to target page function for navigation bar.
                    //TODO Find a universal way of setting jumping function for the navibar.
                    case R.id.bnv_main:
                        startActivity(new Intent(activity, MainpageActivity.class));
                        finish();
                        break;
                    case R.id.bnv_course:
                        startActivity(new Intent(activity, CoursePageActivity.class));
                        finish();
                        break;
                    case R.id.bnv_student:
                        startActivity(new Intent(activity, StudentPageActivity.class));
                        finish();
                        break;
                    case R.id.bnv_statistics:
                        startActivity(new Intent(activity, StatisticsPageActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });
    }


    private void resetPassword() {
        if (!check_login_pass(username, mEditTextOldPassword.getText().toString())) {
            mTextViewResult.setText("Password Error!");

            Toast.makeText(getApplicationContext(),"Old Password Error!",Toast.LENGTH_SHORT).show();
            return;
        } else if (!mEditTextNewPasswordConfirm.getText().toString().equals(mEditTextNewPassword.getText().toString())) {
            mTextViewResult.setText("New Password Different!");
            Toast.makeText(getApplicationContext(),"New Password Entered Differently!",Toast.LENGTH_SHORT).show();
            return;
        }
        String newpassword = mEditTextNewPassword.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.UserEntry.COLUMN_PASSWORD, newpassword);


        mDataBase.update(DataBaseContract.UserEntry.TABLE_NAME,
                cv,
                DataBaseContract.UserEntry.COLUMN_NAME + " = ?",
                new String[]{username});
        Toast.makeText(getApplicationContext(),"Reset Password Successfully!",Toast.LENGTH_SHORT).show();
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
