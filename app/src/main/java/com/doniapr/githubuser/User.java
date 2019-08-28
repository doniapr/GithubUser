package com.doniapr.githubuser;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {
    int id;
    String name, avatar, url;

    public User(JSONObject object) {
        try {
            int id = object.getInt("id");
            String name = object.getString("login");
            String avatar = object.getString("avatar_url");
            String url = object.getString("url");

            this.id = id;
            this.name = name;
            this.avatar = avatar;
            this.url = url;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.url);
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.avatar = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
