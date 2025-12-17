package com.medilabosolutions.patientservice.mapper;

import com.medilabosolutions.patientservice.dto.PatientDto;
import com.medilabosolutions.patientservice.model.Patient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") //automatically inject this mapper (@Autowired)
public interface PatientMapper {

    PatientDto toDto(Patient entity);

    Patient toEntity(PatientDto dto);

    List<PatientDto> toDtoList(List<Patient> entities);
}
