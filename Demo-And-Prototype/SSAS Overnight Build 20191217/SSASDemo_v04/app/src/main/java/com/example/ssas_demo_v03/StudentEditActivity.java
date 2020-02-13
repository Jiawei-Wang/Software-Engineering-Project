package com.example.ssas_demo_v03;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.example.ssas_demo_v03.DataBaseConnectors.*;

public class StudentEditActivity extends AppCompatActivity {


    private TextView mTextViewStudentid;

    private EditText mTextViewStudentName;
    private EditText mTextViewStudentEmail;
    private EditText mTextViewStudentStatus;

    private Button mButtonToReset;
    private Button mButtonToSave;

    private SQLiteDatabase mDataBase;

    private CourseAdapter mAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit);




        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewStudentid = findViewById(R.id.textview_student_id);

        mTextViewStudentName = findViewById(R.id.editText_student_name);
        mTextViewStudentEmail = findViewById(R.id.editText_student_email);
        mTextViewStudentStatus = findViewById(R.id.editText_student_status);


        intent = getIntent();
        String Studentid = String.valueOf(intent.getIntExtra("StudentId",0));
        String StudentName = String.valueOf(intent.getStringExtra("StudentName"));
        String StudentEmail = String.valueOf(intent.getStringExtra("StudentEmail"));
        String StudentStatus = String.valueOf(intent.getStringExtra("StudentStatus"));



        mTextViewStudentid.setText(Studentid);
        mTextViewStudentName.setText(StudentName);
        mTextViewStudentEmail.setText(StudentEmail);
        mTextViewStudentStatus.setText(StudentStatus);

        mButtonToReset = findViewById(R.id.button_edit_student_reset);
        mButtonToSave = findViewById(R.id.button_edit_student_save);

        mButtonToReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        mButtonToSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudentInDataset();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView_course_of_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor =getAllCourseOfStudent(Studentid);

        mAdapter = new CourseAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
    }

    //TODO Add Close() for all query
    private Cursor getAllCourseOfStudent(String Studentid) {

        return mDataBase.rawQuery("SELECT * from Course INNER JOIN StudentCourse on Course.Courseid = StudentCourse.Courseid WHERE StudentCourse.Studentid = ?",
                new String[]{Studentid}
        );
    }


    private void loadData() {
        intent = getIntent();
        String Studentid = String.valueOf(intent.getIntExtra("StudentId",0));
        String StudentName = String.valueOf(intent.getStringExtra("StudentName"));
        String StudentEmail = String.valueOf(intent.getStringExtra("StudentEmail"));
        String StudentStatus = String.valueOf(intent.getStringExtra("StudentStatus"));



        mTextViewStudentid.setText(Studentid);
        mTextViewStudentName.setText(StudentName);
        mTextViewStudentEmail.setText(StudentEmail);
        mTextViewStudentStatus.setText(StudentStatus);
    }


    private void updateStudentInDataset() {


        if (mTextViewStudentName.getText().toString().trim().length() == 0
                || mTextViewStudentEmail.getText().toString().trim().length() == 0
                || mTextViewStudentStatus.getText().toString().trim().length() == 0) {
            return;
        }
        String StudentName = mTextViewStudentName.getText().toString();
        String StudentEmail = mTextViewStudentEmail.getText().toString();
        String StudentStatus = mTextViewStudentStatus.getText().toString();
        String Studentid = mTextViewStudentid.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.StudentEntry.COLUMN_NAME, StudentName);
        cv.put(DataBaseContract.StudentEntry.COLUMN_EMAIL, StudentEmail);
        cv.put(DataBaseContract.StudentEntry.COLUMN_STATUS, StudentStatus);


        mDataBase.update(DataBaseContract.StudentEntry.TABLE_NAME,
                cv,
                DataBaseContract.StudentEntry.COLUMN_ID + " = ?",
                new String[]{Studentid});

        mAdapter.swapCursor(getAllCourseOfStudent(Studentid));


    }
}
