package com.medilabosolutions.noteservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDto {

    private String id;
    @NotNull(message = "Le patId est obligatoire.")
    private Integer patId;
    @NotBlank(message = "La note ne peut pas être vide.")
    private String note;
    @CreatedDate
    private Instant createdNoteDate;
    @LastModifiedDate
    private Instant updatedNoteDate;
}
