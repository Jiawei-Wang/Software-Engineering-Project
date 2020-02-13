package com.example.ssas_demo_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.example.ssas_demo_v03.DataBaseConnectors.*;

public class CourseViewClassActivity extends AppCompatActivity {

    private TextView mTextViewCourseid;
    private TextView mTextViewTeacherid;
    private TextView mTextViewCourseName;
    private TextView mTextViewCourseStatus;
    private TextView mTextViewClassNumber;

    private Button mButtonToViewCourse;
    private Button mButtonToEditCourse;

    private SQLiteDatabase mDataBase;

    private ClassAdapter mAdapter;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view_class);

        intent = getIntent();
        String Courseid = String.valueOf(intent.getIntExtra("CourseId",0));
        String Teacherid = String.valueOf(intent.getIntExtra("TeacherId",0));
        String CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        String CourseStatus = String.valueOf(intent.getStringExtra("CourseStatus"));
        String ClassNumber = String.valueOf(intent.getIntExtra("ClassNumber",0));


        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewCourseid = findViewById(R.id.textview_course_id);
        mTextViewTeacherid = findViewById(R.id.textview_course_teacher_id);
        mTextViewCourseName = findViewById(R.id.textview_course_name);
        mTextViewCourseStatus =findViewById(R.id.textview_course_status);
        mTextViewClassNumber =findViewById(R.id.textview_class_number);

        mTextViewCourseid.setText(Courseid);
        mTextViewTeacherid.setText(Teacherid);
        mTextViewCourseName.setText(CourseName);
        mTextViewCourseStatus.setText(CourseStatus);
        mTextViewClassNumber.setText(ClassNumber);


        mButtonToEditCourse = findViewById(R.id.button_edit_course);
        mButtonToViewCourse = findViewById(R.id.button_view_course);

        mButtonToViewCourse.setVisibility(View.INVISIBLE);


        RecyclerView recyclerView = findViewById(R.id.recyclerView_class_of_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ClassAdapter(this, getAllClassOfCourse(Courseid));

        recyclerView.setAdapter(mAdapter);

    }

    private Cursor getAllClassOfCourse(String courseid){
        String string = "SELECT Classid,C.Courseid,C.CourseName,DateTime,Classroom " +
                "From Class inner join Course C on Class.Courseid = C.Courseid " +
                "WHERE C.Courseid = ?";
        Cursor cursor = mDataBase.rawQuery(string,new String[]{courseid});
        return cursor;
    }
}
