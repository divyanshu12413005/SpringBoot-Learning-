package com.divyanshu.youtube.hospitalManagement.mapper;

import com.divyanshu.youtube.hospitalManagement.dto.PatientResponseDto;
import com.divyanshu.youtube.hospitalManagement.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PatientMapper {
    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    PatientResponseDto toPatientResponseDto(Patient patient);
}