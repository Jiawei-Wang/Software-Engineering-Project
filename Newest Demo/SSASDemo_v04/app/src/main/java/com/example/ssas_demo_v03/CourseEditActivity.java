package com.example.ssas_demo_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssas_demo_v03.RecyclerviewAdapters.ClassAdapter;
import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CourseEditActivity extends AppCompatActivity {

    private int CourseId;

    private String Courseid;
    private String Teacherid;
    private String CourseName;
    private String CourseStatus;
    private String ClassNumber;


    private TextView mTextViewCourseid;
    private TextView mTextViewTeacherid;
    private TextView mEditTextCourseName;
    private TextView mEditTextCourseStatus;
    private TextView mEditTextClassNumber;

    private Button mButtonToReset;
    private Button mButtonToSave;
    private Button mButtonToDelete;

    private Button mButtonAddClass;

    private Button mButtonEnrollStudent;

    private AlertDialog mAlertDialogChooseStudent;

    private SQLiteDatabase mDataBase;

    private ClassAdapter mAdapter;

    private Intent intent;

    private DBHelper dbHelper;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

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
        CourseId = intent.getIntExtra("CourseId", 0);
        Courseid = String.valueOf(intent.getIntExtra("CourseId", 0));
        Teacherid = String.valueOf(intent.getIntExtra("TeacherId", 0));
        CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        CourseStatus = String.valueOf(intent.getStringExtra("CourseStatus"));
        ClassNumber = String.valueOf(intent.getIntExtra("ClassNumber", 0));
    }

    private void loadPage() {
        dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewCourseid = findViewById(R.id.textview_course_id);
        mTextViewTeacherid = findViewById(R.id.textview_course_teacher_id);
        mEditTextCourseName = findViewById(R.id.editText_course_name);
        mEditTextCourseStatus = findViewById(R.id.editText_course_status);
        mEditTextClassNumber = findViewById(R.id.editText_class_number);
        mButtonEnrollStudent = findViewById(R.id.button_enroll_student);


        mTextViewCourseid.setText(Courseid);
        mTextViewTeacherid.setText(Teacherid);
        mEditTextCourseName.setText(CourseName);
        mEditTextCourseStatus.setText(CourseStatus);
        mEditTextClassNumber.setText(ClassNumber);


        mButtonToReset = findViewById(R.id.button_edit_course_reset);
        mButtonToSave = findViewById(R.id.button_edit_course_save);
        mButtonToDelete = findViewById(R.id.button_delete_course);

        mButtonAddClass = findViewById(R.id.button_add_class_for_course);


        mButtonToReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage();
                Toast.makeText(getApplicationContext(),"Information Reset!",Toast.LENGTH_SHORT).show();
            }
        });

        mButtonToSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourseInDataset();
            }
        });

        mButtonToDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCourse();
            }
        });

        mButtonAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_class_for_course();
            }
        });

        mButtonEnrollStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_enroll_student();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView_class_of_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ClassAdapter(this, getAllClassOfCourse(Courseid));

        recyclerView.setAdapter(mAdapter);

        bottomNavigationView = findViewById(R.id.bnv);

        generate_navibar_listener(bottomNavigationView, CourseEditActivity.this);

        bottomNavigationView.getMenu().getItem(1).setChecked(true);
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

    private Cursor getAllClassOfCourse(String courseid) {
        String string = "SELECT Classid,C.Courseid,C.CourseName,DateTime,Classroom " +
                "From Class inner join Course C on Class.Courseid = C.Courseid " +
                "WHERE C.Courseid = ?";
        Cursor cursor = mDataBase.rawQuery(string, new String[]{courseid});
        return cursor;
    }

    private void updateCourseInDataset() {


        if (mEditTextCourseName.getText().toString().trim().length() == 0
                || mEditTextCourseStatus.getText().toString().trim().length() == 0
                || mEditTextClassNumber.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(),"Please Fill Info Correctly!",Toast.LENGTH_SHORT).show();
            return;
        }
        String coursename = mEditTextCourseName.getText().toString();
        String coursestatus = mEditTextCourseStatus.getText().toString();
        int classnumber = Integer.parseInt(mEditTextClassNumber.getText().toString());

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.CourseEntry.COLUMN_NAME, coursename);
        cv.put(DataBaseContract.CourseEntry.COLUMN_STATUS, coursestatus);
        cv.put(DataBaseContract.CourseEntry.COLUMN_NUMBER_OF_CLASS, classnumber);


        mDataBase.update(DataBaseContract.CourseEntry.TABLE_NAME,
                cv,
                DataBaseContract.CourseEntry.COLUMN_ID + " = ?",
                new String[]{Courseid});

        CourseName = coursename;
        CourseStatus = coursestatus;
        ClassNumber = String.valueOf(classnumber);

        mAdapter.swapCursor(getAllClassOfCourse(Courseid));

        Toast.makeText(getApplicationContext(),"Update Course Successfully!",Toast.LENGTH_SHORT).show();
        finish();


    }


    //TODO Add Delete Button for Class/Course/Student.

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
            Intent intent = new Intent(CourseEditActivity.this, ClassEditActivity.class);
            intent.putExtra("ClassId", Classid);
            intent.putExtra("CourseId", CourseId);
            intent.putExtra("CourseName", CourseName);
            intent.putExtra("DateTime", "");
            intent.putExtra("Classroom", "");
            startActivity(intent);
        }
        Toast.makeText(getApplicationContext(),"New Class Added for Course",Toast.LENGTH_SHORT).show();


        return;


    }

    //TODO Toast.makeText() print info of changing.
    private void removeCourse() {
        mDataBase.delete(DataBaseContract.CourseEntry.TABLE_NAME,
                DataBaseContract.CourseEntry.COLUMN_ID + "= ?",
                new String[]{Courseid});
        mDataBase.delete(DataBaseContract.ClassEntry.TABLE_NAME,
                DataBaseContract.ClassEntry.COLUMN_ID + "=?",
                new String[]{Courseid});

        mDataBase.delete(DataBaseContract.StudentCourseEntry.TABLE_NAME,
                DataBaseContract.StudentCourseEntry.COLUMN_COUSER_ID + "=?",
                new String[]{Courseid});
        Toast.makeText(getApplicationContext(),"Course Removed Successfully",Toast.LENGTH_SHORT).show();
        finish();
    }


    private void select_enroll_student() {
        List<String> studentsname = new ArrayList<>();
        List<String> studentsid = new ArrayList<>();
        Cursor cursor = getAllStudent();
        while (cursor.moveToNext()) {
            studentsname.add(cursor.getString(cursor.getColumnIndex(DataBaseContract.StudentEntry.COLUMN_NAME)));
            studentsid.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(DataBaseContract.StudentEntry.COLUMN_ID))));
        }
        final String[] choicelist = studentsname.toArray(new String[studentsname.size()]);
        final String[] idlist = studentsid.toArray(new String[studentsid.size()]);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Please choose Student");
        alertBuilder.setMultiChoiceItems(choicelist, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                update_student_for_course(idlist[i], isChecked);
            }
        });
        alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAlertDialogChooseStudent.dismiss();
                Toast.makeText(getApplicationContext(),"Enroll Student Successfully!",Toast.LENGTH_SHORT).show();
            }
        });


        mAlertDialogChooseStudent = alertBuilder.create();
        mAlertDialogChooseStudent.show();
    }

    private Cursor getAllStudent() {
        Cursor cursor = mDataBase.rawQuery("SELECT * FROM Student", null);
        return cursor;
    }

    private void update_student_for_course(String studentid, Boolean selected) {
        if (selected) {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseContract.StudentCourseEntry.COLUMN_COUSER_ID, Courseid);
            cv.put(DataBaseContract.StudentCourseEntry.COLUMN_STUDENT_ID, studentid);
            mDataBase.insert(DataBaseContract.StudentCourseEntry.TABLE_NAME, null, cv);
        } else mDataBase.execSQL("DELETE FROM StudentCourse WHERE Courseid = ? AND Studentid =?",new String[]{Courseid,studentid});
    }



}

