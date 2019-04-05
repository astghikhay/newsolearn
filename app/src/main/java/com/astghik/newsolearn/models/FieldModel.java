package com.astghik.newsolearn.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FieldModel implements Parcelable, Serializable {

    @Expose
    @SerializedName("headline")
    private String headline;
    @Expose
    @SerializedName("thumbnail")
    private String thumbnail;

    protected FieldModel(Parcel in) {
        headline = in.readString();
        thumbnail = in.readString();
    }

    public FieldModel(String headline, String thumbnail) {
        this.headline = headline;
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getHeadline() {
        return headline;
    }

    public static final Creator<FieldModel> CREATOR = new Creator<FieldModel>() {
        @Override
        public FieldModel createFromParcel(Parcel in) {
            return new FieldModel(in);
        }

        @Override
        public FieldModel[] newArray(int size) {
            return new FieldModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(headline);
        parcel.writeString(thumbnail);
    }
}
