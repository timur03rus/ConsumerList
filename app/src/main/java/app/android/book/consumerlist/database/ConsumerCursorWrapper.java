package app.android.book.consumerlist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import app.android.book.consumerlist.database.ConsumerDbSchema.ConsumerTable;
import app.android.book.consumerlist.model.Consumer;

public class ConsumerCursorWrapper extends CursorWrapper {

    public ConsumerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Consumer getConsumer() {
        String uuidString = getString(getColumnIndex(ConsumerTable.Cols.UUID));
        String firstName = getString(getColumnIndex(ConsumerTable.Cols.FIRST_NAME));
        String last_name = getString(getColumnIndex(ConsumerTable.Cols.LAST_NAME));
        String phone = getString(getColumnIndex(ConsumerTable.Cols.PHONE));
        String email = getString(getColumnIndex(ConsumerTable.Cols.EMAIL));

        Consumer consumer = new Consumer(UUID.fromString(uuidString));
        consumer.setFirstName(firstName);
        consumer.setLastName(last_name);
        consumer.setPhone(phone);
        consumer.setEmail(email);

        return consumer;
    }
}
