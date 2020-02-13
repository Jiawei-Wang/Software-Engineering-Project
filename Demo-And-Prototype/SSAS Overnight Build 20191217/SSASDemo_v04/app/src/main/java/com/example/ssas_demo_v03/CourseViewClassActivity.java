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

import com.example.ssas_demo_v03.RecyclerviewAdapters.*;
import com.example.ssas_demo_v03.DataBaseConnectors.*;

public class CourseViewClassActivity extends AppCompatActivity {

    private int CourseId;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view_class);
        intent = getIntent();
        CourseId = intent.getIntExtra("CourseId",0);

        String Courseid = String.valueOf(intent.getIntExtra("CourseId",0));
        String Teacherid = String.valueOf(intent.getIntExtra("TeacherId",0));
        String CourseName = String.valueOf(intent.getStringExtra("CourseName"));
        String CourseStatus = String.valueOf(intent.getStringExtra("CourseStatus"));
        String ClassNumber = String.valueOf(intent.getIntExtra("ClassNumber",0));


        DBHelper dbHelper = new DBHelper(this);
        mDataBase = dbHelper.getWritableDatabase();

        mTextViewCourseid = findViewById(R.id.textview_course_id);
        mTextViewTeacherid = findViewById(R.id.textview_course_teacher_id);
        mTextViewCourseName = findViewById(R.id.textview_course_name);
        mTextViewCourseStatus =findViewById(R.id.textview_course_status);
        mTextViewClassNumber =findViewById(R.id.textview_class_number);

        mButtonAddEmptyClass =findViewById(R.id.button_add_empty_class);

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
                addClass();
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView_class_of_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ClassAdapter(this, getAllClassOfCourse(Courseid));

        recyclerView.setAdapter(mAdapter);

    }

    private Cursor getAllClassOfCourse(String courseid){
        String string = "SELECT Classid,C.Courseid,C.CourseName,DateTime,Classroom " +
                "From Class inner join Course C on Class.Courseid = C.Courseid " +
                "WHERE C.Courseid = ?";
        Cursor cursor = mDataBase.rawQuery(string,new String[]{courseid});
        return cursor;
    }

    private void addClass() {
        int Classid;
        String CourseName = "";
        ContentValues cv = new ContentValues();
        cv.put(DataBaseContract.ClassEntry.COLUMN_COURSE_ID,CourseId);


        Cursor cursor2 = mDataBase.rawQuery("SELECT CourseName FROM Course WHERE CourseId = ?",new String[]{String.valueOf(CourseId)});
        if (cursor2.moveToFirst()) {
            CourseName = cursor2.getString(0);
        }
        mDataBase.insert(DataBaseContract.ClassEntry.TABLE_NAME,null,cv);
        Cursor cursor = mDataBase.rawQuery("SELECT last_insert_rowid() FROM Class",null);

        if (cursor.moveToFirst()) {
            Classid = cursor.getInt(0);
            Intent intent = new Intent(CourseViewClassActivity.this, ClassEditActivity.class);
            intent.putExtra("ClassId", Classid);
            intent.putExtra("CourseId", CourseId);
            intent.putExtra("CourseName", CourseName);
            intent.putExtra("DateTime", "");
            intent.putExtra("Classroom","");
            startActivity(intent);
        }


        return;



    }
}
