package com.medilabosolutions.frontservice.client;

import com.medilabosolutions.frontservice.Beans.NoteBean;
import com.medilabosolutions.frontservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "note-service",
        configuration = FeignConfig.class)
public interface NoteGatewayClient {

    @GetMapping("/api/notes/patient/{patId}")
    List<NoteBean> getNotesByPatientId (@PathVariable("patId") int patId);

    @PostMapping("/api/notes/patient")
    NoteBean createNote(@RequestBody NoteBean noteBean);

    @PutMapping("/api/notes/{id}")
    void updateNote(@PathVariable String id, @RequestBody NoteBean noteBean);

    @DeleteMapping("/api/notes/{id}")
    void deleteNotePatientById(@PathVariable String id);

}
