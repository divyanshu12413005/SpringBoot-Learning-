package com.divyanshu.youtube.hospitalManagement.mapper;

import com.divyanshu.youtube.hospitalManagement.dto.AppointmentResponseDto;
import com.divyanshu.youtube.hospitalManagement.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "doctor.id", target = "doctorId")
    AppointmentResponseDto toAppointmentResponseDto(Appointment appointment);
}