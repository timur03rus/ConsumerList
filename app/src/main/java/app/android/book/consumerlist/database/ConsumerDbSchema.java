package app.android.book.consumerlist.database;

public class ConsumerDbSchema {
    public static final class ConsumerTable {
        public static final String NAME = "consumers";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String FIRST_NAME = "first_name";
            public static final String LAST_NAME = "last_name";
            public static final String PHONE = "phone";
            public static final String EMAIL = "email";
        }
    }
}
