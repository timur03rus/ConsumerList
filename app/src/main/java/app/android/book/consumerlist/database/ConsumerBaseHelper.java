package app.android.book.consumerlist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.android.book.consumerlist.database.ConsumerDbSchema.ConsumerTable;

public class ConsumerBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "consumerBase.db";

    public ConsumerBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ConsumerTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ConsumerTable.Cols.UUID    + ", " +
                ConsumerTable.Cols.FIRST_NAME + ", " +
                ConsumerTable.Cols.LAST_NAME + ", " +
                ConsumerTable.Cols.PHONE + ", " +
                ConsumerTable.Cols.EMAIL +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
