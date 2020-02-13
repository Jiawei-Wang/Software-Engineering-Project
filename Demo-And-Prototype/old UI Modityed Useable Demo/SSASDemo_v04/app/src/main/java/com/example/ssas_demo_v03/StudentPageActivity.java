package com.example.ssas_demo_v03;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.StudentAdapter;
import com.example.ssas_demo_v03.RecyclerviewAdapters.*;

import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentPageActivity extends AppCompatActivity {

    private SQLiteDatabase mDataBase;

    private StudentAdapter mAdapter;

    private Button mButtonAddStudent;
    private DBHelper dbHelper;


    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);
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
        RecyclerView recyclerView = findViewById(R.id.recyclerView_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StudentAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);


        mButtonAddStudent = findViewById(R.id.button_add_empty_student);
        mButtonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });
        bottomNavigationView = findViewById(R.id.bnv);

        generate_navibar_listener(bottomNavigationView, StudentPageActivity.this);

        bottomNavigationView.getMenu().getItem(2).setChecked(true);

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

    private void addStudent() {
        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.StudentEntry.COLUMN_NAME, "Student Name");
        mDataBase.insert(DataBaseContract.StudentEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDataBase.rawQuery("SELECT last_insert_rowid() FROM Student", null);
        int Studentid;
        if (cursor.moveToFirst()) {
            Studentid = cursor.getInt(0);
            Intent intent = new Intent(StudentPageActivity.this, StudentEditActivity.class);
            intent.putExtra("StudentId", Studentid);
            intent.putExtra("StudentName", "Student Name");
            intent.putExtra("StudentEmail", "");
            intent.putExtra("StudentStatus", "");
            startActivity(intent);
        }


        return;


    }


    private Cursor getAllItems() {
        return mDataBase.query(
                DataBaseContract.StudentEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DataBaseContract.StudentEntry.COLUMN_ID
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
}