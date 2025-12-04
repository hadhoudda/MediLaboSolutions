package com.medilabosolutions.riskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {

    private String id;
    private Integer patId;
    private String note;
    private Instant createdNoteDate;
    private Instant updatedNoteDate;
}
