package app.android.book.consumerlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import app.android.book.consumerlist.model.Consumer;

public class ConsumerListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mConsumerRecyclerView;
    private ConsumerAdapter mAdapter;
    private View mLayout;
    private Button mAddConsumerButton;

    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onConsumerSelected(Consumer consumer);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_consumer_list, container, false);

        mConsumerRecyclerView = v.findViewById(R.id.consumer_recycler_view);
        mConsumerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLayout = v.findViewById(R.id.empty_view);
        mAddConsumerButton = v.findViewById(R.id.add_consumer_button);
        mAddConsumerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consumer consumer = new Consumer();
                ConsumerLab.getInstance(getActivity()).addConsumer(consumer);
                Intent intent = ConsumerPagerActivity.newIntent(getActivity(), consumer.getId());
                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateIU();

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateIU();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_consumer_list, menu);

        menu.findItem(R.id.delete_consumer).setVisible(false);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_consumer:
                Consumer consumer = new Consumer();
                ConsumerLab.getInstance(getActivity()).addConsumer(consumer);
                updateIU();
                mCallbacks.onConsumerSelected(consumer);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        ConsumerLab consumerLab = ConsumerLab.getInstance(getActivity());
        int consumerCount = consumerLab.getConsumers().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, consumerCount, consumerCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateIU() {
        ConsumerLab consumerLab = ConsumerLab.getInstance(getActivity());
        List<Consumer> consumers = consumerLab.getConsumers();

        mLayout.setVisibility(consumers.size() > 0 ? View.GONE : View.VISIBLE);

        if (mAdapter == null) {
            mAdapter = new ConsumerAdapter(consumers);
            mConsumerRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setConsumers(consumers);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class ConsumerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mFirstNameTextView;
        private TextView mLastNameTextView;
        private TextView mPhoneTextView;
        private TextView mEmailTextView;

        private Consumer mConsumer;


        public void bind(Consumer consumer) {
            mConsumer = consumer;

            mFirstNameTextView.setText(mConsumer.getFirstName());
            mLastNameTextView.setText(mConsumer.getLastName());
            mPhoneTextView.setText(mConsumer.getPhone());
            mEmailTextView.setText(mConsumer.getEmail());

        }

        public ConsumerHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_consumer, parent, false));

            itemView.setOnClickListener(this);

            mFirstNameTextView = itemView.findViewById(R.id.consumer_first_name);
            mLastNameTextView = itemView.findViewById(R.id.consumer_last_name);
            mPhoneTextView = itemView.findViewById(R.id.consumer_phone);
            mEmailTextView = itemView.findViewById(R.id.consumer_email);

            mLastNameTextView.setSingleLine(false);
            mEmailTextView.setSingleLine(false);
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onConsumerSelected(mConsumer);
        }
    }

    private class ConsumerAdapter extends RecyclerView.Adapter<ConsumerHolder> {
        private List<Consumer> mConsumers;

        public ConsumerAdapter(List<Consumer> consumers) {
            mConsumers = consumers;
        }

        @NonNull
        @Override
        public ConsumerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ConsumerHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ConsumerHolder consumerHolder, int i) {
            Consumer consumer = mConsumers.get(i);
            consumerHolder.bind(consumer);
        }

        @Override
        public int getItemCount() {
            return mConsumers.size();
        }

        public void setConsumers(List<Consumer> consumers) {
            mConsumers = consumers;
        }
    }
}
