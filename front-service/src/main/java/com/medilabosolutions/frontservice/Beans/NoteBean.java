package com.medilabosolutions.frontservice.Beans;

import lombok.Data;

import java.time.Instant;

@Data
public class NoteBean {

    private String id;
    private Integer patId;
    private String note;
    private Instant createdNoteDate;
    private Instant updatedNoteDate;

}
