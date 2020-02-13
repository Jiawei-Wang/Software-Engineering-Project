package com.example.ssas_demo_v03;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;

import com.example.ssas_demo_v03.DataBaseConnectors.*;

public class CoursePageActivity extends AppCompatActivity {


    public static final String LoginPreferences = "LoginInfo";

    private SQLiteDatabase mDataBase;

    private CourseAdapter mAdapter;

    private Button mButtonAddCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);


        mButtonAddCourse = findViewById(R.id.button_add_empty_course);
        mButtonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });
    }

    private Cursor getAllItems() {
        return mDataBase.query(
                DataBaseContract.CourseEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DataBaseContract.CourseEntry.COLUMN_ID
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


    private void addCourse() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(LoginPreferences, Context.MODE_PRIVATE);
        String Userid = sharedPreferences.getString("Userid", "0");
        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.CourseEntry.COLUMN_NAME, "Course Name");
        cv.put(DataBaseContract.CourseEntry.COLUMN_TEACHER_ID, Userid);
        mDataBase.insert(DataBaseContract.CourseEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDataBase.rawQuery("SELECT last_insert_rowid() FROM Course", null);
        int CourseId;
        if (cursor.moveToFirst()) {
            CourseId = cursor.getInt(0);
            Intent intent = new Intent(CoursePageActivity.this, CourseEditActivity.class);
            intent.putExtra("CourseId", CourseId);
            intent.putExtra("TeacherId", Integer.parseInt(Userid));
            intent.putExtra("CourseName", "");
            intent.putExtra("CourseStatus", "");
            intent.putExtra("ClassNumber", "");
            startActivity(intent);
        }


        return;


    }
}
