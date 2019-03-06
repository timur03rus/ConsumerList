package app.android.book.consumerlist;

import android.content.Intent;
import android.support.v4.app.Fragment;

import app.android.book.consumerlist.model.Consumer;

public class ConsumerListActivity extends SingleFragmentActivity implements ConsumerListFragment.Callbacks, ConsumerFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new ConsumerListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onConsumerSelected(Consumer consumer) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = ConsumerPagerActivity.newIntent(this, consumer.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = ConsumerFragment.newInstance(consumer.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onConsumerUpdated(Consumer consumer) {
        ConsumerListFragment listFragment = (ConsumerListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateIU();
    }
}
