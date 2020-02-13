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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.example.ssas_demo_v03.DataBaseConnectors.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClassEditActivity extends AppCompatActivity {

    int CourseId;

    private String Classid;
    private String Courseid;
    private String CourseName;
    private String DateTime;
    private String Classroom;


    private TextView mTextViewClassid;
    private TextView mTextViewCourseName;
    private TextView mTextViewCourseid;

    private EditText mEditTextClassDateTime;
    private EditText mEditTextClassroom;

    private Button mButtonReset;
    private Button mButtonSave;
    private Button mButtonToDelete;

    private Button mButtonAddEmptyClass;

    private Intent intent;
    private SQLiteDatabase mDataBase;

    private AttendanceEditorAdapter mAdapter;
    private DBHelper dbHelper;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_edit);

        loadData();
        loadPage();


        //TODO For all save button add jump back.

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }

    private void loadData(){
        intent = getIntent();


        Classid = String.valueOf(intent.getIntExtra("ClassId", 0));
        Courseid = String.valueOf(intent.getIntExtra("CourseId", 0));
        CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        DateTime = String.valueOf(intent.getStringExtra("DateTime"));
        Classroom = String.valueOf(intent.getStringExtra("Classroom"));
    }
    private void loadPage() {

        mTextViewClassid = findViewById(R.id.textview_class_classid);
        mTextViewCourseName = findViewById(R.id.textview_class_course_name);
        mTextViewCourseid = findViewById(R.id.textview_class_course_id);
        mEditTextClassDateTime = findViewById(R.id.editText_class_datetime);
        mEditTextClassroom = findViewById(R.id.editText_class_classroom);

        mTextViewClassid.setText(Classid);
        mTextViewCourseName.setText(CourseName);
        mTextViewCourseid.setText(Courseid);
        mEditTextClassDateTime.setText(DateTime);
        mEditTextClassroom.setText(Classroom);

        mButtonAddEmptyClass = findViewById(R.id.button_add_empty_class);


        mButtonReset = findViewById(R.id.button_edit_class_reset);
        mButtonSave = findViewById(R.id.button_edit_class_save);

        mButtonToDelete = findViewById(R.id.button_delete_class);

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage();
                Toast.makeText(getApplicationContext(),"Information Reset!",Toast.LENGTH_SHORT).show();
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClassInDataset();
            }
        });

        mButtonToDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClass();
            }
        });

        dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_attendance_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Cursor cursor = getAllAttendanceOfClass(Classid);
        mAdapter = new AttendanceEditorAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);

        bottomNavigationView = findViewById(R.id.bnv);

        generate_navibar_listener(bottomNavigationView, ClassEditActivity.this);

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
                        break;
                    case R.id.bnv_course:
                        startActivity(new Intent(activity, CoursePageActivity.class));
                        break;
                    case R.id.bnv_student:
                        startActivity(new Intent(activity, StudentPageActivity.class));
                        break;
                    case R.id.bnv_statistics:
                        startActivity(new Intent(activity, StatisticsPageActivity.class));
                        break;
                }
                return true;
            }
        });
    };


    private Cursor getAllAttendanceOfClass(String Classid) {
        String string = "SELECT Recordid,Student.Studentid,Student.StudentName,StudentClass.Classid,AttendanceStatus " +
                "FROM Student INNER JOIN StudentClass ON Student.Studentid = StudentClass.Studentid  " +
                "WHERE StudentClass.Classid = ?";
        return mDataBase.rawQuery(string,
                new String[]{Classid});
    }


    private void updateClassInDataset() {


        if (mEditTextClassDateTime.getText().toString().trim().length() == 0
                || mEditTextClassroom.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(),"Please Fill Info Correctly!",Toast.LENGTH_SHORT).show();
            return;
        }

        String classdatetime = mEditTextClassDateTime.getText().toString();
        String classroom = mEditTextClassroom.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.ClassEntry.COLUMN_CLASSROOM, classroom);
        cv.put(DataBaseContract.ClassEntry.COLUMN_DATETIME, classdatetime);

        mDataBase.update(DataBaseContract.ClassEntry.TABLE_NAME,
                cv,
                DataBaseContract.ClassEntry.COLUMN_ID + " = ?",
                new String[]{Classid});

        Classroom=classroom;
        DateTime=classdatetime;
        Toast.makeText(getApplicationContext(),"Update Class Successfully!",Toast.LENGTH_SHORT).show();
        finish();


    }


    private void removeClass() {
        mDataBase.delete(DataBaseContract.ClassEntry.TABLE_NAME,
                DataBaseContract.ClassEntry.COLUMN_ID + "= ?",
                new String[]{Classid});
        mDataBase.delete(DataBaseContract.StudentClassEntry.TABLE_NAME,
                DataBaseContract.ClassEntry.COLUMN_ID + "=?",
                new String[]{Classid});
        Toast.makeText(getApplicationContext(),"Remove Class Successfully!",Toast.LENGTH_SHORT).show();
        finish();
    }

}
