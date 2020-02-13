package com.example.ssas_demo_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private SQLiteDatabase mDataBase;

    private CourseAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);


        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewUserid = findViewById(R.id.textView_user_userid);
        mTextViewUserDateCreated = findViewById(R.id.textView_user_datecreated);

        mEditTextUsername = findViewById(R.id.EditText_user_username);
        mEditTextUserEmail = findViewById(R.id.EditText_user_email);
        mEditTextStatus = findViewById(R.id.EditText_user_status);


        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        String userid = sharedPreferences.getString("Userid", "");
        String userdatecreated = sharedPreferences.getString("UserDateCreated", "");
        String useremail = sharedPreferences.getString("UserEmail", "");
        String fullname = sharedPreferences.getString("FullName", "");
        String useraccountstatus = sharedPreferences.getString("AccountStatus", "");


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
                loadData();
            }
        });

        mButtonToSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInDataset();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView_course_of_user_in_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseAdapter(this, getAllCourseOfUser(userid));
        recyclerView.setAdapter(mAdapter);
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


    private void loadData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        String userid = sharedPreferences.getString("Userid", "");
        String userdatecreated = sharedPreferences.getString("UserDateCreated", "");
        String useremail = sharedPreferences.getString("UserEmail", "");
        String fullname = sharedPreferences.getString("FullName", "");
        String useraccountstatus = sharedPreferences.getString("AccountStatus", "");


        mTextViewUserid.setText(userid);
        mTextViewUserDateCreated.setText(userdatecreated);
        mEditTextUsername.setText(username);
        mEditTextUserEmail.setText(useremail);
        mEditTextStatus.setText(useraccountstatus);
    }


    private void updateUserInDataset() {


        if (mEditTextUsername.getText().toString().trim().length() == 0
                || mEditTextUserEmail.getText().toString().trim().length() == 0
                || mEditTextStatus.getText().toString().trim().length() == 0) {
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


    }
}

