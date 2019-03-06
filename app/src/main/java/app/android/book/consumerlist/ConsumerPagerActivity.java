package app.android.book.consumerlist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

import app.android.book.consumerlist.model.Consumer;

public class ConsumerPagerActivity extends AppCompatActivity implements View.OnClickListener, ConsumerFragment.Callbacks {
    private static final String EXTRA_CONSUMER_ID = "app.android.book.consumerlist.consumer_id";

    private ViewPager mViewPager;
    private List<Consumer> mConsumers;

    private Button mFirstPageButton, mLastPageButton;

    public static Intent newIntent(Context packageContext, UUID consumerId) {
        Intent intent = new Intent(packageContext, ConsumerPagerActivity.class);
        intent.putExtra(EXTRA_CONSUMER_ID, consumerId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_pager);

        final UUID consumerId = (UUID) getIntent().getSerializableExtra(EXTRA_CONSUMER_ID);

        mViewPager = findViewById(R.id.consumer_view_pager);
        mFirstPageButton = findViewById(R.id.first_page_button);
        mLastPageButton = findViewById(R.id.last_page_button);

        mConsumers = ConsumerLab.getInstance(this).getConsumers();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Consumer consumer = mConsumers.get(i);

                if (mViewPager.getCurrentItem() == 0)
                    mFirstPageButton.setEnabled(false);
                else
                    mFirstPageButton.setEnabled(true);

                if (mViewPager.getCurrentItem() == (mConsumers.size() - 1))
                    mLastPageButton.setEnabled(false);
                else
                    mLastPageButton.setEnabled(true);

                return ConsumerFragment.newInstance(consumer.getId());
            }

            @Override
            public int getCount() {
                return mConsumers.size();
            }

        });

        for (int i = 0; i < mConsumers.size(); i++) {
            if (mConsumers.get(i).getId().equals(consumerId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mFirstPageButton.setOnClickListener(this);
        mLastPageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_page_button:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.last_page_button:
                mViewPager.setCurrentItem(mConsumers.size() - 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConsumerUpdated(Consumer consumer) {

    }
}
