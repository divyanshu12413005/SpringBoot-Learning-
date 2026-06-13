package com.divyanshu.youtube.LearningRESTAPIs.service;

import com.divyanshu.youtube.LearningRESTAPIs.dto.AddStudentRequetDto;
import com.divyanshu.youtube.LearningRESTAPIs.dto.StudentDto;

import java.util.Map;
import java.util.List;

public interface StudentService {
    StudentDto getStudentById(Long id);

    List<StudentDto> getAllStudent();

    List<StudentDto> getAllStudents();

    StudentDto createStudent(AddStudentRequetDto addStudentRequetDto);

    void deleteStudent(Long id);

    StudentDto updateStudent(Long id, AddStudentRequetDto addStudentRequetDto);

    StudentDto patchStudent(Long id, Map<String, Object> updates);
}