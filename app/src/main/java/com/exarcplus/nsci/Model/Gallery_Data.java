package com.exarcplus.nsci.Model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Gallery_Data implements Parcelable {
    @SerializedName("gallery_id")
    @Expose
    private String galleryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("total_count")
    @Expose
    private String totalCount;



    protected Gallery_Data(Parcel in) {
        galleryId = in.readString();
        title = in.readString();
        created = in.readString();
        totalCount = in.readString();
    }

    public static final Creator<Gallery_Data> CREATOR = new Creator<Gallery_Data>() {
        @Override
        public Gallery_Data createFromParcel(Parcel in) {
            return new Gallery_Data(in);
        }

        @Override
        public Gallery_Data[] newArray(int size) {
            return new Gallery_Data[size];
        }
    };

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(galleryId);
        dest.writeString(title);
        dest.writeString(created);
        dest.writeString(totalCount);
    }


}
