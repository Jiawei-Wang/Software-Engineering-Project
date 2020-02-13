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
import android.widget.EditText;
import android.widget.TextView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.example.ssas_demo_v03.DataBaseConnectors.*;

public class ClassEditActivity extends AppCompatActivity {


    private TextView mTextViewClassid;
    private TextView mTextViewCourseName;
    private TextView mTextViewCourseid;

    private EditText mEditTextClassDateTime;
    private EditText mEditTextClassroom;

    private Button mButtonReset;
    private Button mButtonSave;

    private Intent intent;
    private SQLiteDatabase mDataBase;

    private AttendanceEditorAdapter mAdapter;

    // TODO Reset Structure of Class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_edit);

        intent = getIntent();
        String Classid = String.valueOf(intent.getIntExtra("ClassId",0));
        String Courseid = String.valueOf(intent.getIntExtra("CourseId",0));
        String CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        String DateTime = String.valueOf(intent.getStringExtra("DateTime"));
        String Classroom = String.valueOf(intent.getStringExtra("Classroom"));

        mTextViewClassid = findViewById(R.id.textview_class_classid);
        mTextViewCourseName = findViewById(R.id.textview_class_course_name);
        mTextViewCourseid = findViewById(R.id.textview_class_course_id);
        mEditTextClassDateTime = findViewById(R.id.editText_class_classroom);
        mEditTextClassroom = findViewById(R.id.editText_class_classroom);

        mTextViewClassid.setText(Classid);
        mTextViewCourseName.setText(Courseid);
        mTextViewCourseid.setText(CourseName);
        mEditTextClassDateTime.setText(DateTime);
        mEditTextClassroom.setText(Classroom);



        mButtonReset =findViewById(R.id.button_edit_class_reset);
        mButtonSave =findViewById(R.id.button_edit_class_save);



        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_attendance_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Cursor cursor = getAllAttendanceOfClass(Classid);
        mAdapter = new AttendanceEditorAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
    }

    //TODO ADD SAVE/EDIT DATA TO DATABASE





    private Cursor getAllAttendanceOfClass(String Classid) {
        String string ="SELECT Recordid,Student.Studentid,Student.StudentName,StudentClass.Classid,AttendanceStatus " +
                "FROM Student INNER JOIN StudentClass ON Student.Studentid = StudentClass.Studentid  " +
                "WHERE StudentClass.Classid = ?";
        return mDataBase.rawQuery(string,
                new String[]{Classid});
    }

}
