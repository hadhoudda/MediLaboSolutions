package com.medilabosolutions.noteservice.mapper;

import com.medilabosolutions.noteservice.dto.NoteDto;
import com.medilabosolutions.noteservice.model.Note;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") //injecter automatiquement ce mapper (@Autowired)
public interface NoteMapper {

    NoteDto toDto(Note entity);

    Note toEntity(NoteDto dto);

    List<NoteDto> toDtoList(List<Note> entities);
}

