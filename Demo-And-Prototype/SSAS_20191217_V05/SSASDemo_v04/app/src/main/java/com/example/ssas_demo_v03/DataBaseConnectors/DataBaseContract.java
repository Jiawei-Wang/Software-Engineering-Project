package com.example.ssas_demo_v03.DataBaseConnectors;

import android.provider.BaseColumns;
import android.provider.SearchRecentSuggestions;

public class DataBaseContract {
    public DataBaseContract() {
    }

    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_ID = "Userid";
        public static final String COLUMN_NAME = "Username";
        public static final String COLUMN_PASSWORD = "Password";
        public static final String COLUMN_FULLNAME = "FullName";
        public static final String COLUMN_STATUS = "AccountStatus";
        public static final String COLUMN_DATE_CREATED = "DateCreated";
        public static final String COLUMN_USERPIC = "UserPic";
        public static final String COLUMN_EMAIL = "Email";
    }

    public static final class StudentEntry implements BaseColumns{
        public static final String TABLE_NAME = "Student";
        public static final String COLUMN_ID = "Studentid";
        public static final String COLUMN_NAME = "StudentName";
        public static final String COLUMN_EMAIL = "StudentEmail";
        public static final String COLUMN_STATUS = "StudentStatus";
        public static final String COLUMN_PIC = "StudentPic";
    }

    public static final class CourseEntry implements BaseColumns{
        public static final String TABLE_NAME = "Course";
        public static final String COLUMN_ID = "Courseid";
        public static final String COLUMN_TEACHER_ID = "Teacherid";
        public static final String COLUMN_NAME = "CourseName";
        public static final String COLUMN_STATUS = "OfferStatus";
        public static final String COLUMN_NUMBER_OF_CLASS = "NumberOfClass";
        public static final String COLUMN_NUMBER_OF_STUDENT = "NumberOfStudent";
    }

    public static final class ClassEntry implements BaseColumns{
        public static final String TABLE_NAME = "Class";
        public static final String COLUMN_ID = "Classid";
        public static final String COLUMN_COURSE_ID = "Courseid";
        public static final String COLUMN_COURSE_NAME = "CourseName";
        public static final String COLUMN_DATETIME = "DateTime";
        public static final String COLUMN_CLASSROOM = "Classroom";
    }

    public static final class StudentClassEntry implements BaseColumns{
        public static final String TABLE_NAME = "StudentClass";
        public static final String COLUMN_ID = "Recordid";
        public static final String COLUMN_STUDENT_ID = "Studentid";
        public static final String COLUMN_CLASS_ID = "Classid";
        public static final String COLUMN_DATETIME = "RecordTime";
        public static final String COLUMN_STATUS = "AttendanceStatus";
    }
}
