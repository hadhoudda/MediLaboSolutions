package com.medilabosolutions.riskservice.client;

import com.medilabosolutions.riskservice.config.FeignConfig;
import com.medilabosolutions.riskservice.dto.NoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "note-service",
        configuration = FeignConfig.class)
public interface NoteClient {

    @GetMapping("/api/notes/patient/{patId}")
    List<NoteDto> getNotesByPatientId (@PathVariable("patId") int patId);

}
