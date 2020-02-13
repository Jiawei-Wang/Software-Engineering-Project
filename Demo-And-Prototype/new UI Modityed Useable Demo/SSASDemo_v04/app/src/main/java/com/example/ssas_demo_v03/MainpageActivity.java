package com.example.ssas_demo_v03;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;

import com.example.ssas_demo_v03.DataBaseConnectors.*;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainpageActivity extends AppCompatActivity {

    public static final String LoginPreferences = "LoginInfo";


    private TextView mTextViewUsername;
    private TextView mTextViewUserid;
    private TextView mTextViewUserEmail;
    private TextView mTextViewUserDateCreated;
    private TextView mTextViewUserStatus;

    private Button mButtonToView;
    private Button mButtonToEdit;

    private SQLiteDatabase mDataBase;

    private CourseAdapter mAdapter;

    private DBHelper dbHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        loadPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }

    private void loadPage(){

        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);


        dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewUsername = findViewById(R.id.textView_user_username);
        mTextViewUserid = findViewById(R.id.textView_user_userid);
        mTextViewUserDateCreated = findViewById(R.id.textView_user_datecreated);
        mTextViewUserEmail = findViewById(R.id.textView_user_email);

        mTextViewUserStatus = findViewById(R.id.textView_user_status);

        //TODO Read Userpic from Database and display.

        mTextViewUsername.setText(sharedPreferences.getString("Username",""));
        mTextViewUserid.setText(sharedPreferences.getString("Userid",""));
        mTextViewUserDateCreated.setText(sharedPreferences.getString("UserDateCreated",""));
        mTextViewUserEmail.setText(sharedPreferences.getString("UserEmail",""));
        mTextViewUserStatus.setText(sharedPreferences.getString("AccountStatus",""));


        mButtonToView=findViewById(R.id.button_view_user);
        mButtonToEdit=findViewById(R.id.button_edit_user);

        mButtonToView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainpageActivity.this, UserViewCourseActivity.class));
            }
        });

        mButtonToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainpageActivity.this,UserEditActivity.class));
            }
        });




        RecyclerView recyclerView = findViewById(R.id.recyclerView_course_on_mainpage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseAdapter(this, getAllCourseOfUser(sharedPreferences.getString("Userid","")));
        recyclerView.setAdapter(mAdapter);


        //TODO Fix problem of search function.


        bottomNavigationView = findViewById(R.id.bnv);

        generate_navibar_listener(bottomNavigationView, MainpageActivity.this);

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



}
