package app.android.book.consumerlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.List;
import java.util.UUID;

import app.android.book.consumerlist.model.Consumer;

import static android.view.View.OnClickListener;

public class ConsumerFragment extends Fragment {

    private static final String ARG_CONSUMER_ID = "consumer_id";

    private static final int REQUEST_PHOTO = 2;
    private static final int REQUEST_PIN = 3;

    private Consumer mConsumer;
    private File mPhotoFile;
    private EditText mFirstNameET;
    private EditText mLastNameET;
    private EditText mPhoneET;
    private EditText mEmailET;


    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;
    private View v;

    public interface Callbacks {
        void onConsumerUpdated(Consumer consumer);
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

    public static ConsumerFragment newInstance(UUID consumerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONSUMER_ID, consumerId);

        ConsumerFragment fragment = new ConsumerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID consumerId = (UUID) getArguments().getSerializable(ARG_CONSUMER_ID);
        mConsumer = ConsumerLab.getInstance(getActivity()).getConsumer(consumerId);
        mPhotoFile = ConsumerLab.getInstance(getActivity()).getPhotoFile(mConsumer);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_consumer_list, menu);

        menu.findItem(R.id.new_consumer).setVisible(false);
        menu.findItem(R.id.show_subtitle).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_consumer:
                Intent intent = new Intent(getActivity(), PinActivity.class);
                startActivityForResult(intent, REQUEST_PIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_consumer, container, false);

        mPhotoButton = v.findViewById(R.id.consumer_camera);
        mPhotoView = v.findViewById(R.id.consumer_photo);

        mFirstNameET = v.findViewById(R.id.consumer_first_name);
        mFirstNameET.setText(mConsumer.getFirstName());
        mFirstNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mConsumer.setFirstName(s.toString());
                updateConsumer();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLastNameET = v.findViewById(R.id.consumer_last_name);
        mLastNameET.setText(mConsumer.getLastName());
        mLastNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mConsumer.setLastName(s.toString());
                updateConsumer();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPhoneET = v.findViewById(R.id.consumer_phone);
        mPhoneET.setText(mConsumer.getPhone());
        mPhoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mConsumer.setPhone(s.toString());
                updateConsumer();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEmailET = v.findViewById(R.id.consumer_email);
        mEmailET.setText(mConsumer.getEmail());
        mEmailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mConsumer.setEmail(s.toString());
                updateConsumer();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "app.android.book.criminalintent.FileProvider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int orientation = getResources().getConfiguration().orientation;

        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "app.android.book.criminalintent.FileProvider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateConsumer();
            updatePhotoView();
        } else if (requestCode == REQUEST_PIN) {
            ConsumerLab consumerLab = ConsumerLab.getInstance(getActivity());
            consumerLab.deleteConsumer(mConsumer.getId());
            updateConsumer();
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                getActivity().finish();
            } else {
                v.setVisibility(View.GONE);
            }

        }
    }

    private void updateConsumer() {
        ConsumerLab.getInstance(getActivity()).updateConsumer(mConsumer);
        mCallbacks.onConsumerUpdated(mConsumer);
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
            mPhotoView.setContentDescription(getString(R.string.consumer_photo_no_image_description));
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap((bitmap));
            mPhotoView.setContentDescription(getString(R.string.consumer_photo_image_description));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ConsumerLab.getInstance(getActivity()).updateConsumer(mConsumer);
    }
}
