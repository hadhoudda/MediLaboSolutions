package com.medilabosolutions.frontservice.Beans;

import java.time.Instant;

public class NoteBean {

    private String id;
    private Integer patId;
    private String note;
    private Instant createdNoteDate;
    private Instant updatedNoteDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPatId() {
        return patId;
    }

    public void setPatId(Integer patId) {
        this.patId = patId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedNoteDate() {
        return createdNoteDate;
    }

    public void setCreatedNoteDate(Instant createdNoteDate) {
        this.createdNoteDate = createdNoteDate;
    }

    public Instant getUpdatedNoteDate() {
        return updatedNoteDate;
    }

    public void setUpdatedNoteDate(Instant updatedNoteDate) {
        this.updatedNoteDate = updatedNoteDate;
    }
}
