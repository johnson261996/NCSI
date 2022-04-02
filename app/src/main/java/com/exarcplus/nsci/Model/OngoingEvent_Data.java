package com.exarcplus.nsci.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OngoingEvent_Data {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("venue_url")
    @Expose
    private String venueUrl;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("from_time")
    @Expose
    private String fromTime;
    @SerializedName("to_time")
    @Expose
    private String toTime;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("interested_status")
    @Expose
    private String interestedStatus;
    @SerializedName("interested_users")
    @Expose
    private List<Object> interestedUsers = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getVenueUrl() {
        return venueUrl;
    }

    public void setVenueUrl(String venueUrl) {
        this.venueUrl = venueUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getInterestedStatus() {
        return interestedStatus;
    }

    public void setInterestedStatus(String interestedStatus) {
        this.interestedStatus = interestedStatus;
    }

    public List<Object> getInterestedUsers() {
        return interestedUsers;
    }

    public void setInterestedUsers(List<Object> interestedUsers) {
        this.interestedUsers = interestedUsers;
    }

}
