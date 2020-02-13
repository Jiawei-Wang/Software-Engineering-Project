package com.example.logindemo1211;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainpageActivity extends AppCompatActivity {

    public static final String LoginPreferences = "LoginInfo";


    private TextView mTextViewUsername;
    private TextView mTextViewUserid;
    private TextView mTextViewUserEmail;
    private TextView mTextViewUserDateCreated;

    private SQLiteDatabase mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);


        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewUsername = findViewById(R.id.textView_user_username);
        mTextViewUserid = findViewById(R.id.textView_user_userid);
        mTextViewUserDateCreated = findViewById(R.id.textView_user_datecreated);
        mTextViewUserEmail = findViewById(R.id.editText_email);

        //TODO Read Userpic from Database and display.

        mTextViewUsername.setText(sharedPreferences.getString("Username",""));
        mTextViewUserid.setText(sharedPreferences.getString("Userid",""));
        mTextViewUserDateCreated.setText(sharedPreferences.getString("UserDateCreated",""));


        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv);
        final TextView textView =findViewById(R.id.textView_bnv);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    //TODO Add Jumping to target page function for navigation bar.
                    case R.id.bnv_main:
                        textView.setText("To Main");
                        break;
                    case R.id.bnv_course:
                        textView.setText("To Student");
                        break;
                    case R.id.bnv_student:
                        textView.setText("To Student");
                        break;
                    case R.id.bnv_statistics:
                        textView.setText("To Statistics");
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.getMenu().getItem(0).setChecked(true);


    }


}
