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

import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.example.ssas_demo_v03.RecyclerviewAdapters.*;

public class ClassViewActivity extends AppCompatActivity {


    private TextView mTextViewCourseName;
    private TextView mTextViewClassDateTime;
    private TextView mTextViewClassroom;

    private Button mButtonToViewCourse;
    private Button mButtonToEditCourse;

    private Intent intent;
    private SQLiteDatabase mDataBase;

    private AttendanceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_view);

        intent = getIntent();
        String Classid = String.valueOf(intent.getIntExtra("ClassId",0));
        String Courseid = String.valueOf(intent.getIntExtra("CourseId",0));
        String CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        String DateTime = String.valueOf(intent.getStringExtra("DateTime"));
        String Classroom = String.valueOf(intent.getStringExtra("Classroom"));


        mTextViewCourseName = findViewById(R.id.textview_class_course_name);
        mTextViewClassDateTime =findViewById(R.id.textview_class_datetime);
        mTextViewClassroom =findViewById(R.id.textview_class_classroom);

        mButtonToViewCourse =findViewById(R.id.button_to_view_class);

        mButtonToViewCourse.setVisibility(View.INVISIBLE);


        mTextViewCourseName.setText(CourseName);
        mTextViewClassDateTime.setText(DateTime);
        mTextViewClassroom.setText(Classroom);


        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_attendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Cursor cursor = getAllAttendanceOfClass(Classid);
        mAdapter = new AttendanceAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
    }






    private Cursor getAllAttendanceOfClass(String Classid) {
        String string ="SELECT Recordid,Student.Studentid,Student.StudentName,StudentClass.Classid,AttendanceStatus " +
                "FROM Student INNER JOIN StudentClass ON Student.Studentid = StudentClass.Studentid  " +
                "WHERE StudentClass.Classid = ?";
        return mDataBase.rawQuery(string,
                new String[]{Classid});
    }

}
