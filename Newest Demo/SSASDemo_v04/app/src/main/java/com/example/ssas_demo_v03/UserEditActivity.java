package com.example.ssas_demo_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.example.ssas_demo_v03.DataBaseConnectors.*;

public class UserEditActivity extends AppCompatActivity {

    public static final String LoginPreferences = "LoginInfo";


    private TextView mTextViewUserid;
    private TextView mTextViewUserDateCreated;

    private EditText mEditTextUsername;
    private EditText mEditTextUserEmail;
    private EditText mEditTextStatus;

    private Button mButtonToReset;
    private Button mButtonToSave;

    private Button mButtonToEditPassword;

    private SQLiteDatabase mDataBase;

    private CourseAdapter mAdapter;
    private DBHelper dbHelper;

    private String username;
    private String userid;
    private String userdatecreated;
    private String useremail;
    private String fullname;
    private String useraccountstatus;


    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }


    private void loadPage() {
        dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewUserid = findViewById(R.id.textView_user_userid);
        mTextViewUserDateCreated = findViewById(R.id.textView_user_datecreated);

        mEditTextUsername = findViewById(R.id.EditText_user_username);
        mEditTextUserEmail = findViewById(R.id.EditText_user_email);
        mEditTextStatus = findViewById(R.id.EditText_user_status);

        mButtonToEditPassword = findViewById(R.id.button_edit_password);

        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);
        username = sharedPreferences.getString("Username", "");
        userid = sharedPreferences.getString("Userid", "");
        userdatecreated = sharedPreferences.getString("UserDateCreated", "");
        useremail = sharedPreferences.getString("UserEmail", "");
        fullname = sharedPreferences.getString("FullName", "");
        useraccountstatus = sharedPreferences.getString("AccountStatus", "");


        mTextViewUserid.setText(userid);
        mTextViewUserDateCreated.setText(userdatecreated);
        mEditTextUsername.setText(username);
        mEditTextUserEmail.setText(useremail);
        mEditTextStatus.setText(useraccountstatus);


        mButtonToReset = findViewById(R.id.button_edit_user_reset);
        mButtonToSave = findViewById(R.id.button_edit_user_save);

        mButtonToReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage();
                Toast.makeText(getApplicationContext(),"Information Reset!",Toast.LENGTH_SHORT).show();
            }
        });

        mButtonToSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInDataset();
            }
        });

        mButtonToEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserEditActivity.this, UserResetPasswordActivity.class));
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView_course_of_user_in_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseAdapter(this, getAllCourseOfUser(userid));
        recyclerView.setAdapter(mAdapter);
        bottomNavigationView = findViewById(R.id.bnv);

        generate_navibar_listener(bottomNavigationView, UserEditActivity.this);
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


    private Cursor getAllCourseOfUser(String Userid) {
        return mDataBase.query(
                DataBaseContract.CourseEntry.TABLE_NAME,
                null,
                DataBaseContract.CourseEntry.COLUMN_TEACHER_ID + " = ?",
                new String[]{Userid},
                null,
                null,
                DataBaseContract.CourseEntry.COLUMN_ID
        );
    }


    private void updateUserInDataset() {


        if (mEditTextUsername.getText().toString().trim().length() == 0
                || mEditTextUserEmail.getText().toString().trim().length() == 0
                || mEditTextStatus.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(),"Please Fill Info Correctly!",Toast.LENGTH_SHORT).show();
            return;
        }
        String UserName = mEditTextUsername.getText().toString();
        String UserEmail = mEditTextUserEmail.getText().toString();
        String UserStatus = mEditTextStatus.getText().toString();
        String Userid = mTextViewUserid.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.UserEntry.COLUMN_NAME, UserName);
        cv.put(DataBaseContract.UserEntry.COLUMN_EMAIL, UserEmail);
        cv.put(DataBaseContract.UserEntry.COLUMN_STATUS, UserStatus);


        mDataBase.update(DataBaseContract.UserEntry.TABLE_NAME,
                cv,
                DataBaseContract.UserEntry.COLUMN_ID + " = ?",
                new String[]{Userid});

        mAdapter.swapCursor(getAllCourseOfUser(Userid));

        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Username", UserName);
        editor.putString("UserEmail", UserEmail);
        editor.putString("AccountStatus", UserStatus);

        editor.apply();

        Toast.makeText(getApplicationContext(),"Update UserInfo Successfully!",Toast.LENGTH_SHORT).show();
        finish();

    }
}

