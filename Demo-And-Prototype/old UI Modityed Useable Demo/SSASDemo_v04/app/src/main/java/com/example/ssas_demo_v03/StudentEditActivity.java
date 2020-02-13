package com.example.ssas_demo_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentEditActivity extends AppCompatActivity {


    private String Studentid;
    private String StudentName;
    private String StudentEmail;
    private String StudentStatus;
    private TextView mTextViewStudentid;

    private EditText mTextViewStudentName;
    private EditText mTextViewStudentEmail;
    private EditText mTextViewStudentStatus;

    private Button mButtonToReset;
    private Button mButtonToSave;
    private Button mButtonToDelete;

    private SQLiteDatabase mDataBase;

    private CourseAdapter mAdapter;
    private Intent intent;
    private DBHelper dbHelper;


    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit);
        loadData();
        loadPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }

    private void loadData() {
        intent = getIntent();
        Studentid = String.valueOf(intent.getIntExtra("StudentId", 0));
        StudentName = String.valueOf(intent.getStringExtra("StudentName"));
        StudentEmail = String.valueOf(intent.getStringExtra("StudentEmail"));
        StudentStatus = String.valueOf(intent.getStringExtra("StudentStatus"));

    }

    private void loadPage() {


        dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewStudentid = findViewById(R.id.textview_student_id);

        mTextViewStudentName = findViewById(R.id.editText_student_name);
        mTextViewStudentEmail = findViewById(R.id.editText_student_email);
        mTextViewStudentStatus = findViewById(R.id.editText_student_status);


        mTextViewStudentid.setText(Studentid);
        mTextViewStudentName.setText(StudentName);
        mTextViewStudentEmail.setText(StudentEmail);
        mTextViewStudentStatus.setText(StudentStatus);

        mButtonToReset = findViewById(R.id.button_edit_student_reset);
        mButtonToSave = findViewById(R.id.button_edit_student_save);
        mButtonToDelete = findViewById(R.id.button_delete_student);

        mButtonToReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage();
            }
        });

        mButtonToSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudentInDataset();
            }
        });

        mButtonToDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeStudent();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView_course_of_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cursor cursor = getAllCourseOfStudent(Studentid);

        mAdapter = new CourseAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
        bottomNavigationView = findViewById(R.id.bnv);

        generate_navibar_listener(bottomNavigationView, StudentEditActivity.this);

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

    //TODO Add Close() for all query
    private Cursor getAllCourseOfStudent(String Studentid) {

        return mDataBase.rawQuery("SELECT * from Course INNER JOIN StudentCourse on Course.Courseid = StudentCourse.Courseid WHERE StudentCourse.Studentid = ?",
                new String[]{Studentid}
        );
    }


    private void updateStudentInDataset() {


        if (mTextViewStudentName.getText().toString().trim().length() == 0
                || mTextViewStudentEmail.getText().toString().trim().length() == 0
                || mTextViewStudentStatus.getText().toString().trim().length() == 0) {
            return;
        }
        String studentname = mTextViewStudentName.getText().toString();
        String studentemail = mTextViewStudentEmail.getText().toString();
        String studentstatus = mTextViewStudentStatus.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.StudentEntry.COLUMN_NAME, studentname);
        cv.put(DataBaseContract.StudentEntry.COLUMN_EMAIL, studentemail);
        cv.put(DataBaseContract.StudentEntry.COLUMN_STATUS, studentstatus);


        mDataBase.update(DataBaseContract.StudentEntry.TABLE_NAME,
                cv,
                DataBaseContract.StudentEntry.COLUMN_ID + " = ?",
                new String[]{Studentid});

        mAdapter.swapCursor(getAllCourseOfStudent(Studentid));

        StudentName = studentname;
        StudentEmail = studentemail;
        StudentStatus = studentstatus;
        finish();

    }

    private void removeStudent() {
        mDataBase.delete(DataBaseContract.StudentEntry.TABLE_NAME,
                DataBaseContract.StudentEntry.COLUMN_ID + "= ?",
                new String[]{Studentid});
        mDataBase.delete(DataBaseContract.StudentClassEntry.TABLE_NAME,
                DataBaseContract.StudentClassEntry.COLUMN_STUDENT_ID + "=?",
                new String[]{Studentid});

        mDataBase.delete(DataBaseContract.StudentCourseEntry.TABLE_NAME,
                DataBaseContract.StudentCourseEntry.COLUMN_STUDENT_ID + "=?",
                new String[]{Studentid});
        finish();
    }
}
