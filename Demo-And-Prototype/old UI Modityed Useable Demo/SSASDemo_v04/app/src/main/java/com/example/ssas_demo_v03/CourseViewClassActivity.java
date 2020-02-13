package com.example.ssas_demo_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CourseViewClassActivity extends AppCompatActivity {

    private int CourseId;

    private String Courseid;
    private String Teacherid;
    private String CourseName;
    private String CourseStatus;
    private String ClassNumber;


    private TextView mTextViewCourseid;
    private TextView mTextViewTeacherid;
    private TextView mTextViewCourseName;
    private TextView mTextViewCourseStatus;
    private TextView mTextViewClassNumber;

    private Button mButtonToViewCourse;
    private Button mButtonToEditCourse;

    private Button mButtonAddEmptyClass;

    private SQLiteDatabase mDataBase;

    private ClassAdapter mAdapter;

    private Intent intent;

    private DBHelper dbHelper;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view_class);
        loadPage();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }

    private void loadPage() {
        intent = getIntent();

        CourseId = intent.getIntExtra("CourseId", 0);
        Courseid = String.valueOf(intent.getIntExtra("CourseId", 0));

        dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        Cursor cursor0 = mDataBase.rawQuery("SELECT * FROM Course WHERE Courseid = ?",new String[]{Courseid});
        if(cursor0.moveToFirst()){
            Teacherid = String.valueOf(cursor0.getInt(cursor0.getColumnIndex(DataBaseContract.CourseEntry.COLUMN_TEACHER_ID)));
            CourseName = cursor0.getString(cursor0.getColumnIndex(DataBaseContract.CourseEntry.COLUMN_NAME));
            CourseStatus = cursor0.getString(cursor0.getColumnIndex(DataBaseContract.CourseEntry.COLUMN_STATUS));
            ClassNumber = String.valueOf(cursor0.getInt(cursor0.getColumnIndex(DataBaseContract.CourseEntry.COLUMN_NUMBER_OF_CLASS)));
        }


        mTextViewCourseid = findViewById(R.id.textview_course_id);
        mTextViewTeacherid = findViewById(R.id.textview_course_teacher_id);
        mTextViewCourseName = findViewById(R.id.textview_course_name);
        mTextViewCourseStatus = findViewById(R.id.textview_course_status);
        mTextViewClassNumber = findViewById(R.id.textview_class_number);

        mButtonAddEmptyClass = findViewById(R.id.button_add_empty_class);

        mTextViewCourseid.setText(Courseid);
        mTextViewTeacherid.setText(Teacherid);
        mTextViewCourseName.setText(CourseName);
        mTextViewCourseStatus.setText(CourseStatus);
        mTextViewClassNumber.setText(ClassNumber);


        mButtonToEditCourse = findViewById(R.id.button_edit_course);
        mButtonToViewCourse = findViewById(R.id.button_view_course);

        mButtonToViewCourse.setVisibility(View.INVISIBLE);

        mButtonAddEmptyClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_class_for_course();
            }
        });
        mButtonToEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump_to_edit_course();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView_class_of_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ClassAdapter(this, getAllClassOfCourse(Courseid));

        recyclerView.setAdapter(mAdapter);
        bottomNavigationView = findViewById(R.id.bnv);

        generate_navibar_listener(bottomNavigationView, CourseViewClassActivity.this);

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

    public void jump_to_edit_course() {

        Intent intent = new Intent(CourseViewClassActivity.this, CourseEditActivity.class);
        intent.putExtra("CourseId", Integer.parseInt(Courseid));
        intent.putExtra("TeacherId", Integer.parseInt(Teacherid));
        intent.putExtra("CourseName", CourseName);
        intent.putExtra("CourseStatus", CourseStatus);
        intent.putExtra("ClassNumber", Integer.parseInt(ClassNumber));
        startActivity(intent);


    }

    private Cursor getAllClassOfCourse(String courseid) {
        String string = "SELECT Classid,C.Courseid,C.CourseName,DateTime,Classroom " +
                "From Class inner join Course C on Class.Courseid = C.Courseid " +
                "WHERE C.Courseid = ?";
        Cursor cursor = mDataBase.rawQuery(string, new String[]{courseid});
        return cursor;
    }

    private void add_new_class_for_course() {
        int Classid;
        String CourseName = "";
        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.ClassEntry.COLUMN_COURSE_ID, CourseId);


        Cursor cursor2 = mDataBase.rawQuery("SELECT CourseName FROM Course WHERE CourseId = ?", new String[]{String.valueOf(CourseId)});
        if (cursor2.moveToFirst()) {
            CourseName = cursor2.getString(0);
        }
        mDataBase.insert(DataBaseContract.ClassEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDataBase.rawQuery("SELECT last_insert_rowid() FROM Class", null);

        if (cursor.moveToFirst()) {
            Classid = cursor.getInt(0);


            ContentValues cv2 = new ContentValues();
            Cursor cursor3 = mDataBase.rawQuery("SELECT Studentid FROM StudentCourse SC WHERE Courseid =?", new String[]{Courseid});
            while (cursor3.moveToNext()) {
                cv2.put(DataBaseContract.StudentClassEntry.COLUMN_CLASS_ID, Classid);
                cv2.put(DataBaseContract.StudentClassEntry.COLUMN_STUDENT_ID, cursor3.getInt(0));
                cv2.put(DataBaseContract.StudentClassEntry.COLUMN_STATUS, "Not Attend");
                mDataBase.insert(DataBaseContract.StudentClassEntry.TABLE_NAME, null, cv2);
            }
            Intent intent = new Intent(CourseViewClassActivity.this, ClassEditActivity.class);
            intent.putExtra("ClassId", Classid);
            intent.putExtra("CourseId", CourseId);
            intent.putExtra("CourseName", CourseName);
            intent.putExtra("DateTime", "");
            intent.putExtra("Classroom", "");
            startActivity(intent);
        }


        return;


    }
}
