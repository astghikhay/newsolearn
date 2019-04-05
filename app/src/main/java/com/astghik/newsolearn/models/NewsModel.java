package com.astghik.newsolearn.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsModel extends ItemModel implements Parcelable, Serializable {

    @Expose
    @SerializedName("fields")
    private FieldModel field;

    public NewsModel() {
    }

    public FieldModel getField() {
        return field;
    }

    public void setField(FieldModel field) {
        this.field = field;
    }

    public static final Creator<NewsModel> CREATOR = new Creator<NewsModel>() {
        @Override
        public NewsModel createFromParcel(Parcel in) {
            return new NewsModel(in);
        }

        @Override
        public NewsModel[] newArray(int size) {
            return new NewsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected NewsModel(Parcel in) {
        field = in.readParcelable(getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(field);

    }
}
