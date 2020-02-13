package com.example.ssas_demo_v03;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;

import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.example.ssas_demo_v03.RecyclerviewAdapters.StudentAdapter;

public class StatisticsPageActivity extends AppCompatActivity {



    private SQLiteDatabase mDatabase;

    private StudentAdapter mAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_page);

        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_student);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        mAdapter = new StudentAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);
    }

    private Cursor getAllItems(){
        return mDatabase.query(
                DataBaseContract.StudentEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DataBaseContract.StudentEntry.COLUMN_ID
        );
    }

}
