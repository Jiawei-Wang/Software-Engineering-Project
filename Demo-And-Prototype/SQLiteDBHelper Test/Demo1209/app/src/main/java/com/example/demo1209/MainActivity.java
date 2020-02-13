package com.example.demo1209;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    private SQLiteDatabase mDatabase;

    private StudentAdapter mAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DemoDBHelper dbHelper = new DemoDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        mAdapter = new StudentAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);
    }

    private Cursor getAllItems(){
        return mDatabase.query(
                StudentContract.StudentEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                StudentContract.StudentEntry.COLUMN_ID
        );
    }
}
