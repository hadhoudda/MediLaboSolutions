package com.medilabosolutions.noteservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("notes")
public class Note {

    @Id
    private String id;

    @Field("patId")
    private Integer patId;

    private String patient;

    private String note;
}
