package com.medilabosolutions.noteservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteUpdateDto {

    @NotBlank(message = "La note ne peut pas être vide.")
    private String note;
}
