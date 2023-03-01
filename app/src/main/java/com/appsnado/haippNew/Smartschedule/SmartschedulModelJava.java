package com.appsnado.haippNew.Smartschedule;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmartschedulModelJava {

    public String getSs_id() {
        return ss_id;
    }

    public void setSs_id(String ss_id) {
        this.ss_id = ss_id;
    }

    public String getSs_device_id() {
        return ss_device_id;
    }

    public void setSs_device_id(String ss_device_id) {
        this.ss_device_id = ss_device_id;
    }

    public String getSs_title() {
        return ss_title;
    }

    public void setSs_title(String ss_title) {
        this.ss_title = ss_title;
    }

    public String getSs_day() {
        return ss_day;
    }

    public void setSs_day(String ss_day) {
        this.ss_day = ss_day;
    }

    public String getSs_start_time() {
        return ss_start_time;
    }

    public void setSs_start_time(String ss_start_time) {
        this.ss_start_time = ss_start_time;
    }

    public String getSs_end_time() {
        return ss_end_time;
    }

    public void setSs_end_time(String ss_end_time) {
        this.ss_end_time = ss_end_time;
    }

    public String getSs_is_blocked() {
        return ss_is_blocked;
    }

    public void setSs_is_blocked(String ss_is_blocked) {
        this.ss_is_blocked = ss_is_blocked;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Expose
    @SerializedName("ss_id")
    private String ss_id;

    @Expose
    @SerializedName("ss_device_id")
    private String ss_device_id;

    @Expose
    @SerializedName("ss_title")
    private String ss_title;

    @Expose
    @SerializedName("ss_day")
    private String ss_day;

    @Expose
    @SerializedName("ss_start_time")
    private String ss_start_time;

    @Expose
    @SerializedName("ss_end_time")
    private String ss_end_time;

    @Expose
    @SerializedName("ss_is_blocked")
    private String ss_is_blocked;

    @Expose
    @SerializedName("created_at")
    private String created_at;

    @Expose
    @SerializedName("updated_at")
    private String updated_at;

}
