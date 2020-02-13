package com.example.ssas_demo_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.CourseAdapter;
import com.example.ssas_demo_v03.RecyclerviewAdapters.*;

import com.example.ssas_demo_v03.DataBaseConnectors.*;

public class UserViewCourseActivity extends AppCompatActivity {

    public static final String LoginPreferences = "LoginInfo";


    private TextView mTextViewUsername;
    private TextView mTextViewUserid;
    private TextView mTextViewUserEmail;
    private TextView mTextViewUserDateCreated;

    private Button mButtonToView;
    private Button mButtonToEdit;

    private SQLiteDatabase mDataBase;

    private CourseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_course);

        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);


        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewUsername = findViewById(R.id.textView_user_username);
        mTextViewUserid = findViewById(R.id.textView_user_userid);
        mTextViewUserDateCreated = findViewById(R.id.textView_user_datecreated);
        mTextViewUserEmail = findViewById(R.id.textView_user_email);

        //TODO Read Userpic from Database and display.

        mTextViewUsername.setText(sharedPreferences.getString("Username", ""));
        mTextViewUserid.setText(sharedPreferences.getString("Userid", ""));
        mTextViewUserDateCreated.setText(sharedPreferences.getString("UserDateCreated", ""));
        mTextViewUserEmail.setText(sharedPreferences.getString("UserEmail", ""));

        mButtonToView=findViewById(R.id.button_view_user);
        mButtonToEdit=findViewById(R.id.button_edit_user);

        mButtonToView.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_course_of_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        String Userid = sharedPreferences.getString("Userid", "");
        mAdapter = new CourseAdapter(this, getAllCourseOfUser(Userid));
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

}
