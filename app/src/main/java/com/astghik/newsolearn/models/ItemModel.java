package com.astghik.newsolearn.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemModel implements Serializable, Parcelable {

    @Expose
    @SerializedName("id")
    private String neewsID;

    @Expose
    @SerializedName("sectionName")
    private String neewsCategory;

    @Expose
    @SerializedName("headline")
    private String headline;

    @Expose
    @SerializedName("thumbnail")
    private String thumbnail;

    private boolean isSaved = false;
    private boolean isPined = false;

    public ItemModel() {
    }

    protected ItemModel(Parcel in) {
        neewsID = in.readString();
        neewsCategory = in.readString();
        headline = in.readString();
        thumbnail = in.readString();
        isSaved = in.readByte() != 0;
        isPined = in.readByte() != 0;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getNeewsCategory() {
        return neewsCategory;
    }

    public String getNeewsID() {
        return neewsID;
    }

    public boolean isPined() {
        return isPined;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setPined(boolean pined) {
        isPined = pined;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public void setNeewsCategory(String neewsCategory) {
        this.neewsCategory = neewsCategory;
    }

    public void setNeewsID(String neewsID) {
        this.neewsID = neewsID;
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(neewsID);
        parcel.writeString(neewsCategory);
        parcel.writeString(headline);
        parcel.writeString(thumbnail);
        parcel.writeByte((byte) (isSaved ? 1 : 0));
        parcel.writeByte((byte) (isPined ? 1 : 0));
    }
}
