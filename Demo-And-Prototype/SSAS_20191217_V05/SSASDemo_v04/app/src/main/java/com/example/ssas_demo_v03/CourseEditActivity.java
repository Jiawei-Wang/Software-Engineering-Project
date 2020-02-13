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
import android.widget.TextView;

import com.example.ssas_demo_v03.RecyclerviewAdapters.ClassAdapter;
import com.example.ssas_demo_v03.DataBaseConnectors.*;

import java.lang.reflect.Array;

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

    private Button mButtonAddClass;

    private SQLiteDatabase mDataBase;

    private ClassAdapter mAdapter;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);



        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewCourseid = findViewById(R.id.textview_course_id);
        mTextViewTeacherid = findViewById(R.id.textview_course_teacher_id);
        mEditTextCourseName = findViewById(R.id.editText_course_name);
        mEditTextCourseStatus = findViewById(R.id.editText_course_status);
        mEditTextClassNumber = findViewById(R.id.editText_class_number);

        intent = getIntent();
        CourseId  =intent.getIntExtra("CourseId",0);
        Courseid = String.valueOf(intent.getIntExtra("CourseId", 0));
        Teacherid = String.valueOf(intent.getIntExtra("TeacherId", 0));
        CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        CourseStatus = String.valueOf(intent.getStringExtra("CourseStatus"));
        ClassNumber = String.valueOf(intent.getIntExtra("ClassNumber", 0));

        mTextViewCourseid.setText(Courseid);
        mTextViewTeacherid.setText(Teacherid);
        mEditTextCourseName.setText(CourseName);
        mEditTextCourseStatus.setText(CourseStatus);
        mEditTextClassNumber.setText(ClassNumber);


        mButtonToReset = findViewById(R.id.button_edit_course_reset);
        mButtonToSave = findViewById(R.id.button_edit_course_save);

        mButtonAddClass = findViewById(R.id.button_add_class_for_course);

        mButtonToReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        mButtonToSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourseInDataset();
            }
        });

        mButtonAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_class_for_course();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView_class_of_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ClassAdapter(this, getAllClassOfCourse(Courseid));

        recyclerView.setAdapter(mAdapter);

    }

    private void loadData(){
        intent = getIntent();
        String Courseid = String.valueOf(intent.getIntExtra("CourseId", 0));
        String Teacherid = String.valueOf(intent.getIntExtra("TeacherId", 0));
        String CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        String CourseStatus = String.valueOf(intent.getStringExtra("CourseStatus"));
        String ClassNumber = String.valueOf(intent.getIntExtra("ClassNumber", 0));

        mTextViewCourseid.setText(Courseid);
        mTextViewTeacherid.setText(Teacherid);
        mEditTextCourseName.setText(CourseName);
        mEditTextCourseStatus.setText(CourseStatus);
        mEditTextClassNumber.setText(ClassNumber);
    }

    private Cursor getAllClassOfCourse(String courseid){
        String string = "SELECT Classid,C.Courseid,C.CourseName,DateTime,Classroom " +
                "From Class inner join Course C on Class.Courseid = C.Courseid " +
                "WHERE C.Courseid = ?";
        Cursor cursor = mDataBase.rawQuery(string,new String[]{courseid});
        return cursor;
    }

    private void updateCourseInDataset() {


        if (mEditTextCourseName.getText().toString().trim().length() == 0
                || mEditTextCourseStatus.getText().toString().trim().length() == 0
                || mEditTextClassNumber.getText().toString().trim().length() == 0) {
            return;
        }
        String CourseName = mEditTextCourseName.getText().toString();
        String CourseStatus = mEditTextCourseStatus.getText().toString();
        int ClassNumber = Integer.parseInt(mEditTextClassNumber.getText().toString());
        String Courseid = mTextViewCourseid.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.CourseEntry.COLUMN_NAME,CourseName);
        cv.put(DataBaseContract.CourseEntry.COLUMN_STATUS,CourseStatus);
        cv.put(DataBaseContract.CourseEntry.COLUMN_NUMBER_OF_CLASS,ClassNumber);


        mDataBase.update(DataBaseContract.CourseEntry.TABLE_NAME,
                cv,
                DataBaseContract.CourseEntry.COLUMN_ID+" = ?",
                new String[]{Courseid});

        mAdapter.swapCursor(getAllClassOfCourse(Courseid));

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
            Cursor cursor3 =mDataBase.rawQuery("SELECT Studentid FROM StudentCourse SC WHERE Courseid =?",new String[]{Courseid});
            while(cursor3.moveToNext()){
                cv2.put(DataBaseContract.StudentClassEntry.COLUMN_CLASS_ID,Classid);
                cv2.put(DataBaseContract.StudentClassEntry.COLUMN_STUDENT_ID,cursor3.getInt(0));
                cv2.put(DataBaseContract.StudentClassEntry.COLUMN_STATUS,"Not Attend");
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


        return;


    }
}
