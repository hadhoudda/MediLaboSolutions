package com.medilabosolutions.noteservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@Document("notes")
public class Note {

    @Id
    private String id;

    @NotNull(message = "Le patId est obligatoire.")
    @Field("patId")
    private Integer patId;

    @NotBlank(message = "La note ne peut pas être vide.")
    private String note;

    @CreatedDate
    private Instant createdNoteDate;

    @LastModifiedDate
    private Instant updatedNoteDate;

}
