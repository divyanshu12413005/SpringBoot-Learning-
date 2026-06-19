package com.divyanshu.youtube.hospitalManagement.mapper;

import com.divyanshu.youtube.hospitalManagement.dto.DoctorResponseDto;
import com.divyanshu.youtube.hospitalManagement.dto.OnboardDoctorRequestDto;
import com.divyanshu.youtube.hospitalManagement.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    DoctorResponseDto toDoctorResponseDto(Doctor doctor);

    Doctor toDoctor(OnboardDoctorRequestDto onboardDoctorRequestDto);
}