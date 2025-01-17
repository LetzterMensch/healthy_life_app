package com.example.gr.model.database.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.
/**
 * Entity mapped to table "ACTIVITY_DESC_TAG_LINK".
 */
public class ActivityDescTagLink {

    private Long id;
    private long activityDescriptionId;
    private long tagId;

    public ActivityDescTagLink() {
    }

    public ActivityDescTagLink(Long id) {
        this.id = id;
    }

    public ActivityDescTagLink(Long id, long activityDescriptionId, long tagId) {
        this.id = id;
        this.activityDescriptionId = activityDescriptionId;
        this.tagId = tagId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getActivityDescriptionId() {
        return activityDescriptionId;
    }

    public void setActivityDescriptionId(long activityDescriptionId) {
        this.activityDescriptionId = activityDescriptionId;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

}
