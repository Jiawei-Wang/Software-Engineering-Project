package com.example.demo1209;

import android.provider.BaseColumns;

public class StudentContract {

    private  StudentContract(){};

    public static final class StudentEntry implements BaseColumns {
        public static final String TABLE_NAME = "Student";
        public static final String COLUMN_ID = "Studentid";
        public static final String COLUMN_NAME = "StudentName";
        public static final String COLUMN_EMAIL = "StudentEmail";
        public static final String COLUMN_STATUS = "StudentStatus";
        public static final String COLUMN_PIC = "StudentPic";
    }
}
