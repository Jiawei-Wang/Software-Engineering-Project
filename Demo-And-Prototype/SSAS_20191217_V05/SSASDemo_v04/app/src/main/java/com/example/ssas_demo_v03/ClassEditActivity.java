package com.example.ssas_demo_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.example.ssas_demo_v03.DataBaseConnectors.*;

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

    private Button mButtonAddEmptyClass;

    private Intent intent;
    private SQLiteDatabase mDataBase;

    private AttendanceEditorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_edit);

        intent = getIntent();


        Classid = String.valueOf(intent.getIntExtra("ClassId", 0));
        Courseid = String.valueOf(intent.getIntExtra("CourseId", 0));
        CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        DateTime = String.valueOf(intent.getStringExtra("DateTime"));
        Classroom = String.valueOf(intent.getStringExtra("Classroom"));

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

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClassInDataset();
            }
        });

        //TODO For all save button add jump back.


        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_attendance_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Cursor cursor = getAllAttendanceOfClass(Classid);
        mAdapter = new AttendanceEditorAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
    }


    private void loadData() {
        intent = getIntent();
        String Classid = String.valueOf(intent.getIntExtra("ClassId", 0));
        String Courseid = String.valueOf(intent.getIntExtra("CourseId", 0));
        String CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        String DateTime = String.valueOf(intent.getStringExtra("DateTime"));
        String Classroom = String.valueOf(intent.getStringExtra("Classroom"));

        mTextViewClassid.setText(Classid);
        mTextViewCourseName.setText(CourseName);
        mTextViewCourseid.setText(Courseid);
        mEditTextClassDateTime.setText(DateTime);
        mEditTextClassroom.setText(Classroom);
    }


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
            return;
        }

        String ClassDateTime = mEditTextClassDateTime.getText().toString();
        String Classroom = mEditTextClassroom.getText().toString();
        String Classid = mTextViewClassid.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.ClassEntry.COLUMN_CLASSROOM, Classroom);
        cv.put(DataBaseContract.ClassEntry.COLUMN_DATETIME, ClassDateTime);

        mDataBase.update(DataBaseContract.ClassEntry.TABLE_NAME,
                cv,
                DataBaseContract.ClassEntry.COLUMN_ID + " = ?",
                new String[]{Classid});

        mAdapter.swapCursor(getAllAttendanceOfClass(Classid));

        finish();


    }

}
