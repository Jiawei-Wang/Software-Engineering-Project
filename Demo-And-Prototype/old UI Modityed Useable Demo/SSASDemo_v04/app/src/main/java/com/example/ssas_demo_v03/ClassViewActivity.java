package com.example.ssas_demo_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClassViewActivity extends AppCompatActivity {

    int ClassId;

    private String Classid;
    private String Courseid;
    private String CourseName;
    private String DateTime;
    private String Classroom;


    private TextView mTextViewCourseName;
    private TextView mTextViewClassDateTime;
    private TextView mTextViewClassroom;

    private Button mButtonToViewCourse;
    private Button mButtonToEditCourse;

    private Intent intent;
    private SQLiteDatabase mDataBase;

    private AttendanceAdapter mAdapter;

    private DBHelper dbHelper;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_view);
        loadPage();
    }

    private void loadPage() {
        intent = getIntent();
        Classid = String.valueOf(intent.getIntExtra("ClassId", 0));
        Courseid = String.valueOf(intent.getIntExtra("CourseId", 0));
        CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        Cursor cursor0 = mDataBase.rawQuery("SELECT * FROM Class WHERE Classid = ?", new String[]{Classid});
        if (cursor0.moveToFirst()) {
            DateTime = cursor0.getString(cursor0.getColumnIndex(DataBaseContract.ClassEntry.COLUMN_DATETIME));
            Classroom = cursor0.getString(cursor0.getColumnIndex(DataBaseContract.ClassEntry.COLUMN_CLASSROOM));
        }


        mTextViewCourseName = findViewById(R.id.textview_class_course_name);
        mTextViewClassDateTime = findViewById(R.id.textview_class_datetime);
        mTextViewClassroom = findViewById(R.id.textview_class_classroom);

        mButtonToViewCourse = findViewById(R.id.button_to_view_class);

        mButtonToEditCourse = findViewById(R.id.button_to_edit_class);


        mButtonToViewCourse.setVisibility(View.INVISIBLE);

        mButtonToEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassViewActivity.this, ClassEditActivity.class);
                intent.putExtra("ClassId", Integer.parseInt(Classid));
                intent.putExtra("CourseId", Integer.parseInt(Courseid));
                intent.putExtra("CourseName", CourseName);
                intent.putExtra("DateTime", DateTime);
                intent.putExtra("Classroom", Classroom);
                startActivity(intent);
            }
        });


        mTextViewCourseName.setText(CourseName);
        mTextViewClassDateTime.setText(DateTime);
        mTextViewClassroom.setText(Classroom);


        RecyclerView recyclerView = findViewById(R.id.recyclerView_attendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Cursor cursor = getAllAttendanceOfClass(Classid);
        mAdapter = new AttendanceAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
        bottomNavigationView = findViewById(R.id.bnv);

        generate_navibar_listener(bottomNavigationView, ClassViewActivity.this);

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

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }

    private Cursor getAllAttendanceOfClass(String Classid) {
        String string = "SELECT Recordid,Student.Studentid,Student.StudentName,StudentClass.Classid,AttendanceStatus " +
                "FROM Student INNER JOIN StudentClass ON Student.Studentid = StudentClass.Studentid  " +
                "WHERE StudentClass.Classid = ?";
        return mDataBase.rawQuery(string,
                new String[]{Classid});
    }

}
