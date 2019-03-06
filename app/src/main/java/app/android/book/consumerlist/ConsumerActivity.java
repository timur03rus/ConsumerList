package app.android.book.consumerlist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class ConsumerActivity extends SingleFragmentActivity {

    private static final String EXTRA_CONSUMER_ID = "app.android.book.consumerlist.consumer_id";

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, ConsumerActivity.class);
        intent.putExtra(EXTRA_CONSUMER_ID, crimeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CONSUMER_ID);
        return ConsumerFragment.newInstance(crimeId);
    }
}
