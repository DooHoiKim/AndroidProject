package com.dh.pro1822.pwassistance;

import android.provider.BaseColumns;

public final class ContractDB {
    private ContractDB() {};

    public static final String DB_NAME = "pwAssist";
    public static final String DB_EXPORT_DEST = "DB_BACKUP";

    public static class LoginEntry implements BaseColumns {
        public static final String TABLE_NAME = "LOGIN";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_LOG_IN_PW = "LOG_IN_PW";
    }

    public static class PwListEntry implements BaseColumns {
        public static final String TABLE_NAME = "PWLIST";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_LOG_IN_ID = "LOG_IN_ID";
        public static final String COLUMN_LOG_IN_PW = "LOG_IN_PW";
        public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    }
}
