package com.astghik.newsolearn.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Parcelable, Serializable {

    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("total")
    private int total;
    @Expose
    @SerializedName("results")
    private List<NewsModel> newsList;

    protected Result(Parcel in) {
        status = in.readString();
        total = in.readInt();
        newsList = in.readArrayList(ClassLoader.getSystemClassLoader());

    }

    public int getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public List<NewsModel> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsModel> newsList) {
        this.newsList = newsList;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeInt(total);
        parcel.writeTypedList(newsList);
    }
}
