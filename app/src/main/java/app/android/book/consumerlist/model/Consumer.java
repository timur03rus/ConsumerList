package app.android.book.consumerlist.model;

import java.util.UUID;

public class Consumer {

    private UUID mId;
    private String mFirstName;
    private String mLastName;
    private String mPhone;
    private String mEmail;

    public Consumer() {
        this(UUID.randomUUID());
    }

    public Consumer(UUID id) {
        mId = id;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public UUID getId() {
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }


    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
