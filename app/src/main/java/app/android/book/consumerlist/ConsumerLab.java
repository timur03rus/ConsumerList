package app.android.book.consumerlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.android.book.consumerlist.database.ConsumerBaseHelper;
import app.android.book.consumerlist.database.ConsumerCursorWrapper;
import app.android.book.consumerlist.database.ConsumerDbSchema.ConsumerTable;
import app.android.book.consumerlist.model.Consumer;

public class ConsumerLab {
    private static ConsumerLab sConsumerLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ConsumerLab getInstance(Context context) {
        if (sConsumerLab == null)
            sConsumerLab = new ConsumerLab(context);
        return sConsumerLab;
    }

    private ConsumerLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ConsumerBaseHelper(mContext).getWritableDatabase();

    }

    public void addConsumer(Consumer c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(ConsumerTable.NAME, null, values);
    }

    public List<Consumer> getConsumers() {
        List<Consumer> consumers = new ArrayList<>();

        ConsumerCursorWrapper cursor = queryConsumers(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                consumers.add(cursor.getConsumer());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return consumers;
    }

    public Consumer getConsumer(UUID id) {
        ConsumerCursorWrapper cursor = queryConsumers(
                ConsumerTable.Cols.UUID + "= ?",
                new  String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getConsumer();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Consumer consumer) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, consumer.getPhotoFilename());
    }

    public void deleteConsumer(UUID id) {
        mDatabase.delete(ConsumerTable.NAME,
                ConsumerTable.Cols.UUID + "= ?",
                new String[]{id.toString()}
        );
    }

    public void updateConsumer(Consumer consumer) {
        String uuidString = consumer.getId().toString();
        ContentValues values = getContentValues(consumer);

        mDatabase.update(ConsumerTable.NAME, values, ConsumerTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    private static ContentValues getContentValues(Consumer consumer) {
        ContentValues values = new ContentValues();
        values.put(ConsumerTable.Cols.UUID, consumer.getId().toString());
        values.put(ConsumerTable.Cols.FIRST_NAME, consumer.getFirstName());
        values.put(ConsumerTable.Cols.LAST_NAME, consumer.getLastName());
        values.put(ConsumerTable.Cols.PHONE, consumer.getPhone());
        values.put(ConsumerTable.Cols.EMAIL, consumer.getEmail());

        return values;
    }

    private ConsumerCursorWrapper queryConsumers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ConsumerTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ConsumerCursorWrapper(cursor);
    }
}
