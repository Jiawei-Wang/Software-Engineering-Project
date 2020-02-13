package com.example.logindemo1211;

import android.provider.BaseColumns;

public class UserContract {
    private UserContract() {
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
    }
}
