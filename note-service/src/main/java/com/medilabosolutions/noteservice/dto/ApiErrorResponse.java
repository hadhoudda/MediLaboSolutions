package com.medilabosolutions.noteservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorResponse {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}